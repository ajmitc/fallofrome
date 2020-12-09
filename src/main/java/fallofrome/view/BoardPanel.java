package fallofrome.view;

import fallofrome.Model;
import fallofrome.game.Allegiance;
import fallofrome.game.Force;
import fallofrome.game.UnitType;
import fallofrome.game.board.Area;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class BoardPanel extends JPanel {
    private static final Logger logger = Logger.getLogger(BoardPanel.class.getName());

    private static final BasicStroke NORMAL_STROKE = new BasicStroke(1.f);
    private static final BasicStroke POLYGON_STROKE = new BasicStroke(2.f);

    private static final Font STRENGTH_FONT = new Font("Serif", Font.BOLD, 14);

    private static final Point[] REPLACEMENT_COORDS = new Point[]{
            new Point(1130, 45),
            new Point(1180, 45),
            new Point(1230, 45),
            new Point(1280, 45),
            new Point(1330, 45),
            new Point(1380, 45),
            new Point(1430, 45),
            new Point(1480, 45),
            new Point(1530, 45),
            new Point(1580, 45),

            new Point(1130, 45),
            new Point(1180, 45),
            new Point(1230, 45),
            new Point(1280, 45),
            new Point(1330, 45),
            new Point(1380, 45),
            new Point(1430, 45),
            new Point(1480, 45),
            new Point(1530, 45),
            new Point(1580, 45),
    };

    private Model model;
    private View view;

    private Map<String, Polygon> areaPolygons = new HashMap<>();
    private Map<String, List<Point>> areaForcePlacements = new HashMap<>();

    public BoardPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        Properties provinceProperties = new Properties();
        try {
            provinceProperties.load(BoardPanel.class.getClassLoader().getResourceAsStream("province.properties"));

            Iterator<String> iter = (Iterator<String>) provinceProperties.propertyNames().asIterator();
            while (iter.hasNext()){
                String key = iter.next();
                if (key.startsWith("province.")){
                    if (key.endsWith(".coords")){
                        String[] parts = key.split("\\.");
                        String provinceName = parts[1];
                        String areaId = parts[2];
                        String area = provinceName + "-" + areaId.trim();
                        Polygon polygon = new Polygon();
                        String[] coords = provinceProperties.getProperty(key).split(" ");
                        for (String coord: coords){
                            String[] latLon = coord.split(",");
                            polygon.addPoint(Integer.decode(latLon[0].trim()), Integer.decode(latLon[1].trim()));
                        }
                        areaPolygons.put(area, polygon);
                    }
                    else if (key.endsWith(".force.placement")){
                        String[] parts = key.split("\\.");
                        String provinceName = parts[1];
                        String areaId = parts[2];
                        String area = provinceName + "-" + areaId.trim();
                        List<Point> points = new ArrayList<>();
                        String[] coords = provinceProperties.getProperty(key).split(" ");
                        for (String coord: coords){
                            String[] latLon = coord.split(",");
                            points.add(new Point(Integer.decode(latLon[0].trim()), Integer.decode(latLon[1].trim())));
                        }
                        areaForcePlacements.put(area, points);
                    }
                }
            }
        }
        catch (Exception e){
            logger.severe("Unable to load province.properties: " + e);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;

        Image boardImage = ImageUtil.load("Map.png", 1620);
        g.drawImage(boardImage, 0, 0, null);

        // Draw control
        drawControl(g);

        // Draw Area Polygons
        if (Model.getProperty("map.area.polygon.show", false)) {
            g.setColor(Color.BLACK);
            g.setStroke(POLYGON_STROKE);
            for (Polygon polygon : areaPolygons.values()) {
                g.drawPolygon(polygon);
            }
            g.setStroke(NORMAL_STROKE);
        }

        // Draw Strength
        drawStrength(g);

        // Draw replacements
        drawReplacements(g);

        if (Model.getProperty("mapbuilder.show", false)) {
            g.setColor(Color.BLACK);
            g.drawString(view.getMapBuilderDialog().getMx() + ", " + view.getMapBuilderDialog().getMy(), 50, 50);

            Stroke oldStroke = g.getStroke();
            g.setStroke(POLYGON_STROKE);
            for (String areaCode: view.getMapBuilderDialog().getAreaPoints().keySet()) {
                List<Point> points = view.getMapBuilderDialog().getAreaPoints().get(areaCode);
                g.drawPolygon(
                        points.stream().mapToInt(p -> p.x).toArray(),
                        points.stream().mapToInt(p -> p.y).toArray(),
                        points.size()
                );
            }
            g.setStroke(oldStroke);
        }
    }

    private void drawControl(Graphics2D g){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(a -> {
                drawControl(g, a);
            });
        });
    }

    private void drawControl(Graphics2D g, Area area){
        if (area.getProvince().getController() != null) {
            Polygon polygon = areaPolygons.get(area.getProvince().getName() + "-" + area.getId());
            Color color = ViewUtil.ALLEGIANCE_COLOR.get(area.getProvince().getController());
            Color controlColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 80);
            g.setColor(controlColor);
            g.fillPolygon(polygon);
        }
    }

    private void drawStrength(Graphics2D g){
        model.getGame().getBoard().getProvinces().stream().forEach(p -> {
            p.getAreas().stream().forEach(a -> {
                drawStrength(g, a);
            });
        });
    }

    private void drawStrength(Graphics2D g, Area area){
        area.getForces().stream().forEach(f -> {
            drawStrength(g, f);
        });
    }

    private void drawStrength(Graphics2D g, Force force){
        g.setColor(ViewUtil.ALLEGIANCE_COLOR.get(force.getAllegiance()));
        Image image = null;

        if (force.getUnitType() == UnitType.REGULAR){
            image = ImageUtil.load("L.png");
        }
        else if (force.getUnitType() == UnitType.MILITIA){
            image = ImageUtil.load("M.png");
        }
        else if (force.getUnitType() == UnitType.RAIDING_PARTY){
            image = ImageUtil.load("N.png");
        }

        g.fillRect(force.getX(), force.getY(), image.getWidth(null), image.getHeight(null));
        g.drawImage(image, force.getX(), force.getY(), null);

        g.setColor(Color.BLACK);
        g.drawRect(force.getX(), force.getY(), image.getWidth(null), image.getHeight(null));

        int strengthOffsetX = 10;
        int strengthOffsetY = 0;
        g.setFont(STRENGTH_FONT);
        Rectangle bounds = ViewUtil.getStringBounds(g, "" + force.getStrength(),
                force.getX() + image.getWidth(null) - strengthOffsetX,
                force.getY() + image.getHeight(null) - strengthOffsetY);
        bounds.grow(3, 3);
        g.setColor(Color.WHITE);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(Color.BLACK);
        g.drawString("" + force.getStrength(),
                force.getX() + image.getWidth(null) - strengthOffsetX,
                force.getY() + image.getHeight(null) - strengthOffsetY);
    }

    private void drawReplacements(Graphics2D g){
        Map<Integer, Integer> romanReplacements   = model.getGame().getRomanReplacements();
        Map<Integer, Integer> persianReplacements = model.getGame().getPersianReplacements();
        for (int i = 1; i <= 20; ++i){
            Point p = REPLACEMENT_COORDS[i - 1];
            if (romanReplacements.containsKey(i) && romanReplacements.get(i) > 0){
                Rectangle bounds = ViewUtil.getStringBounds(g, "" + romanReplacements.get(i), p.x, p.y);
                bounds.grow(3, 3);
                g.setColor(Color.WHITE);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                g.setColor(ViewUtil.ALLEGIANCE_COLOR.get(Allegiance.ROMAN));
                g.drawString("" + romanReplacements.get(i), p.x, p.y + bounds.height);
            }
            if (persianReplacements.containsKey(i) && persianReplacements.get(i) > 0){
                g.setColor(ViewUtil.ALLEGIANCE_COLOR.get(Allegiance.PERSIAN));
                g.drawString("" + persianReplacements.get(i), p.x + 15, p.y);
            }
        }
    }

    public Area getAreaClicked(int mx, int my){
        for (Map.Entry<String, Polygon> entry: areaPolygons.entrySet()){
            if (entry.getValue().contains(mx, my)){
                return model.getGame().getBoard().getArea(entry.getKey());
            }
        }
        return null;
    }

    public Map<String, Polygon> getAreaPolygons(){ return areaPolygons; }

    public List<Point> getAreaForcePlacements(String areaCode) {
        return areaForcePlacements.get(areaCode);
    }

    public List<Point> getAreaForcePlacements(Area area) {
        return getAreaForcePlacements(area.getProvince().getName() + "-" + area.getId());
    }

    public void refresh(){
        repaint();
    }
}

package fallofrome.view;

import fallofrome.Model;
import fallofrome.game.board.Area;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class BoardPanel extends JPanel {
    private static final Logger logger = Logger.getLogger(BoardPanel.class.getName());

    private static final BasicStroke POLYGON_STROKE = new BasicStroke(2.f);

    private Model model;
    private View view;

    private Map<String, Polygon> areaPolygons = new HashMap<>();

    public BoardPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        Properties provinceProperties = new Properties();
        try {
            provinceProperties.load(BoardPanel.class.getResourceAsStream("province.properties"));
        }
        catch (Exception e){
            logger.severe("Unable to load province.properties: " + e);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics;

        Image boardImage = ImageUtil.load("board2.jpg", 1620);
        g.drawImage(boardImage, 0, 0, null);



        if (Model.getProperty("mapbuilder.show", false)) {
            g.setColor(Color.BLACK);
            g.drawString(view.getMapBuilderDialog().getMx() + ", " + view.getMapBuilderDialog().getMy(), 50, 50);

            Stroke oldStroke = g.getStroke();
            g.setStroke(POLYGON_STROKE);
            for (String province : view.getMapBuilderDialog().getProvincePoints().keySet()) {
                List<Point> points = view.getMapBuilderDialog().getProvincePoints().get(province);
                g.drawPolygon(
                        points.stream().mapToInt(p -> p.x).toArray(),
                        points.stream().mapToInt(p -> p.y).toArray(),
                        points.size()
                );
            }
            g.setStroke(oldStroke);
        }
    }

    public Area getAreaClicked(int mx, int my){
        return null;
    }

    public void refresh(){
        repaint();
    }
}

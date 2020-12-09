package fallofrome.dev;

import fallofrome.Model;
import fallofrome.game.board.Area;
import fallofrome.game.board.Province;
import fallofrome.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MapBuilderDialog extends JDialog implements MouseListener, MouseMotionListener {
    private static Logger logger = Logger.getLogger(MapBuilderDialog.class.getName());

    private Model model;
    private View view;

    private int mx, my;

    private static enum Mode{
        AREA_POLYGONS,
        AREA_FORCE_PLACEMENT
    }

    private Mode mode = Mode.AREA_POLYGONS;

    private JComboBox<Mode> cbxMode = new JComboBox<>();
    private JComboBox<String> cbxProvinceNames1 = new JComboBox<>();
    private JComboBox<String> cbxAreaCode = new JComboBox<>();

    private Map<String, List<Point>> areaPoints = new HashMap<>();
    private Map<String, List<Point>> forcePoints = new HashMap<>();

    public MapBuilderDialog(Model model, View view){
        super(view.getFrame(), "Map Builder", false);
        this.model = model;
        this.view = view;
        setSize(200, 200);
        setLocationRelativeTo(null);

        cbxMode.addItem(Mode.AREA_POLYGONS);
        cbxMode.addItem(Mode.AREA_FORCE_PLACEMENT);

        cbxMode.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mode = (Mode) cbxMode.getSelectedItem();
                logger.info("Mode changed to " + mode);
            }
        });

        cbxProvinceNames1.addItem("");
        for (String provinceName: Province.PROVINCE_NAMES) {
            cbxProvinceNames1.addItem(provinceName);
        }
        for (int i = 0; i < 5; ++i)
            cbxAreaCode.addItem("" + ((char) ('A' + i)));

        JButton btnExport = new JButton("Export");
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportPoints();
            }
        });

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        getContentPane().add(cbxMode);
        getContentPane().add(cbxProvinceNames1);
        getContentPane().add(cbxAreaCode);
        getContentPane().add(btnExport);

        view.getGamePanel().getBoardPanel().addMouseListener(this);
        view.getGamePanel().getBoardPanel().addMouseMotionListener(this);

        // Load in existing polygons
        for (Map.Entry<String, Polygon> entry: view.getGamePanel().getBoardPanel().getAreaPolygons().entrySet()){
            String key = entry.getKey().replace('-', '.');
            areaPoints.put(key, new ArrayList<>());
            for (int i = 0; i < entry.getValue().npoints; ++i) {
                areaPoints.get(key).add(new Point(entry.getValue().xpoints[i], entry.getValue().ypoints[i]));
            }
        }
    }

    public String getSelectedArea(){
        String selectedProvince = "" + cbxProvinceNames1.getSelectedItem();
        String selectedAreaCode = "" + cbxAreaCode.getSelectedItem();
        return selectedProvince + "." + selectedAreaCode;
    }

    public void exportPoints(){
        if (mode == Mode.AREA_POLYGONS)
            exportAreaPolygonPoints();
        else
            exportAreaForcePlacements();
    }

    private void exportAreaPolygonPoints(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("province.properties"));
            for (String area: getAreaPoints().keySet()){
                String coords = getAreaPoints().get(area).stream().map(p -> p.x + "," + p.y).collect(Collectors.joining(" "));
                bufferedWriter.write("province." + area + ".coords = " + coords + "\n");
            }
            bufferedWriter.close();
        }
        catch (Exception e){
            logger.severe("" + e);
        }
    }

    private void exportAreaForcePlacements(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("province.properties"));
            for (String area: forcePoints.keySet()){
                String coords = forcePoints.get(area).stream().map(p -> p.x + "," + p.y).collect(Collectors.joining(" "));
                bufferedWriter.write("province." + area + ".force.placement = " + coords + "\n");
            }
            bufferedWriter.close();
        }
        catch (Exception e){
            logger.severe("" + e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = new Point(e.getX(), e.getY());
        String area = getSelectedArea();
        if (mode == Mode.AREA_POLYGONS) {
            if (!areaPoints.containsKey(area))
                areaPoints.put(area, new ArrayList<>());
            areaPoints.get(area).add(point);
        }
        else if (mode == Mode.AREA_FORCE_PLACEMENT) {
            if (!forcePoints.containsKey(area))
                forcePoints.put(area, new ArrayList<>());
            forcePoints.get(area).add(point);
        }
        logger.info(area + ": (" + point.x + ", " + point.y + ")");
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
        view.refresh();
    }

    public int getMx() {
        return mx;
    }

    public int getMy() {
        return my;
    }

    public Map<String, List<Point>> getAreaPoints() {
        return areaPoints;
    }
}

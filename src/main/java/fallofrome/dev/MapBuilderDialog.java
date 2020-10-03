package fallofrome.dev;

import fallofrome.Model;
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

    private JComboBox<String> cbxProvinceNames1 = new JComboBox<>();
    private JComboBox<String> cbxProvinceNames2 = new JComboBox<>();
    private JComboBox<String> cbxProvinceNames3 = new JComboBox<>();

    private Map<String, List<Point>> provincePoints = new HashMap<>();

    public MapBuilderDialog(Model model, View view){
        super(view.getFrame(), "Map Builder", false);
        this.model = model;
        this.view = view;
        setSize(200, 200);
        setLocationRelativeTo(null);

        cbxProvinceNames1.addItem("");
        cbxProvinceNames2.addItem("");
        cbxProvinceNames3.addItem("");
        for (String provinceName: Province.PROVINCE_NAMES) {
            cbxProvinceNames1.addItem(provinceName);
            cbxProvinceNames2.addItem(provinceName);
            cbxProvinceNames3.addItem(provinceName);
        }

        JButton btnExport = new JButton("Export");
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportPoints();
            }
        });

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        getContentPane().add(cbxProvinceNames1);
        getContentPane().add(cbxProvinceNames2);
        getContentPane().add(cbxProvinceNames3);
        getContentPane().add(btnExport);

        view.getGamePanel().getBoardPanel().addMouseListener(this);
        view.getGamePanel().getBoardPanel().addMouseMotionListener(this);
    }

    public List<String> getSelectedProvinces(){
        List<String> selectedProvinces = new ArrayList<>();
        String cb1 = "" + cbxProvinceNames1.getSelectedItem();
        String cb2 = "" + cbxProvinceNames2.getSelectedItem();
        String cb3 = "" + cbxProvinceNames3.getSelectedItem();
        if (!cb1.equals(""))
            selectedProvinces.add(cb1);
        if (!cb2.equals(""))
            selectedProvinces.add(cb2);
        if (!cb3.equals(""))
            selectedProvinces.add(cb3);
        return selectedProvinces;
    }

    public void exportPoints(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("province.properties"));
            for (String province: getProvincePoints().keySet()){
                String coords = getProvincePoints().get(province).stream().map(p -> p.x + "," + p.y).collect(Collectors.joining(" "));
                bufferedWriter.write("province." + province + ".coords = " + coords + "\n");
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
        for (String province: getSelectedProvinces()){
            if (!provincePoints.containsKey(province))
                provincePoints.put(province, new ArrayList<>());
            provincePoints.get(province).add(point);
            logger.info(province + ": (" + point.x + ", " + point.y + ")");
        }
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

    public Map<String, List<Point>> getProvincePoints() {
        return provincePoints;
    }
}

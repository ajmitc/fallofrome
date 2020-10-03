package fallofrome.view;

import fallofrome.Model;
import fallofrome.dev.MapBuilderDialog;

import javax.swing.*;
import java.awt.*;

public class View {
    private static final String MAINMENU = "MAINMENU";
    private static final String GAME = "GAME";

    private Model model;
    private JFrame frame;

    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private MapBuilderDialog dialog;

    public View(Model model){
        this.model = model;
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setLocation(0, 0);
        frame.setTitle("Fall of Rome");

        mainMenuPanel = new MainMenuPanel(model, this);
        gamePanel = new GamePanel(model, this);

        frame.getContentPane().setLayout(new CardLayout());
        frame.getContentPane().add(mainMenuPanel, MAINMENU);
        frame.getContentPane().add(gamePanel, GAME);

        if (Model.getProperty("mapbuilder.show", false)) {
            dialog = new MapBuilderDialog(model, this);
            dialog.setVisible(true);
        }
    }

    public void showGame(){
        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
        cardLayout.show(frame.getContentPane(), GAME);
    }

    public void showMainMenu(){
        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
        cardLayout.show(frame.getContentPane(), MAINMENU);
        frame.setGlassPane(null);
    }

    public JFrame getFrame() {
        return frame;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public MapBuilderDialog getMapBuilderDialog() {
        return dialog;
    }

    public void refresh(){
        gamePanel.refresh();
    }
}

package fallofrome.view;

import fallofrome.GameController;
import fallofrome.Model;
import fallofrome.game.Game;
import fallofrome.game.scenario.Scenario;
import fallofrome.game.scenario.Scenario1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private Model model;
    private View view;

    public MainMenuPanel(Model model, View view){
        super(new GridLayout(1, 2));
        this.view = view;

        JButton btnNewGame = new JButton("New Game");
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setGame(new Game());
                Scenario scenario = new Scenario1();
                model.getGame().setup(scenario);
                view.showGame();
                if (!Model.getProperty("mapbuilder.show", false))
                    GameController.getInstance().run();
            }
        });
        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel buttonpanel = new JPanel();
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(100,100, 100, 100));
        new GridBagLayoutHelper(buttonpanel, true)
                .setAnchor(GridBagConstraints.CENTER)
                .add(btnNewGame)
                .nextRow()
                .add(btnExit)
                .nextRow()
                ;

        Image coverImage = ImageUtil.load("cover.jpg");
        JLabel lblCoverImage = new JLabel(new ImageIcon(coverImage));

        add(lblCoverImage);
        add(buttonpanel);
    }
}

package fallofrome.view;

import fallofrome.Model;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Model model;
    private View view;

    private BoardPanel boardPanel;
    private GameSidePanel gameSidePanel;

    public GamePanel(Model model, View view){
        super(new BorderLayout());
        this.model = model;
        this.view = view;

        boardPanel = new BoardPanel(model, view);
        gameSidePanel = new GameSidePanel(model, view);

        add(boardPanel, BorderLayout.CENTER);
        add(gameSidePanel, BorderLayout.EAST);
    }

    public void refresh(){
        boardPanel.refresh();
        gameSidePanel.refresh();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public GameSidePanel getGameSidePanel(){return gameSidePanel;}
}

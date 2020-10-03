package fallofrome.view;

import fallofrome.Model;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Model model;
    private View view;

    private BoardPanel boardPanel;

    public GamePanel(Model model, View view){
        super(new BorderLayout());
        this.model = model;
        this.view = view;

        boardPanel = new BoardPanel(model, view);

        add(boardPanel, BorderLayout.CENTER);
    }

    public void refresh(){
        boardPanel.refresh();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}

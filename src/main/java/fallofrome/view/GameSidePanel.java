package fallofrome.view;

import fallofrome.Model;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.logging.Logger;

public class GameSidePanel extends JPanel {
    private static final Logger logger = Logger.getLogger(GameSidePanel.class.getName());

    public static final String NORMAL = "normal";
    public static final String ITALIC = "italic";
    public static final String BOLD   = "bold";
    public static final String BOLD_ITALIC = "bold italic";

    private Model model;
    private View view;

    private JTextPane txtOutput;
    private StyledDocument txtOutputDoc;

    private JLabel lblRomanTreasury;
    private JLabel lblPersianTreasury;

    public GameSidePanel(Model model, View view){
        super(new BorderLayout());
        this.model = model;
        this.view = view;

        JLabel lblRomanTreasuryTitle = new JLabel("Roman Treasury: ");
        JLabel lblPersianTreasuryTitle = new JLabel("Persian Treasury: ");
        lblRomanTreasury = new JLabel("0");
        lblPersianTreasury = new JLabel("0");

        JPanel romanTreasuryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        romanTreasuryPanel.add(lblRomanTreasuryTitle);
        romanTreasuryPanel.add(lblRomanTreasury);

        JPanel persianTreasuryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        persianTreasuryPanel.add(lblPersianTreasuryTitle);
        persianTreasuryPanel.add(lblPersianTreasury);

        JPanel treasuryPanel = new JPanel();
        treasuryPanel.setLayout(new BoxLayout(treasuryPanel, BoxLayout.PAGE_AXIS));
        treasuryPanel.add(romanTreasuryPanel);
        treasuryPanel.add(persianTreasuryPanel);

        txtOutput = new JTextPane();
        txtOutputDoc = txtOutput.getStyledDocument();
        txtOutput.setEditable(false);
        txtOutput.setAutoscrolls(true);
        txtOutput.setDoubleBuffered(true);
        txtOutput.setMinimumSize(new Dimension(250, 250));
        txtOutput.setPreferredSize(new Dimension(250, 250));

        addStyles();

        add(treasuryPanel, BorderLayout.NORTH);
        add(new JScrollPane(txtOutput), BorderLayout.CENTER);
    }

    private void addStyles(){
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = txtOutputDoc.addStyle(NORMAL, def);
        StyleConstants.setFontFamily(def, "SansSerif");
        StyleConstants.setFontSize(regular, 12);

        Style s = txtOutputDoc.addStyle(ITALIC, regular);
        StyleConstants.setItalic(s, true);

        s = txtOutputDoc.addStyle(BOLD, regular);
        StyleConstants.setBold(s, true);

        s = txtOutputDoc.addStyle(BOLD_ITALIC, regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setItalic(s, true);

        /*
        s = txtOutputDoc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = txtOutputDoc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        s = txtOutputDoc.addStyle("icon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon pigIcon = createImageIcon("images/Pig.gif",
                                            "a cute pig");
        if (pigIcon != null) {
            StyleConstants.setIcon(s, pigIcon);
        }

        s = txtOutputDoc.addStyle("button", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon soundIcon = createImageIcon("images/sound.gif",
                                              "sound icon");
        JButton button = new JButton();
        if (soundIcon != null) {
            button.setIcon(soundIcon);
        } else {
            button.setText("BEEP");
        }
        button.setCursor(Cursor.getDefaultCursor());
        button.setMargin(new Insets(0,0,0,0));
        button.setActionCommand(buttonString);
        button.addActionListener(this);
        StyleConstants.setComponent(s, button);
         */
    }

    public void refresh(){
        lblRomanTreasury.setText("" + model.getGame().getRomanTreasury());
        lblPersianTreasury.setText("" + model.getGame().getPersianTreasury());
    }


    public void appendOutputLn(String text){
        appendOutputLn(text, NORMAL);
    }

    public void appendOutputLn(String text, String style){
        appendOutput(text + "\n", style);
    }

    public void appendOutput(String text, String style){
        try {
            txtOutputDoc.insertString(txtOutputDoc.getLength(), text, txtOutputDoc.getStyle(style));
        }
        catch (BadLocationException ble){
            logger.severe("" + ble);
        }
    }
}

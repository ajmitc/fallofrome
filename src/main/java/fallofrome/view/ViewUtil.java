package fallofrome.view;

import fallofrome.game.Allegiance;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.HashMap;
import java.util.Map;

public class ViewUtil {
    public static final Font FORM_KEY_FONT   = new Font("Serif", Font.BOLD, 12);
    public static final Font FORM_VALUE_FONT = new Font("Serif", Font.PLAIN, 12);
    public static final Font FORM_VALUE_FONT_ITALICS = new Font("Serif", Font.ITALIC, 12);
    public static final Font FORM_TITLE_FONT = new Font("Serif", Font.BOLD, 16);

    public static final Map<Allegiance, Color> ALLEGIANCE_COLOR = new HashMap<>();

    static {
        ALLEGIANCE_COLOR.put(Allegiance.ROMAN, Color.RED);
        ALLEGIANCE_COLOR.put(Allegiance.REBELLIOUS_ROMAN, Color.PINK);
        ALLEGIANCE_COLOR.put(Allegiance.DACIAN_BARBARIAN, Color.GRAY);
        ALLEGIANCE_COLOR.put(Allegiance.GERMAN_BARBARIAN, Color.GRAY);
        ALLEGIANCE_COLOR.put(Allegiance.HUN_BARBARIAN, Color.GRAY);
        ALLEGIANCE_COLOR.put(Allegiance.PICTISH_BARBARIAN, Color.GRAY);
        ALLEGIANCE_COLOR.put(Allegiance.SCYTHIAN_BARBARIAN, Color.GRAY);
        ALLEGIANCE_COLOR.put(Allegiance.TAURICAN_BARBARIAN, Color.GRAY);
        ALLEGIANCE_COLOR.put(Allegiance.INDEPENDENT_STATE, Color.YELLOW);
        ALLEGIANCE_COLOR.put(Allegiance.PERSIAN, Color.MAGENTA);
        ALLEGIANCE_COLOR.put(Allegiance.REVOLTING_MILITIA, Color.GREEN);
    }

    public static Rectangle getStringBounds(Graphics2D g2, String str, int x, int y) {
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
        return gv.getPixelBounds(null, x, y);
    }

    public static Dimension getScreenSize(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }


    private ViewUtil(){}
}

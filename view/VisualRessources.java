package view;

import java.awt.*;
import java.io.File;

public class VisualRessources {
    private static VisualRessources instance;

    public static VisualRessources getInstance() {
        if (instance == null) {
            instance = new VisualRessources();
        }
        return instance;
    }

    public Color customBlue = new Color(27,148,209);
    public Font customFont;
    public Font customFontItal;
    public Font customFontSuperItal;

    private VisualRessources() {
        // setting the custom fonts for the app
        try{
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/galacticstorm.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.ITALIC, 40); // if font isn't imported, use a classic font
        }
        try{
            customFontItal = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/galacticstormital.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFontItal);
        } catch (Exception e) {
            e.printStackTrace();
            customFontItal = new Font("Arial", Font.ITALIC, 40); // if font isn't imported, use a classic font
        }
        try{
            customFontSuperItal = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/galacticstormsuperital.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFontSuperItal);
        } catch (Exception e) {
            e.printStackTrace();
            customFontSuperItal = new Font("Arial", Font.ITALIC, 40); // if font isn't imported, use a classic font
        }
    }
}

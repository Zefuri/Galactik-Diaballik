package view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VisualResources {
    private static VisualResources instance;

    public static VisualResources getInstance() {
        if (instance == null) {
            instance = new VisualResources();
        }
        return instance;
    }

    public Color customBlue = new Color(27,148,209);

    public Font customFont;
    public Font customFontItal;
    public Font customFontSuperItal;

    BufferedImage userIconImage;
    BufferedImage computerIconImage;

    private VisualResources(){
        // setting the custom fonts for the app
        try{
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/galacticstorm.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.ITALIC, 40); // if font isn't imported, use a classic font
        }
        try{
            customFontItal = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/galacticstormital.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFontItal);
        } catch (Exception e) {
            e.printStackTrace();
            customFontItal = new Font("Arial", Font.ITALIC, 40); // if font isn't imported, use a classic font
        }
        try{
            customFontSuperItal = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/galacticstormsuperital.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFontSuperItal);
        } catch (Exception e) {
            e.printStackTrace();
            customFontSuperItal = new Font("Arial", Font.ITALIC, 40); // if font isn't imported, use a classic font
        }

        // loading images
        try{
            userIconImage = ImageIO.read(new File("resources/images/man-avatar.png"));
            computerIconImage = ImageIO.read(new File("resources/images/computer.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

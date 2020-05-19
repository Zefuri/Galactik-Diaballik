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

    public Color customBlue = new Color(27, 148, 209);
    public Color customLightBlue = new Color(115, 198,240);
    public Color customRed = new Color(208, 0, 0);
    public Color customGrassGreen = new Color(111, 176, 72);

    public Font customFont;
    public Font customFontItal;
    public Font customFontSuperItal;

    BufferedImage userIconImage;
    BufferedImage computerIconImage;
    BufferedImage resetIconImage;
    BufferedImage backwardIconImage;

    private VisualResources(){
        // setting the custom fonts for the app
        try{
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/galacticstorm.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.ITALIC, 40); // if font isn't imported, use a classic font
        }
        try{
            customFontItal = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/galacticstormital.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFontItal);
        } catch (Exception e) {
            e.printStackTrace();
            customFontItal = new Font("Arial", Font.ITALIC, 40); // if font isn't imported, use a classic font
        }
        try{
            customFontSuperItal = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/galacticstormsuperital.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFontSuperItal);
        } catch (Exception e) {
            e.printStackTrace();
            customFontSuperItal = new Font("Arial", Font.ITALIC, 40); // if font isn't imported, use a classic font
        }

        // loading images
        try{
            userIconImage = ImageIO.read(getClass().getResourceAsStream("/resources/images/man-avatar.png"));
            computerIconImage = ImageIO.read(getClass().getResourceAsStream("/resources/images/computer.png"));
            resetIconImage = ImageIO.read(getClass().getResourceAsStream("/resources/images/reset.png"));
            backwardIconImage = ImageIO.read(getClass().getResourceAsStream("/resources/images/backward.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

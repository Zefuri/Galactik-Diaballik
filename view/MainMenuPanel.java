package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel() {
        this.setLayout(new GridLayout(2,1)); // upper area for the title, lower area for the buttons

        // importing a font to use throughout the game
        Font customFont;
        try{
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/planetkosmos.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 40); // if font isn't imported, use a classic font
        }

        JPanel mainTitlePanel = new JPanel(new FlowLayout()); // panel will hold the title
        this.add(mainTitlePanel);

        // TODO : create the title

        JPanel buttonsPanel = new JPanel(new GridLayout(3,1)); // panel will hold the buttons. need as many rows as buttons
        this.add(buttonsPanel);

        Font buttonsFont = customFont.deriveFont(45f); // use the font and set its size
        Dimension buttonsDimensions = new Dimension(300, 70); // arbitrary button dimension

        JPanel playPanel = new JPanel(new FlowLayout()); // button needs to be in its own flow layout panel to collapse borders
        buttonsPanel.add(playPanel);
        JButton playButton = new JButton("Jouer");
        playButton.setFont(buttonsFont);
        playButton.setPreferredSize(buttonsDimensions);
        playPanel.add(playButton);

        JPanel settingsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(settingsPanel);
        JButton settingsButton = new JButton("Option");
        settingsButton.setFont(buttonsFont);
        settingsButton.setPreferredSize(buttonsDimensions);
        settingsPanel.add(settingsButton);

        JPanel quitPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(quitPanel);
        JButton quitButton = new JButton("Quitter");
        quitButton.setFont(buttonsFont);
        quitButton.setPreferredSize(buttonsDimensions);
        quitPanel.add(quitButton);
    }
}

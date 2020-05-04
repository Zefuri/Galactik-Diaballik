package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel() {
        Color backgroundColor = new Color(27,148,209);
        this.setLayout(new GridLayout(2,1)); // upper area for the title, lower area for the buttons

        // importing a font to use throughout the game
        Font customFont;
        try{
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/galacticstormsuperital.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 40); // if font isn't imported, use a classic font
        }

        JPanel mainTitlePanel = new JPanel(new GridLayout(2,1)); // panel will hold the title
        mainTitlePanel.setBackground(backgroundColor); // without this a fine raw will appear
        this.add(mainTitlePanel);

        Font titleFont = customFont.deriveFont(100f); // create a font for the title

        // JLabel doesn't do multi line stuff so need for two labels and thus two panels
        // TODO : Find a way to lower the top label
        JPanel firstTitlePanel = new JPanel();
        firstTitlePanel.setBackground(backgroundColor);
        mainTitlePanel.add(firstTitlePanel);
        JLabel firstMainTitleLabel = new JLabel("Galactik");
        firstMainTitleLabel.setFont(titleFont);
        firstTitlePanel.add(firstMainTitleLabel);

        JPanel secondTitlePanel = new JPanel();
        secondTitlePanel.setBackground(backgroundColor);
        mainTitlePanel.add(secondTitlePanel);
        JLabel secondMainTitleLabel = new JLabel("   Diaballik"); // offset to make it look good
        secondMainTitleLabel.setFont(titleFont);
        secondTitlePanel.add(secondMainTitleLabel);

        JPanel buttonsPanel = new JPanel(new GridLayout(3,1)); // panel will hold the buttons. need as many rows as buttons
        this.add(buttonsPanel);

        Font buttonsFont = customFont.deriveFont(45f); // use the font and set its size
        Dimension buttonsDimensions = new Dimension(300, 70); // arbitrary button dimension

        JPanel playPanel = new JPanel(new FlowLayout()); // button needs to be in its own flow layout panel to collapse borders
        playPanel.setBackground(backgroundColor);
        buttonsPanel.add(playPanel);
        JButton playButton = new JButton("Jouer");
        playButton.setFont(buttonsFont);
        playButton.setPreferredSize(buttonsDimensions);
        playPanel.add(playButton);

        JPanel settingsPanel = new JPanel(new FlowLayout());
        settingsPanel.setBackground(backgroundColor);
        buttonsPanel.add(settingsPanel);
        JButton settingsButton = new JButton("Option");
        settingsButton.setFont(buttonsFont);
        settingsButton.setPreferredSize(buttonsDimensions);
        settingsPanel.add(settingsButton);

        JPanel quitPanel = new JPanel(new FlowLayout());
        quitPanel.setBackground(backgroundColor);
        buttonsPanel.add(quitPanel);
        JButton quitButton = new JButton("Quitter");
        quitButton.setFont(buttonsFont);
        quitButton.setPreferredSize(buttonsDimensions);
        quitPanel.add(quitButton);
    }
}

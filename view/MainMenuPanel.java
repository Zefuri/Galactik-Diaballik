package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel extends JPanel {

    private static Color backgroundColor;
    private static JPanel buttonsPanel;
    private static Font buttonsFont;
    private static Dimension buttonsDimensions;

    public MainMenuPanel() {
        backgroundColor = VisualResources.getInstance().customBlue;
        this.setLayout(new GridLayout(2,1)); // upper area for the title, lower area for the buttons

        // importing a font to use throughout the game
        Font customFont = VisualResources.getInstance().customFontSuperItal;

        JPanel mainTitlePanel = new JPanel();
        mainTitlePanel.setBackground(backgroundColor); // without this a fine ray will appear
        this.add(mainTitlePanel);

        Font titleFont = customFont.deriveFont(100f); // create a font for the title

        JLabel titleLabel = new JLabel("<html>Galactik<br>&emsp;Diaballik"); // html for multi-line label and "&emsp;" for a tab. don't ask.
        titleLabel.setFont(titleFont);
        mainTitlePanel.add(titleLabel);

        buttonsPanel = new JPanel(new GridLayout(3,1)); // panel will hold the buttons. need as many rows as buttons
        this.add(buttonsPanel);

        buttonsFont = customFont.deriveFont(45f); // use the font and set its size
        buttonsDimensions = new Dimension(300, 70); // arbitrary button dimension

        //create the play button
        JButton playButton = createButton("Jouer");
        playButton.addActionListener(actionEvent -> System.out.println("clicked on play")); // action listener for when button is clicked

        // create the settings button
        JButton settingsButton = createButton("Option");
        settingsButton.addActionListener(actionEvent -> System.out.println("clicked on settings"));

        // create the quit button
        JButton quitButton = createButton("Quitter");
        quitButton.addActionListener(actionEvent -> System.out.println("clicked on quit"));
    }

    /*
    Function creates and sets a button then adds it to the buttons grid layout panel
     */
    private JButton createButton(String text) {
        JPanel currentButtonPanel = new JPanel(new FlowLayout()); // button needs to be in its own flow layout panel to collapse borders
        currentButtonPanel.setBackground(backgroundColor);
        buttonsPanel.add(currentButtonPanel);
        JButton button = new JButton(text);
        button.setFont(buttonsFont);
        button.setPreferredSize(buttonsDimensions);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorder(new LineBorder(Color.BLUE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(BorderFactory.createEmptyBorder());
            }
        });
        currentButtonPanel.add(button);

        return button;
    }
}

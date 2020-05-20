package view;

import model.enums.UserInput;
import patterns.Observable;
import patterns.ObservableHandler;
import patterns.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuPanel extends JPanel implements Observable {

    private ObservableHandler observableHandler;

    private static Color backgroundColor;
    private static JPanel buttonsPanel;
    private static Font buttonsFont;
    private static Dimension buttonsDimensions;

    public MainMenuPanel() {
        observableHandler = new ObservableHandler();

        backgroundColor = VisualResources.getInstance().customBlue;
        this.setLayout(new GridLayout(2,1)); // upper area for the title, lower area for the buttons

        // importing a font to use throughout the game
        Font customFont = VisualResources.getInstance().customFontSuperItal;

        JPanel mainTitlePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        mainTitlePanel.setBackground(backgroundColor); // without this a fine ray will appear
        this.add(mainTitlePanel);

        Font titleFont = customFont.deriveFont(100f); // create a font for the title

        JLabel titleLabel = new JLabel("<html>Galactik<br>&emsp;Diaballik"); // html for multi-line label and "&emsp;" for a tab. don't ask.
        titleLabel.setBorder(new EmptyBorder(0,0,0,20));
        titleLabel.setFont(titleFont);
        mainTitlePanel.add(titleLabel, gbc);

        buttonsPanel = new JPanel(new GridLayout(4,1)); // panel will hold the buttons. need as many rows as buttons
        this.add(buttonsPanel);

        buttonsFont = customFont.deriveFont(45f); // use the font and set its size
        buttonsDimensions = new Dimension(400, 70); // arbitrary button dimension

        //create the play button
        JButton playButton = createButton("Jouer");
        playButton.addActionListener(actionEvent -> notify(UserInput.CLICKED_PLAY)); // action listener for when button is clicked

        // create the settings button
        JButton settingsButton = createButton("Charger");
        settingsButton.addActionListener(actionEvent -> notify(UserInput.CLICKED_LOAD));

        // create the visualize button
        JButton visualizeButton = createButton("Visualiser partie");
        visualizeButton.addActionListener(actionEvent -> notify(UserInput.CLICKED_VISU));

        // create the quit button
        JButton quitButton = createButton("Quitter");
        quitButton.addActionListener(actionEvent -> notify(UserInput.CLICKED_QUIT));
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

    @Override
    public void addObserver(Observer observer) {
        observableHandler.addObserver(observer);
    }

    @Override
    public void notify(Object object) {
        observableHandler.notify(object);
    }
}

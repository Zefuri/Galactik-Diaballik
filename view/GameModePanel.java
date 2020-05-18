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
import java.awt.image.BufferedImage;

public class GameModePanel extends JPanel implements Observable {

    private ObservableHandler observableHandler;

    private JPanel pvpPanel;
    private JPanel pvpCenterPanel;
    private JPanel pvcPanel;
    private JPanel pvcCenterPanel;
    private JPanel cvcPanel;
    private JPanel cvcCenterPanel;

    public GameModePanel() {
        observableHandler = new ObservableHandler();

        this.setBackground(VisualResources.getInstance().customBlue);
        this.setLayout(new GridLayout(4,1)); // divide the panel into 4 rows

        GridBagConstraints gbc = new GridBagConstraints(); // gbc to center both vertically and horizontally
        gbc.anchor = GridBagConstraints.CENTER;

        // Top title panel and label
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(VisualResources.getInstance().customBlue); // use the custom blue
        this.add(titlePanel);
        JLabel titleLabel = new JLabel("Choisi ton mode de jeu"); // title at the top of the panel
        titleLabel.setBorder(new EmptyBorder(0,0,0,20)); // for the italic text
        titleLabel.setFont(VisualResources.getInstance().customFontSuperItal.deriveFont(60f)); // written in super italic
        titlePanel.add(titleLabel, gbc); // added and centered with the GBC

        createClickablePanels(); // create the clickable panels
    }

    /*
    Code segmentation script. Creates the clickable panels and sets their mouse adapters
    When mouse hovers over, the borders become red
     */
    private void createClickablePanels() {
        // player vs player clickable panel on second line
        pvpPanel = createGameModeSubPanel(VisualResources.getInstance().userIconImage, "Joueur vs Joueur", VisualResources.getInstance().userIconImage);
        pvpPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameModePanel.this.notify(UserInput.CLICKED_PVP);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setHover(true, pvpPanel, pvpCenterPanel);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setHover(false, pvpPanel, pvpCenterPanel);
            }
        });
        this.add(pvpPanel);

        // player vs computer clickable panel on third line
        pvcPanel = createGameModeSubPanel(VisualResources.getInstance().userIconImage, "Joueur vs IA", VisualResources.getInstance().computerIconImage);
        pvcPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameModePanel.this.notify(UserInput.CLICKED_PVC);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setHover(true, pvcPanel, pvcCenterPanel);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setHover(false, pvcPanel, pvcCenterPanel);
            }
        });
        this.add(pvcPanel);

        // computer vs computer clickable panel on fourth line
        cvcPanel = createGameModeSubPanel(VisualResources.getInstance().computerIconImage, "IA vs IA", VisualResources.getInstance().computerIconImage);
        cvcPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameModePanel.this.notify(UserInput.CLICKED_CVC);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setHover(true, cvcPanel, cvcCenterPanel);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setHover(false, cvcPanel, cvcCenterPanel);
            }
        });
        this.add(cvcPanel);
    }

    /*
    Function creates and returns a panel with the correct icons and text
     */
    private JPanel createGameModeSubPanel(BufferedImage leftIcon, String text, BufferedImage rightIcon) {
        JPanel mainPanel = new JPanel(new GridLayout(1,3)); // the panel has 3 columns
        mainPanel.setBackground(VisualResources.getInstance().customBlue);
        mainPanel.setBorder(new LineBorder(Color.BLUE)); // default border color

        // left picture as an ImageIcon
        JLabel leftPictureLabel = new JLabel(new ImageIcon(leftIcon));
        mainPanel.add(leftPictureLabel);

        GridBagConstraints gbc = new GridBagConstraints(); // GBC to center vertically
        gbc.anchor = GridBagConstraints.CENTER;

        // the text in the middle of the icons
        JPanel centerTextPanel = new JPanel(new GridBagLayout());
        centerTextPanel.setBackground(VisualResources.getInstance().customBlue);
        mainPanel.add(centerTextPanel);
        JLabel centerTextLabel = new JLabel(text);
        centerTextLabel.setFont(VisualResources.getInstance().customFontItal.deriveFont(30f)); // written in italic
        centerTextPanel.add(centerTextLabel, gbc); // add and center with GBC

        // switch gets the right center panel. Java doesn't have out parameters :`(
        switch (text){
            case "Joueur vs Joueur" :
                pvpCenterPanel = centerTextPanel;
                break;
            case "Joueur vs IA" :
                pvcCenterPanel = centerTextPanel;
                break;
            case "IA vs IA" :
                cvcCenterPanel = centerTextPanel;
                break;
        }

        // right picture as an ImageIcon
        JLabel rightPictureLabel = new JLabel(new ImageIcon(rightIcon));
        mainPanel.add(rightPictureLabel);

        return mainPanel;
    }

    /*
    Sets the background of the given panels to the right color depending on whether the mouse is hovering
     */
    private void setHover(boolean isHover, JPanel mainPanel, JPanel centerPanel) {
        if (isHover) {
            mainPanel.setBackground(VisualResources.getInstance().customLightBlue);
            centerPanel.setBackground(VisualResources.getInstance().customLightBlue);
        } else {
            mainPanel.setBackground(VisualResources.getInstance().customBlue);
            centerPanel.setBackground(VisualResources.getInstance().customBlue);
        }
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

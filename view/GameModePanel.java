package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameModePanel extends JPanel {

    public GameModePanel() {
        this.setLayout(new GridLayout(4,1));

        // Top title panel and label
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(VisualResources.getInstance().customBlue);
        this.add(titlePanel);
        JLabel titleLabel = new JLabel("Choisi ton mode de jeu");
        titleLabel.setFont(VisualResources.getInstance().customFont.deriveFont(60f));
        titlePanel.add(titleLabel);

        createClickablePanels();
    }

    /*
    Code segmentation script. Creates the clickable panels and sets their mouse adapters
     */
    private void createClickablePanels() {
        JPanel pvpPanel = createGameModeSubPanel(VisualResources.getInstance().userIconImage, "Joueur vs Joueur", VisualResources.getInstance().userIconImage);
        pvpPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pvpPanel.setBorder(new LineBorder(Color.RED));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pvpPanel.setBorder(new LineBorder(Color.BLUE));
            }
        });
        this.add(pvpPanel);

        JPanel pvcPanel = createGameModeSubPanel(VisualResources.getInstance().userIconImage, "Joueur vs IA", VisualResources.getInstance().computerIconImage);
        pvcPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pvcPanel.setBorder(new LineBorder(Color.RED));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pvcPanel.setBorder(new LineBorder(Color.BLUE));
            }
        });
        this.add(pvcPanel);

        JPanel cvcPanel = createGameModeSubPanel(VisualResources.getInstance().computerIconImage, "IA vs IA", VisualResources.getInstance().computerIconImage);
        cvcPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cvcPanel.setBorder(new LineBorder(Color.RED));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cvcPanel.setBorder(new LineBorder(Color.BLUE));
            }
        });
        this.add(cvcPanel);
    }

    /*
    Function creates and returns a panel with the right icons and text
     */
    private JPanel createGameModeSubPanel(BufferedImage picture1, String text, BufferedImage picture2) {
        JPanel mainPanel = new JPanel(new GridLayout(1,3));
        mainPanel.setBackground(VisualResources.getInstance().customBlue);
        mainPanel.setBorder(new LineBorder(Color.BLUE));

        JLabel leftPictureLabel = new JLabel(new ImageIcon(picture1));
        mainPanel.add(leftPictureLabel);

        JPanel centerTextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerTextPanel.setBackground(VisualResources.getInstance().customBlue);
        mainPanel.add(centerTextPanel);
        JLabel centerTextLabel = new JLabel(text);
        centerTextLabel.setFont(VisualResources.getInstance().customFont.deriveFont(30f));
        centerTextPanel.add(centerTextLabel);

        JLabel rightPictureLabel = new JLabel(new ImageIcon(picture2));
        mainPanel.add(rightPictureLabel);

        return mainPanel;
    }
}

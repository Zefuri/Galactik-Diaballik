package view;

import javax.swing.*;
import java.awt.*;

public class GameModePanel extends JPanel {

    public GameModePanel() {
        this.setLayout(new GridLayout(4,1));

        // Top title panel and label
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.add(titlePanel);
        JLabel titleLabel = new JLabel("Choisi ton mode de jeu");
        titleLabel.setFont(VisualRessources.getInstance().customFont.deriveFont(40f));
        titlePanel.add(titleLabel);

        JPanel pvpPanel = createGameModeSubPanel("player", "Joueur contre Joueur", "player");
        this.add(pvpPanel);

        JPanel pvcPanel = createGameModeSubPanel("player", "Joueur contre IA", "computer");
        this.add(pvcPanel);

        JPanel cvcPanel = createGameModeSubPanel("computer", "IA contre IA", "computer");
        this.add(cvcPanel);
    }

    private JPanel createGameModeSubPanel(String picture1, String text, String picture2) {
        JPanel mainPanel = new JPanel(new GridLayout(1,3));

        JLabel leftPictureLabel = new JLabel(picture1);
        mainPanel.add(leftPictureLabel);

        JPanel centerTextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(centerTextPanel);
        JLabel centerTextLabel = new JLabel(text);
        centerTextPanel.add(centerTextLabel);

        JLabel rightPictureLabel = new JLabel(picture2);
        mainPanel.add(rightPictureLabel);

        return mainPanel;
    }
}

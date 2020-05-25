package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EVETimer implements ActionListener {
    private Technoid technoid;

    public EVETimer(Technoid technoid) {
        this.technoid = technoid;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        technoid.playAI();
    }
}

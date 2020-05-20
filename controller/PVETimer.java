package controller;

import controller.listeners.MouseAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PVETimer implements ActionListener {

    private MouseAction mouseAction;

    public PVETimer(MouseAction mouseAction) {
        this.mouseAction = mouseAction;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        mouseAction.playAI();
    }
}

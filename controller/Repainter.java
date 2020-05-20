package controller;

import controller.listeners.MouseAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Repainter implements ActionListener {

    private MouseAction mouseAction;

    public Repainter(MouseAction mouseAction) {
        this.mouseAction = mouseAction;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        mouseAction.playAI();
    }
}

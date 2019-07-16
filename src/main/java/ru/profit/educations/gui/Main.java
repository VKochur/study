package ru.profit.educations.gui;

import ru.profit.educations.gui.controller.Controller;
import ru.profit.educations.gui.view.View;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {

    static View view;

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                view = new View();
            }
        });
        Model model = new Model();
        Controller controller = new Controller();
        controller.setView(view);
        controller.setModel(model);
    }
}

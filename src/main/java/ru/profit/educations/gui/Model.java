package ru.profit.educations.gui;

import ru.profit.educations.gui.controller.Controller;

public class Model {

    private Controller controller;

    public Model() {
    }


    private void importOrders(String pathToXls) {

        controller.setInterruptedFlag(false);
        controller.sendMessageToView("Количество Заказов для анализа: 100");
        for (int i = 0; i < 100 && !controller.isInterruptedFlag(); i++) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            controller.sendStatusToView("делается " + i);

        }

        controller.sendStatusToView("");
        controller.setRunning(false);

    }



    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void startImportOrders(String path) {
        importOrders(null);
    }
}

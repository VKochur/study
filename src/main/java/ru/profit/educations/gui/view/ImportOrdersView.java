package ru.profit.educations.gui.view;

import ru.profit.educations.gui.controller.Controller;

public interface ImportOrdersView {

    void sendPathFileXlsForRun();
    void showMessage(String message);
    void showError(String message);
    void showStatus(String status);
    void setStatusAboutRunning(boolean flag);

    Controller getController();
    void setController(Controller controller);
}

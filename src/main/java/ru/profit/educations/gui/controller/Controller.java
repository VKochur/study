package ru.profit.educations.gui.controller;

import ru.profit.educations.gui.Model;
import ru.profit.educations.gui.view.ImportOrdersView;

public class Controller {

    volatile private boolean isRunning;
    volatile private boolean interruptedFlag;

    private ImportOrdersView view;
    private Model model;

    public ImportOrdersView getView() {
        return view;
    }

    public void setView(ImportOrdersView view) {
        this.view = view;
        view.setController(this);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
        model.setController(this);
    }

    public void startImportOrdersFromFileXls(String path) {
        if (!isRunning) {
            setRunning(true);
            new Thread(() -> model.startImportOrders(path)).start();
        }
    }

    public void sendMessageToView(String message){
        view.showMessage(message);
    }

    public void sendError(String message){
        view.showError(message);
    }

    public void sendStatusToView(String message){
        view.showStatus(message);
    }

    public synchronized void setRunning(boolean running) {
        isRunning = running;
        view.setStatusAboutRunning(running);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isInterruptedFlag() {
        return interruptedFlag;
    }

    public synchronized void setInterruptedFlag(boolean interruptedFlag) {
        this.interruptedFlag = interruptedFlag;
    }
}

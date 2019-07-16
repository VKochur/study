package ru.profit.educations.gui.view;

import ru.profit.educations.gui.controller.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;

public class View extends JFrame implements ImportOrdersView {

    private Controller controller;

    private JLabel label;
    private JTextField textPath;
    private JFileChooser fileChooser;
    private JButton buttonRun;
    private JTextPane textPane;
    private JPanel capPanel;
    private JPanel bottomPanel;
    private JButton runButton;
    private JButton cancelButton;
    private JLabel statusLabel;

    public View() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        buildGui();
        addListeners();
        setMinimumSize(new Dimension(600, 400));
        setVisible(true);
        setStatusAboutRunning(false);
    }

    @Override
    public void dispose() {
        if (controller != null) {
            if (controller.isRunning()) {
                controller.setInterruptedFlag(true);
            }
        }
        super.dispose();
    }

    private void addListeners() {
        JFrame jFrame = this;
        buttonRun.addActionListener(e -> {
            if (fileChooser.showDialog(jFrame, WindowTitles.FILE_CHOOSER_TITLE) == JFileChooser.APPROVE_OPTION) {
                textPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        runButton.addActionListener(e -> sendPathFileXlsForRun());

        cancelButton.addActionListener(e -> {
            if (controller.isRunning()) {
                controller.setInterruptedFlag(true);
            } else {
                jFrame.dispose();
            }
        });
    }

    private void buildGui() {

        setTitle(WindowTitles.TITLE);

        capPanel = new JPanel();
        capPanel.setLayout(new BorderLayout());
        add(capPanel, BorderLayout.NORTH);
        label = new JLabel(WindowTitles.LABEL_PATH_FILE);
        textPath = new JTextField();
        buttonRun = new JButton();
        buttonRun.setText(WindowTitles.BUTTON_CHOOSE_FILE);
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(".") + 1).equals("xls");
                }
            }

            @Override
            public String getDescription() {
                return "*.xls";
            }
        });
        capPanel.add(label, BorderLayout.WEST);
        capPanel.add(textPath, BorderLayout.CENTER);
        capPanel.add(buttonRun, BorderLayout.EAST);

        textPane = new JTextPane();
        DefaultCaret caret = (DefaultCaret) textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        runButton = new JButton();
        runButton.setText(WindowTitles.BUTTON_OK);
        cancelButton = new JButton();
        cancelButton.setText(WindowTitles.BUTTON_CANCEL);
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtons.add(runButton);
        panelButtons.add(cancelButton);
        bottomPanel.add(panelButtons);
        statusLabel = new JLabel();
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
    }

    @Override
    public void showStatus(String status) {
        statusLabel.setText(status);
    }

    @Override
    public void sendPathFileXlsForRun() {
        getController().startImportOrdersFromFileXls(textPath.getText());
    }

    @Override
    public void showMessage(String message) {
        addMessage(message + "\n", WindowTitles.MESSAGE_TEXT_COLOR);
    }

    @Override
    public void showError(String message) {
        addMessage(message + "\n", WindowTitles.ERROR_TEXT_COLOR);
    }

    private void addMessage(String message, Color textColor) {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyledDocument document = textPane.getStyledDocument();
        try {
            StyleConstants.setForeground(attributeSet, textColor);
            document.insertString(document.getLength(), message, attributeSet);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void setStatusAboutRunning(boolean flag) {
        runButton.setEnabled(!flag);
        if (flag) {
            cancelButton.setText(WindowTitles.BUTTON_INTERRUPTED);
        } else {
            cancelButton.setText(WindowTitles.BUTTON_CANCEL);
        }
    }
}

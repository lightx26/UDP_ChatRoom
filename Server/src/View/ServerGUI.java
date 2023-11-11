package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import App.Server;

public class ServerGUI extends JFrame {
    // private DataTransfer dataTransfer;
    private Server server;

    private JPanel operationPanel;
    private JTextField portField;
    private JButton startButton;
    private JButton stopButton;
    private JTextArea logArea;
    private JPanel logPanel;

    public ServerGUI(Server server) {
        this.server = server;
        CreateView();
        stopButton.setEnabled(false);
        // Show();
        AttachEventHandler();
    }

    // public void Show() {
    // this.setVisible(true);
    // }

    private void CreateView() {
        this.setTitle("Server GUI");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        this.setMinimumSize(new Dimension(400, 400));
        this.setPreferredSize(new Dimension(400, 400));

        operationPanel = new JPanel();
        portField = new JTextField();
        portField.setPreferredSize(new Dimension(100, 28));
        startButton = new JButton();
        startButton.setText("Start server");
        stopButton = new JButton();
        stopButton.setText("Stop server");

        operationPanel.add(portField);
        operationPanel.add(startButton);
        operationPanel.add(stopButton);

        logPanel = new JPanel();
        logPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        logPanel.setMinimumSize(new Dimension(400, 300));
        logPanel.setPreferredSize(new Dimension(400, 300));

        logArea = new JTextArea(16, 32);
        logArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        logArea.setEditable(false);
        logArea.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(logArea);
        logPanel.add(sp);

        this.add(operationPanel);
        this.add(logPanel);

        this.setVisible(true);
    }

    public void settingServerOn() {
        portField.setEditable(false);
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    public void settingServerOff() {
        portField.setEditable(true);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void AttachEventHandler() {

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.setPortNumber(Integer.parseInt(portField.getText()));
                server.start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    server.deliverChat("3 Server is stopped.");
                    server.stopServer();
                } catch (Exception e1) {
                    server.log("Cannot stop server: " + e1.getMessage() + "\n");
                }

                server.log("Server is stopped");
                portField.setEditable(true);
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });
    }

    public void log(String str) {
        logArea.append(str + "\n");
    }
}

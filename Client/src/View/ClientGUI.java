package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import App.Client;

public class ClientGUI extends JFrame {

    // 0 for leave
    // 1 for enter
    // 2 for chat

    private Client client;

    // Components
    private JPanel setupPanel;
    private JPanel operationPanel;
    private JTextField usernameField;
    private JTextField portField;
    private JButton connectButton;
    private JButton leaveButton;
    private JTextField textField;
    private JButton sendButton;
    private JButton cancelButton;
    private JTextArea resultArea;
    private JPanel textPanel;
    private JPanel buttonsPanel;
    private JPanel resultPanel;

    private boolean _isConnected = false;

    public ClientGUI(Client client) {
        this.client = client;
        CreateView();
        // Show();
        AttachEventHandler();
        ReceiveChat();
    }

    // public void Show() {
    // this.setVisible(true);
    // }

    private void CreateView() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Client GUI");

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        this.setMinimumSize(new Dimension(400, 540));
        this.setPreferredSize(new Dimension(400, 540));

        setupPanel = new JPanel();
        setupPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setupPanel.setMinimumSize(new Dimension(400, 20));
        setupPanel.setPreferredSize(new Dimension(400, 40));

        JLabel port = new JLabel("Port: ");
        portField = new JTextField();
        portField.setPreferredSize(new Dimension(80, 28));
        JLabel username = new JLabel("Username: ");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(120, 28));

        setupPanel.add(port);
        setupPanel.add(portField);
        setupPanel.add(username);
        setupPanel.add(usernameField);

        operationPanel = new JPanel();
        operationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        operationPanel.setMinimumSize(new Dimension(400, 20));
        operationPanel.setPreferredSize(new Dimension(400, 40));

        connectButton = new JButton();
        connectButton.setText("Connect");
        leaveButton = new JButton();
        leaveButton.setText("Leave");

        operationPanel.add(connectButton);
        operationPanel.add(leaveButton);

        resultPanel = new JPanel();
        resultPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        resultPanel.setMinimumSize(new Dimension(400, 300));
        resultPanel.setPreferredSize(new Dimension(400, 300));

        resultArea = new JTextArea(16, 32);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        resultArea.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(resultArea);
        resultPanel.add(sp);

        textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        textPanel.setPreferredSize(new Dimension(400, 40));

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(320, 30));
        textPanel.add(textField);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonsPanel.setPreferredSize(new Dimension(180, 40));
        sendButton = new JButton();
        sendButton.setText("Send");
        buttonsPanel.add(sendButton);

        cancelButton = new JButton();
        cancelButton.setText("Clear chat");
        buttonsPanel.add(cancelButton);

        this.add(setupPanel);
        this.add(operationPanel);
        this.add(resultPanel);
        this.add(textPanel);
        this.add(buttonsPanel);

        settingNotConnected();

        this.setVisible(true);
    }

    void settingNotConnected() {
        // usernameField.setEditable(true);
        portField.setEditable(true);
        connectButton.setEnabled(true);
        leaveButton.setEnabled(false);
        textField.setEditable(false);
        sendButton.setEnabled(false);
    }

    void settingConnected() {
        portField.setEditable(false);
        usernameField.setEditable(false);
        connectButton.setEnabled(false);
        leaveButton.setEnabled(true);
        textField.setEditable(true);
        sendButton.setEnabled(true);
    }

    private void AttachEventHandler() {
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (portField.getText().equals("")) {
                    resultArea.append("Please enter port number of server.\n");
                } else {
                    if (usernameField.getText().equals("")) {
                        resultArea.append("Please enter your username.\n");
                    } else {
                        client.setServerPort(Integer.parseInt(portField.getText()));
                        client.setUsername(usernameField.getText());

                        client.startChat();

                        client.send("1 " + client.getUsername());

                        _isConnected = true;

                        settingConnected();
                    }
                }
            }
        });

        usernameField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connectButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.send("0 " + client.getUsername());
                settingNotConnected();
                // try {
                // client.socket.shutdownOutput();
                // } catch (IOException e1) {
                // System.err.println("Cannot shutdown output.");
                // e1.printStackTrace();
                // }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client.getUsername() != null) {
                    String text = textField.getText();
                    if (text.length() > 0) {
                        try {
                            client.send("2 " + client.getUsername() + ": " + text);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    textField.setText("");
                } else {
                    resultArea.append("Please enter your username.\n");
                }

            }
        });

        textField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultArea.setText("");
            }
        });
    }

    void ReceiveChat() {
        while (true) {
            if (_isConnected) {
                String res = client.receive();
                String code = res.substring(0, 1);
                String payload = res.substring(2);

                if (code.equals("2")) {
                    resultArea.append(payload + "\n");
                }

                else if (code.equals("1")) {
                    if (payload.equals(client.getUsername() + " enter the conversation.")) {
                        resultArea.append("Welcome " + client.getUsername() + "\nStart chat now.\n");
                    } else {
                        resultArea.append(payload + "\n");
                    }
                }

            } else {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
    }
}

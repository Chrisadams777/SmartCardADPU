package smartcard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GUIManager {
    private JFrame frame;
    private JTextArea apduDisplay;
    private JTextField apduInput;
    private JButton sendButton;
    private JButton manipulateButton;
    private JButton saveLogButton;
    private JButton runScriptButton;
    private JButton batchProcessButton;
    private APDUManager apduManager;
    private ScriptEngine scriptEngine;

    public GUIManager(APDUManager apduManager, ScriptEngine scriptEngine) {
        this.apduManager = apduManager;
        this.scriptEngine = scriptEngine;
        setupGUI();
    }

    private void setupGUI() {
        frame = new JFrame("SmartCard APDU Interface");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        apduDisplay = new JTextArea();
        apduDisplay.setEditable(false);

        apduInput = new JTextField(40);

        sendButton = new JButton("Send APDU");
        manipulateButton = new JButton("Manipulate and Send APDU");
        saveLogButton = new JButton("Save Log");
        runScriptButton = new JButton("Run Script");
        batchProcessButton = new JButton("Batch Process");

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                byte[] command = hexStringToByteArray(apduInput.getText());
                apduManager.sendAPDUCommand(null, command); // Replace null with actual CardChannel
            }
        });

        manipulateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                byte[] originalCommand = hexStringToByteArray(apduInput.getText());
                byte[] manipulatedCommand = manipulateCommand(originalCommand);
                apduManager.manipulateAndSendAPDU(null, originalCommand, manipulatedCommand); // Replace null with actual CardChannel
            }
        });

        saveLogButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveLogToFile();
            }
        });

        runScriptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String scriptContent = JOptionPane.showInputDialog(frame, "Enter your script:");
                if (scriptContent != null && !scriptContent.isEmpty()) {
                    scriptEngine.executeScript(scriptContent, apduManager, null); // Replace null with actual CardChannel
                }
            }
        });

        batchProcessButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                batchProcessCommands();
            }
        });

        JPanel panel = new JPanel();
        panel.add(apduInput);
        panel.add(sendButton);
        panel.add(manipulateButton);
        panel.add(saveLogButton);
        panel.add(runScriptButton);
        panel.add(batchProcessButton);

        frame.getContentPane().add(BorderLayout.CENTER, new JScrollPane(apduDisplay));
        frame.getContentPane().add(BorderLayout.SOUTH, panel);

        frame.setVisible(true);
    }

    public static void displayAPDUCommunication(String message) {
        // Update the APDU display area
        // Implementation to append to apduDisplay JTextArea
    }

    public static void displayError(String error) {
        // Display error messages in the GUI
        // Implementation to show errors in apduDisplay JTextArea
    }

    private void saveLogToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("apdu_log.txt"))) {
            writer.write(apduDisplay.getText());
            JOptionPane.showMessageDialog(frame, "Log saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to save log: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void batchProcessCommands() {
        String batchCommands = JOptionPane.showInputDialog(frame, "Enter batch commands, one per line:");
        if (batchCommands != null && !batchCommands.isEmpty()) {
            scriptEngine.executeScript(batchCommands, apduManager, null); // Replace null with actual CardChannel
        }
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private byte[] manipulateCommand(byte[] command) {
        // Simple example: invert all bytes (bitwise NOT)
        byte[] manipulated = new byte[command.length];
        for (int i = 0; i < command.length; i++) {
            manipulated[i] = (byte) ~command[i];
        }
        return manipulated;
    }
}

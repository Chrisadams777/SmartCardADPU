package smartcard;

import javax.smartcardio.*;

public class DeviceManager {
    private TerminalFactory terminalFactory;
    private CardTerminal terminal;

    public DeviceManager() {
        try {
            terminalFactory = TerminalFactory.getDefault();
            CardTerminals terminals = terminalFactory.terminals();
            terminal = terminals.list().get(0);  // Default to first terminal
            System.out.println("Device initialized: " + terminal.getName());
        } catch (Exception e) {
            System.err.println("Failed to initialize device: " + e.getMessage());
        }
    }

    public CardTerminal getTerminal() {
        return terminal;
    }

    public void selectDevice(String deviceName) {
        try {
            CardTerminals terminals = terminalFactory.terminals();
            for (CardTerminal term : terminals.list()) {
                if (term.getName().equals(deviceName)) {
                    terminal = term;
                    System.out.println("Device selected: " + terminal.getName());
                    return;
                }
            }
            System.out.println("Device not found: " + deviceName);
        } catch (Exception e) {
            System.err.println("Error selecting device: " + e.getMessage());
        }
    }
}

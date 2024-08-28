package smartcard;

import javax.smartcardio.*;

public class Main {
    public static void main(String[] args) {
        DeviceManager deviceManager = new DeviceManager();
        CardTerminal terminal = deviceManager.getTerminal();

        if (terminal == null) {
            System.out.println("No terminal available. Exiting.");
            return;
        }

        CardReader cardReader = new CardReader(terminal);
        APDUManager apduManager = new APDUManager();
        ScriptEngine scriptEngine = new ScriptEngine();
        GUIManager guiManager = new GUIManager(apduManager, scriptEngine);

        // Example usage
        cardReader.readCardData();
        byte[] dataToWrite = new byte[]{(byte) 0x00, (byte) 0xD6, (byte) 0x00, (byte) 0x00, (byte) 0x10};
        cardReader.writeCardData(dataToWrite);

        try {
            Card card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();
            byte[] apduCommand = new byte[]{(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00, (byte) 0x0E};
            apduManager.sendAPDUCommand(channel, apduCommand);
            card.disconnect(false);
        } catch (Exception e) {
            System.err.println("Error in APDU command process: " + e.getMessage());
        }

        POSTerminalEmulator posEmulator = new POSTerminalEmulator();
        posEmulator.simulateTransaction("Credit", 123.45);
    }
}

package smartcard;

import javax.smartcardio.*;

public class CardReader {
    private CardTerminal terminal;

    public CardReader(CardTerminal terminal) {
        this.terminal = terminal;
    }

    public void readCardData() {
        try {
            Card card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();
            CommandAPDU command = new CommandAPDU(new byte[]{(byte) 0x00, (byte) 0xB0, (byte) 0x00, (byte) 0x00, (byte) 0x10});
            ResponseAPDU response = channel.transmit(command);
            GUIManager.displayAPDUCommunication("Sent: " + bytesToHex(command.getBytes()) + " Received: " + bytesToHex(response.getBytes()));
            card.disconnect(false);
        } catch (Exception e) {
            System.err.println("Error reading card: " + e.getMessage());
        }
    }

    public void writeCardData(byte[] data) {
        try {
            Card card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();
            CommandAPDU command = new CommandAPDU(data);
            ResponseAPDU response = channel.transmit(command);
            GUIManager.displayAPDUCommunication("Sent: " + bytesToHex(command.getBytes()) + " Received: " + bytesToHex(response.getBytes()));
            card.disconnect(false);
        } catch (Exception e) {
            System.err.println("Error writing to card: " + e.getMessage());
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}

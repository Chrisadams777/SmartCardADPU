package smartcard;

import javax.smartcardio.*;

public class APDUManager {

    public ResponseAPDU sendAPDUCommand(CardChannel channel, byte[] command) {
        try {
            CommandAPDU apdu = new CommandAPDU(command);
            ResponseAPDU response = channel.transmit(apdu);
            GUIManager.displayAPDUCommunication("Sent: " + bytesToHex(apdu.getBytes()) + " | Received: " + bytesToHex(response.getBytes()));
            return response;
        } catch (Exception e) {
            logError("Failed to send APDU command", e);
            return null;
        }
    }

    public ResponseAPDU manipulateAndSendAPDU(CardChannel channel, byte[] command, byte[] manipulatedData) {
        try {
            CommandAPDU apdu = new CommandAPDU(manipulatedData);
            ResponseAPDU response = channel.transmit(apdu);
            GUIManager.displayAPDUCommunication("Manipulated Sent: " + bytesToHex(apdu.getBytes()) + " | Received: " + bytesToHex(response.getBytes()));
            return response;
        } catch (Exception e) {
            logError("Failed to send manipulated APDU command", e);
            return null;
        }
    }

    private void logError(String message, Exception e) {
        String errorMessage = message + ": " + e.getMessage();
        System.err.println(errorMessage);
        GUIManager.displayError(errorMessage);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}

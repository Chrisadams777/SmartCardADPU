package smartcard;

import javax.smartcardio.*;
import java.util.ArrayList;
import java.util.List;

public class ScriptEngine {

    public void executeScript(String script, APDUManager apduManager, CardChannel channel) {
        List<byte[]> commands = parseScript(script);
        for (byte[] command : commands) {
            ResponseAPDU response = apduManager.sendAPDUCommand(channel, command);
            if (response == null) {
                GUIManager.displayError("Script execution failed at command: " + bytesToHex(command));
                break;
            }
        }
    }

    private List<byte[]> parseScript(String script) {
        List<byte[]> commands = new ArrayList<>();
        String[] lines = script.split("\\r?\\n");
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue; // Ignore empty lines and comments
            }
            try {
                commands.add(hexStringToByteArray(line.trim()));
            } catch (Exception e) {
                GUIManager.displayError("Invalid command format: " + line);
            }
        }
        return commands;
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

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}

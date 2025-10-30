import java.util.Scanner;

public class ChecksumFraming {
    
    
    static int calculateChecksum(String data) {
        int sum = 0;
        for (int i = 0; i < data.length(); i++) {
            sum += (int) data.charAt(i);
        }
        return sum % 256;
    }

    
    static byte[] sendFrame(String data) {
        int checksum = calculateChecksum(data);
        byte start = 0x7E;  // Start of Frame
        byte end = 0x7F;    // End of Frame
        byte[] frame = new byte[data.length() + 3];

        frame[0] = start;
        for (int i = 0; i < data.length(); i++) {
            frame[i + 1] = (byte) data.charAt(i);
        }
        frame[data.length() + 1] = (byte) checksum;
        frame[data.length() + 2] = end;

        return frame;
    }

    
    static boolean receiveFrame(byte[] frame) {
        if (frame[0] != 0x7E || frame[frame.length - 1] != 0x7F) {
            System.out.println("Frame Error: Invalid start/end bytes!");
            return false;
        }

        StringBuilder data = new StringBuilder();
        for (int i = 1; i < frame.length - 2; i++) {
            data.append((char) frame[i]);
        }

        int receivedChecksum = frame[frame.length - 2] & 0xFF;
        int calculatedChecksum = calculateChecksum(data.toString());

        if (receivedChecksum != calculatedChecksum) {
            System.out.println("Checksum Error Detected!");
            return false;
        }

        System.out.println("Received Data: " + data);
        System.out.println("Status: No error detected.");
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter data to send: ");
        String data = sc.nextLine();

        byte[] frame = sendFrame(data);

        System.out.print("Transmitted Frame (in HEX): ");
        for (byte b : frame) {
            System.out.printf("%02X ", b);
        }
        System.out.println();

        

        receiveFrame(frame);
        sc.close();
    }
}

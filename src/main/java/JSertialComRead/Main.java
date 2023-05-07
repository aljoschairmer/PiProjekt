package JSertialComRead;

import com.fazecast.jSerialComm.SerialPort;

public class Main {
    public static void main(String[] args) {
        SerialPort comPort = SerialPort.getCommPorts()[1];
        comPort.openPort();
        System.out.println("\n" + comPort.getSystemPortName());
        try {
            while (true)
            {
                while (comPort.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes.");
                String text = new String(readBuffer, "UTF-8");
                System.out.println("Received -> "+ text);
            }
        } catch (Exception e) { e.printStackTrace(); }
        comPort.closePort();
    }
}



package com.shyam.androidjssc;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String PORT_NAME = "/dev/ttyXR1";
    private static final int BAUD_RATE = 9600;
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    /**
     * The port.
     */
    private jssc.SerialPort serialPort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        Log.d(TAG, "running to here1....................");
        openSerialPort();
        Log.d(TAG, "running to here2....................");
        write();
        Log.d(TAG, "running to here.3...................");
    }

    @Override
    protected void onDestroy() {

        try {
            if (serialPort != null) {
                synchronized (this) {
                    serialPort.removeEventListener();
                    serialPort.closePort();
                    serialPort = null;
                    this.notify();
                }

                Log.i(TAG, "Serial port '" + PORT_NAME + "' closed.");
            }
        } catch (Exception e) {
            Log.w(TAG, "Error closing serial port: '" + PORT_NAME + "'", e);
        }

        super.onDestroy();
    }

    /**
     * Write a sample string to serial port.
     */
    private void write() {
        if (null != serialPort) {
            try {
//                serialPort.writeBytes("Hello".getBytes());
//                byte[] buffer = new byte[] {(byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x04, (byte) 0x1A};
                byte[] buffer = new byte[] {(byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x05, (byte) 0xCB};
                boolean isOK = serialPort.writeBytes(buffer);
                Log.d(TAG, "isOK ===" + isOK);
                byte[] test2 = serialPort.readBytes(1);
//                String test2 = serialPort.readHexString(3);
//                System.out.println("test2 test2: " + bytesToHex(test2));
//                Log.e(TAG, "Exception while writing =====" + bytesToHex(test2));
            } catch (SerialPortException e) {
                Log.e(TAG, "Exception while writing", e);
            }
        }
    }

    /**
     * Open serial port.
     */
    private void openSerialPort() {

        serialPort = new jssc.SerialPort(PORT_NAME);

        try {
            serialPort.openPort();
            serialPort.setParams(BAUD_RATE, 8, 1, 0);
            serialPort.setFlowControlMode(jssc.SerialPort.FLOWCONTROL_XONXOFF_OUT);
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            serialPort.setEventsMask(mask);//Set mask
            serialPort.addEventListener(mSerialPortEventListener);

        } catch (SerialPortException e) {
            Log.e(TAG, "Unable to open port", e);
        }
    }
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    /**
     * Sample serial port listener.
     */
    private SerialPortEventListener mSerialPortEventListener = new SerialPortEventListener() {
        @Override
        public void serialEvent(final SerialPortEvent serialPortEvent) {
            Log.d("SerialPotListener=", "hihihihihihihi");
        }
    };
}

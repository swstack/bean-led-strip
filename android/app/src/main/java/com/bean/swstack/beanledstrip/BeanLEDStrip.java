package com.bean.swstack.beanledstrip;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;


/**************************************
 * Message Definitions
 *************************************/
enum Command {
    set_leds
}


/**************************************
 * Global State
 *************************************/
enum LEDState {
    ON,
    OFF,
}


public class BeanLEDStrip {

    private final byte START_FRAME = 0x77;  // Random start frame

    private static final String TAG = "BeanLEDStrip";
    private static final String BEAN_NAME = "bean_led_strip";

    protected final Context context;
    protected final TextView connectStatus;
    protected Bean ledStrip;
    protected LEDState ledState;

    public BeanLEDStrip(Context context, TextView connectStatus) {
        this.context = context;
        this.connectStatus = connectStatus;
        this.ledStrip = null;
        this.ledState = LEDState.OFF;
    }

    public Boolean isConnected() {
        return this.ledStrip.isConnected();
    }

    public void connect() {
        Log.d(TAG, "Starting connection attempt...");
        if (ledStrip == null) {
            Log.d(TAG, "Starting Bean discovery...");
            BeanManager.getInstance().startDiscovery(new BeanDiscoveryListener() {
                @Override
                public void onBeanDiscovered(Bean bean, int rssi) {
                    Log.d(TAG, "Bean found");

                    if (bean.getDevice().getName().equals(BEAN_NAME)) {
                        Log.d(TAG, "LED strip found!");
                        ledStrip = bean;

                        Log.d(TAG, "Starting to connect to bean named " + BEAN_NAME);
                        ledStrip.connect(context, beanListener);
                    }
                }

                @Override
                public void onDiscoveryComplete() {
                    Log.d(TAG, "Bean Discovery Complete");
                }
            });
        }
    }

    public void reset() {
        if (ledStrip != null) {
            this.ledStrip.disconnect();
            this.ledStrip = null;
            this.ledState = LEDState.OFF;
        }
        connectStatus.setText("Disconnected");
        connect();
    }

    private BeanListener beanListener = new BeanListener() {

        @Override
        public void onConnected() {
            Log.d(TAG, "Connected");
            connectStatus.setText("Connected");
            setLeds(0, 0, 0);
        }

        @Override
        public void onConnectionFailed() {
            Log.d(TAG, "Connection Failed");
        }

        @Override
        public void onDisconnected() {
            Log.d(TAG, "Disconnected");
        }

        @Override
        public void onSerialMessageReceived(byte[] bytes) {
            Log.d(TAG, "Serial Message Received");
        }

        @Override
        public void onScratchValueChanged(ScratchBank scratchBank, byte[] bytes) {
            Log.d(TAG, "Scratch Value Changed");
        }

        @Override
        public void onError(BeanError beanError) {
            Log.d(TAG, "Bean Error");
        }
    };

    private byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public void setLeds(int red, int green, int blue) {
        byte[] buffer = new byte[5];
        buffer[0] = START_FRAME;
        buffer[1] = (byte) Command.set_leds.ordinal();
        buffer[2] = (byte) red;
        buffer[3] = (byte) green;
        buffer[4] = (byte) blue;
        System.out.println(String.format("Setting RGB (%d, %d, %d)", red, green, blue));
        ledStrip.sendSerialMessage(buffer);
    }

    public void setLedsOld(int red, int green, int blue)  {

        // Header and command ID
        byte[] buffer = new byte[2];
        buffer[0] = START_FRAME;
        buffer[1] = (byte) Command.set_leds.ordinal();

        // Payload
        byte[] redBytes = intToByteArray(red);
        byte[] greenBytes = intToByteArray(green);
        byte[] blueBytes = intToByteArray(blue);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(buffer);
            outputStream.write(redBytes);
            outputStream.write(greenBytes);
            outputStream.write(blueBytes);
            byte[] allbytes = outputStream.toByteArray();
            ledStrip.sendSerialMessage(allbytes);
            System.out.println("Sending serial message of length: " + allbytes.length);
            byte [] tmp = Arrays.copyOfRange(allbytes, 2, 6);
            System.out.println("Original red: " + red);
            System.out.println(new BigInteger(tmp).intValue());
            System.out.println("----");
            byte x = (byte) 255;
            System.out.println(x & 0xFF);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FAIL: setLeds");
        }

    }

}

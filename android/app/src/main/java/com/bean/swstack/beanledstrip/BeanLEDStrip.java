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


/**************************************
 * Message Definitions
 *************************************/
enum Command {
    turn_off,
    set_red,
    set_green,
    set_blue,
}


/**************************************
 * Global State
 *************************************/
enum LEDState {
    ON,
    OFF,
}


public class BeanLEDStrip {

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
            sendCommand(Command.turn_off);
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

    private void sendCommand(Command command) {
        byte[] byteBuf = new byte[1];
        byteBuf[0] = (byte) command.ordinal();
        ledStrip.sendSerialMessage(byteBuf);
    }

}

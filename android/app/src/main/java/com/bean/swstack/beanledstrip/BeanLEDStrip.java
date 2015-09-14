package com.bean.swstack.beanledstrip;

import android.util.Log;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanManager;

public class BeanLEDStrip {

    private static final String TAG = "BeanLEDStrip";
    private static final String ledStripAddress = "";
    private Bean ledStrip;
    private Boolean powerState = false;  // default off

    public void connect() {
        BeanManager.getInstance().startDiscovery(beanListener);
    }

    public void syncState() {
        ledStrip.sendSerialMessage("1");
    }

    private BeanDiscoveryListener beanListener = new BeanDiscoveryListener() {
        @Override
        public void onBeanDiscovered(Bean bean, int rssi) {
            Log.d(TAG, "Bean found");
            if (bean.getDevice().getAddress().equals(ledStripAddress)) {
                Log.d(TAG, "LED strip found!");
                ledStrip = bean;
                syncState();
            }
        }

        @Override
        public void onDiscoveryComplete() {
            Log.d(TAG, "Bean Discovery Complete");
        }
    };

}

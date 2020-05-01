package com.windy.exception;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class WhatsappAccessibilityService extends AccessibilityService {
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i("WhatsappAccessibilityService", "Connected...");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {

            Log.i("WhatsappAccessibilityService", "Get into this function");
            Log.i("WhatsappAccessibilityService", "event = " + event.toString());

            if (getRootInActiveWindow() == null) {
                Log.i("WhatsappAccessibilityService", "getRootInActiveWindow = null");
                return;
            }

            AccessibilityNodeInfoCompat rootInActiveWindow = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());

            // Whatsapp Message EditText id
            List<AccessibilityNodeInfoCompat> messageNodeList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
            if (messageNodeList == null || messageNodeList.isEmpty()) {
                Log.i("WhatsappAccessibilityService", "messageNodeList = null");
                return;
            }

            // check if the whatsapp message EditText field is filled with text and ending with suffix
            AccessibilityNodeInfoCompat messageField = messageNodeList.get(0);
            if (messageField.getText() == null
                    || messageField.getText().length() == 0
                    || !messageField.getText().toString().endsWith(getApplicationContext().getString(R.string.whatsapp_suffix))) {
                // So your service doesn't process any message, but the ones ending your apps suffix
                Log.i("WhatsappAccessibilityService", "messageField = null");
                return;
            }

            // Whatsapp send button id
            List<AccessibilityNodeInfoCompat> sendMessageNodeInfoList = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
            if (sendMessageNodeInfoList == null || sendMessageNodeInfoList.isEmpty()) {
                Log.i("WhatsappAccessibilityService", "sendMessageNodeInfoList = null");
                return;
            }

            AccessibilityNodeInfoCompat sendMessageButton = sendMessageNodeInfoList.get(0);
            if (!sendMessageButton.isVisibleToUser()) {
                Log.i("WhatsappAccessibilityService", "sendMessageButton invisible");
                return;
            }

            // Now fire a click on the send button
            sendMessageButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);

            // Now go back to your app by clicking on the Android back button twice:
            // First one to leave the conversation screen
            // Second one to leave whatsapp
            Thread.sleep(500);
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(500);
            performGlobalAction (GLOBAL_ACTION_BACK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {
        Log.i("WhatsappAccessibilityService", "onInterrupt...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("WhatsappAccessibilityService", "onDestroy...");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("WhatsappAccessibilityService", "onUnbind...");
        return super.onUnbind(intent);
    }
}

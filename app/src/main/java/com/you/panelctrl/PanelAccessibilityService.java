package com.you.panelctrl;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;
import org.json.JSONObject;

public class PanelAccessibilityService extends AccessibilityService {
    static PanelAccessibilityService instance;

    @Override
    public void onServiceConnected() { instance = this; }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {}

    @Override
    public void onInterrupt() {}

    static void exec(String json) {
        if (instance == null) return;
        try {
            JSONObject o = new JSONObject(json);
            String action = o.optString("action", "none");
            String value  = o.optString("value", "");

            switch (action) {
                case "back": instance.performGlobalAction(GLOBAL_ACTION_BACK); break;
                case "home": instance.performGlobalAction(GLOBAL_ACTION_HOME); break;
                case "recents": instance.performGlobalAction(GLOBAL_ACTION_RECENTS); break;
                case "tap": tap(value); break;
            }
        } catch (Exception ignored) {}
    }

    // value = "x,y"
    static void tap(String v) {
        try {
            String[] p = v.split(",");
            float x = Float.parseFloat(p[0].trim());
            float y = Float.parseFloat(p[1].trim());

            Path path = new Path();
            path.moveTo(x, y);
            GestureDescription.StrokeDescription stroke =
                    new GestureDescription.StrokeDescription(path, 0, 50);
            GestureDescription gesture =
                    new GestureDescription.Builder().addStroke(stroke).build();
            instance.dispatchGesture(gesture, null, null);
        } catch (Exception ignored) {}
    }
}

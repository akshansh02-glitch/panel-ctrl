package com.you.panelctrl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.view.*;
import android.widget.FrameLayout;

public class OverlayService {
    private static View overlay;
    private static WindowManager wm;

    public static void show(Context c) {
        if (overlay != null) return;
        if (!Settings.canDrawOverlays(c)) return;
        wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);

        FrameLayout v = new FrameLayout(c);
        v.setBackgroundColor(0x00000000); // fully transparent

        int type = Build.VERSION.SDK_INT >= 26
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        wm.addView(v, lp);
        overlay = v;
    }

    public static void hide(Context c) {
        if (overlay == null || wm == null) return;
        try { wm.removeView(overlay); } catch (Exception ignored) {}
        overlay = null;
    }
}

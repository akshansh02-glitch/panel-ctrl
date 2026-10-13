package com.you.panelctrl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(40,40,40,40);

        TextView tv = new TextView(this);
        tv.setText("PanelCtrl\n\n1) Grant overlay\n2) Enable accessibility\n3) Reboot\n\nESP32 AP: 192.168.4.1");
        l.addView(tv);

        Button overlay = new Button(this);
        overlay.setText("Grant overlay permission");
        overlay.setOnClickListener(v -> {
            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(i);
        });
        l.addView(overlay);

        Button acc = new Button(this);
        acc.setText("Open Accessibility settings");
        acc.setOnClickListener(v ->
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        l.addView(acc);

        Button start = new Button(this);
        start.setText("Start service now");
        start.setOnClickListener(v -> {
            Intent i = new Intent(this, HeartbeatService.class);
            if (Build.VERSION.SDK_INT >= 26) startForegroundService(i);
            else startService(i);
        });
        l.addView(start);

        setContentView(l);
    }
}

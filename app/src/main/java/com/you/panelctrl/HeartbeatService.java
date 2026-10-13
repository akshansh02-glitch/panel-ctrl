package com.you.panelctrl;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import java.io.*;
import java.net.*;

public class HeartbeatService extends Service {
    static final String ESP = "http://192.168.4.1";
    Thread worker;
    volatile boolean running = true;
    int misses = 0;
    int lastCmd = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel ch = new NotificationChannel("pc", "PanelCtrl",
                NotificationManager.IMPORTANCE_LOW);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .createNotificationChannel(ch);
        Notification n = new Notification.Builder(this, "pc")
                .setContentTitle("PanelCtrl running")
                .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
                .build();
        startForeground(1, n);
    }

    @Override
    public int onStartCommand(Intent i, int f, int s) {
        if (worker == null) {
            worker = new Thread(this::loop);
            worker.start();
        }
        return START_STICKY;
    }

    void loop() {
        while (running) {
            try {
                String cmd = httpGet(ESP + "/cmd");
                misses = 0;
                OverlayService.show(this);           // touch disabled
                if (cmd != null) {
                    int h = cmd.hashCode();
                    if (h != lastCmd) {
                        lastCmd = h;
                        PanelAccessibilityService.exec(cmd);
                    }
                }
            } catch (Exception e) {
                misses++;
                if (misses >= 3) OverlayService.hide(this);   // touch restored
            }
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        }
    }

    String httpGet(String url) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setConnectTimeout(1500);
        c.setReadTimeout(1500);
        BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) sb.append(line);
        r.close();
        return sb.toString();
    }

    @Override public IBinder onBind(Intent i) { return null; }
    @Override public void onDestroy() { running = false; super.onDestroy(); }
}

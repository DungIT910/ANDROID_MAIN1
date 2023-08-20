package com.example.btl_adr;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class Music extends Service {
    MediaPlayer mediaPlayer;
    int id;
    String maTask;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        maTask = intent.getStringExtra("maTask");
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        String nhankey = intent.getStringExtra("extra");
        Log.e("Music nhan Key", nhankey);
        Toast.makeText(this, nhankey, Toast.LENGTH_SHORT).show();

        if (nhankey.equals("on")) {
            id = 1;
        }
        else {
            id = 0;
        }
        if (id == 1) {
            mediaPlayer.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.stop();
                    stopSelf(); // Dừng dịch vụ

                }
            }, 1000);
        }
        else {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }
        return START_NOT_STICKY;
    }
}

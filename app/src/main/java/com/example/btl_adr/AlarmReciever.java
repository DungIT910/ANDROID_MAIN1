package com.example.btl_adr;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);


        Log.e("Toi trong receiver", "Xin chao");
        String keyFromMain = intent.getStringExtra("extra");
        database db = new database(context);
        db.updateCompleted(intent.getStringExtra("maTask"));
        Log.e("matask", intent.getStringExtra("maTask"));


        if (keyFromMain == null) {
            Toast.makeText(context, "khong lay duoc gi", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("Ban truyen key", keyFromMain);
            Toast.makeText(context, keyFromMain, Toast.LENGTH_SHORT).show();
        }
        Intent myIntent = new Intent(context, Music.class);
        myIntent.putExtra("extra", keyFromMain);
        myIntent.putExtra("userTask", intent.getStringExtra("userTask"));
        myIntent.putExtra("maTask", intent.getStringExtra("maTask"));
        context.startService(myIntent);
    }
}

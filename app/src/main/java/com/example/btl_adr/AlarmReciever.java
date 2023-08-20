package com.example.btl_adr;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);

        Log.e("Toi trong receiver", "Xin chao");
        String keyFromMain = intent.getStringExtra("extra");


        database db = new database(context);
        db.updateCompleted(intent.getStringExtra("maTask"));
//        Toast.makeText(context, context.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();

//        Log.e("secret", context.getClass().getSimpleName());

        if (keyFromMain == null) {
            Toast.makeText(context, "Khong lay duoc gi", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("Ban truyen key", keyFromMain);  // on off
            Toast.makeText(context, keyFromMain, Toast.LENGTH_SHORT).show();
        }
        Intent myIntent = new Intent(context, Music.class);
        myIntent.putExtra("extra", keyFromMain);
        myIntent.putExtra("userTask", intent.getStringExtra("userTask"));
        myIntent.putExtra("maTask", intent.getStringExtra("maTask"));
        context.startService(myIntent);
    }

}

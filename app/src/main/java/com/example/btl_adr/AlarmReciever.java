package com.example.btl_adr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Toi trong receiver", "Xin chao");
        String keyFromMain = intent.getStringExtra("extra");

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

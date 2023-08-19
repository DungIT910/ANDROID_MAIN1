package com.example.btl_adr;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainsActivity extends AppCompatActivity {
    FloatingActionButton btnTuyChonMain;
    RecyclerView recyclerView;
    database myDB;
    ArrayList<String> maTasks, tenTasks, moTaTasks, loaiTasks, thoiGianTasks, thuTasks, ngayTasks,
            thangTasks, gioTasks, trangThaiTasks;
    ArrayList<Calendar> calendarArrayList;
    CustomAdapter customAdapter;
    String taikhoan;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
    Intent intentAlarm;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    int cout = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        Toast.makeText(this, "main duoc goi", Toast.LENGTH_SHORT).show();

        // Anh xa view
        btnTuyChonMain = findViewById(R.id.btn_tuyChonMain);
        recyclerView = findViewById(R.id.recyclerView_task);

        // lay taikhoan tu intent mainactivity
        if (getIntent().hasExtra("userTask")) {
            taikhoan = getIntent().getStringExtra("userTask");
        }

        // Tao moi database va mang
        myDB = new database(MainsActivity.this);
        maTasks = new ArrayList<>();
        tenTasks = new ArrayList<>();
        moTaTasks = new ArrayList<>();
        loaiTasks = new ArrayList<>();
        thoiGianTasks = new ArrayList<>();
        trangThaiTasks = new ArrayList<>();
        calendarArrayList = new ArrayList<>();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        intentAlarm = new Intent(this, AlarmReciever.class);

        // Luu data vao mang
        storeDataInArrays(taikhoan);

        // Tao them vai mang moi con thieu de truyen vao adapter
        thuTasks = new ArrayList<>();
        ngayTasks = new ArrayList<>();
        thangTasks = new ArrayList<>();
        gioTasks = new ArrayList<>();

        //chuyen mang thoigian qua cac mang con thu, ngay, thang
        extractTimeComponents(thoiGianTasks, thuTasks, ngayTasks, thangTasks, gioTasks);

        // Tạo DividerItemDecoration với hướng dọc (VERTICAL)
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        // Đặt drawable cho đường phân chia (ví dụ: R.drawable.divider)
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Truyen du lieu tu mang vao view
        customAdapter = new CustomAdapter( MainsActivity.this, maTasks, tenTasks, moTaTasks, loaiTasks, thoiGianTasks,
                thuTasks, ngayTasks, thangTasks, gioTasks, trangThaiTasks, taikhoan, 0);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainsActivity.this));

        // Show menu khi click btnTuyChon
        btnTuyChonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMainMenu();
            }
        });
        //dat bao thuc
        stringToCalendar(thoiGianTasks,calendarArrayList, simpleDateFormat);
        for (Calendar item :calendarArrayList) {
            Log.e("check1", simpleDateFormat.format(item.getTime()).toString());
        }
        setAlarm(intentAlarm);

    }

    @Override
    public void recreate() {
        super.recreate();
    }

    // Ham show menu khi bam btnTuyChon
    private void ShowMainMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnTuyChonMain);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                // Click vao nut them
                if (itemId == R.id.menu_them) {
                    Intent intent = new Intent(MainsActivity.this, AddTaskActivity.class);
                    intent.putExtra("userTask", taikhoan);
                    startActivity(intent);
                }

                // Click vao nut thong ke
                else if (itemId == R.id.menu_thongKe) {
                    Toast.makeText(MainsActivity.this, "thong ke", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainsActivity.this, StatisticActivity.class);
                    intent.putExtra("userTask", taikhoan);
                    startActivity(intent);
                } else if (itemId == R.id.menu_lichSu) {
                    Toast.makeText(MainsActivity.this, "lich su", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainsActivity.this, HistoryActivity.class);
                    intent.putExtra("userTask", taikhoan);
                    startActivity(intent);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    void storeDataInArrays(String taikhoan) {
        Cursor cursor = myDB.readDataUserTask(taikhoan, 0);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                maTasks.add(cursor.getString(0));
                tenTasks.add(cursor.getString(2));
                moTaTasks.add(cursor.getString(3));
                loaiTasks.add(cursor.getString( 4));
                thoiGianTasks.add(cursor.getString(5));
                trangThaiTasks.add(cursor.getString(6));
            }
        }
    }

    // Chuyen mang thoi gian thanh cac mang con
    public void extractTimeComponents(@NonNull ArrayList<String> thoiGianTasks, ArrayList<String> thuTasks,
                                      ArrayList<String> ngayTasks, ArrayList<String> thangTasks,
                                      ArrayList<String> gioTasks) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");

        for (String thoiGianTask : thoiGianTasks) {
            try {
                Date date = inputFormat.parse(thoiGianTask);
                SimpleDateFormat thuFormat = new SimpleDateFormat("EEE");
                SimpleDateFormat ngayFormat = new SimpleDateFormat("dd");
                SimpleDateFormat thangFormat = new SimpleDateFormat("MMM");
                SimpleDateFormat gioFormat = new SimpleDateFormat("HH:mm");

                thuTasks.add(thuFormat.format(date) + "");
                ngayTasks.add(ngayFormat.format(date) + "");
                thangTasks.add(thangFormat.format(date) + "");
                gioTasks.add(gioFormat.format(date) + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public static void stringToCalendar(@NonNull List<String> dateStringList, List<Calendar> calendarList, SimpleDateFormat dateFormat) {
        for (String dateString : dateStringList) {
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendarList.add(calendar);
        }
    }
    public void setAlarm(Intent intent) {
        for (int i = 0; i < calendarArrayList.size(); i++) {
            intentAlarm.putExtra("extra", "on");
            int requestCode = Integer.parseInt(maTasks.get(i));
            Calendar calendar = calendarArrayList.get(i);
            pendingIntent = PendingIntent.getBroadcast(this, requestCode , intentAlarm, PendingIntent.FLAG_MUTABLE);
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.e("alarm", calendar.getTime().toString());
        }
    }
}
package com.example.btl_adr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    database myDB;
    ArrayList<String> maTasks, tenTasks, moTaTasks, loaiTasks, thoiGianTasks, thuTasks, ngayTasks,
            thangTasks, gioTasks, trangThaiTasks;
    CustomAdapter customAdapter;
    String taikhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Anh xa view
        recyclerView = findViewById(R.id.recyclerView_history);

        // lay taikhoan tu intent mainactivity
        if (getIntent().hasExtra("userTask")) {
            taikhoan = getIntent().getStringExtra("userTask");
        }

        // Tao moi database va mang
        myDB = new database(HistoryActivity.this);
        maTasks = new ArrayList<>();
        tenTasks = new ArrayList<>();
        moTaTasks = new ArrayList<>();
        loaiTasks = new ArrayList<>();
        thoiGianTasks = new ArrayList<>();
        trangThaiTasks = new ArrayList<>();

        // Luu data vao mang
        storeDataInArrays(taikhoan);

        // Tao them vai mang moi con thieu de truyen vao adapter
        thuTasks = new ArrayList<>();
        ngayTasks = new ArrayList<>();
        thangTasks = new ArrayList<>();
        gioTasks = new ArrayList<>();

        //chuyen mang thoigian qua cac mang con thu, ngay, thang
        extractTimeComponents(thoiGianTasks, thuTasks, ngayTasks, thangTasks, gioTasks);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
        // Đặt drawable cho đường phân chia
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Truyen du lieu tu mang vao view
        customAdapter = new CustomAdapter( HistoryActivity.this, maTasks, tenTasks, moTaTasks, loaiTasks, thoiGianTasks,
                thuTasks, ngayTasks, thangTasks, gioTasks, trangThaiTasks, taikhoan, 1);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HistoryActivity.this, MainsActivity.class);
        intent.putExtra("userTask", taikhoan);
        startActivity(intent);
    }
    void storeDataInArrays(String taikhoan) {
        Cursor cursor = myDB.readDataUserTask(taikhoan, 1);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                maTasks.add(cursor.getString(0));
                tenTasks.add(cursor.getString(2));
                moTaTasks.add(cursor.getString(3));
                loaiTasks.add(cursor.getString(4));
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
}
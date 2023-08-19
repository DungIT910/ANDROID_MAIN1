package com.example.btl_adr;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class StatisticActivity extends AppCompatActivity {
    LocalDateTime selectedDateTime, startTime, endTime, recentTime;
    DateTimeFormatter timeFormat, dateFormat, dateTimeFormat; // Thêm định dạng ngày
    TextView tv_ngayTask_start, tv_ngayTask_end, tv_gioTask_start, tv_gioTask_end;
    EditText edt_loaiTask_filter;
    String loaiTask_filter;
    ArrayList<String> maTasks, tenTasks, moTaTasks, loaiTasks, thoiGianTasks, thuTasks, ngayTasks,
            thangTasks, gioTasks, trangThaiTasks;
    Button btn_timKiem;
    database myDB;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    String taikhoan;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        taikhoan = getIntent().getStringExtra("userTask");
        Toast.makeText(this, taikhoan, Toast.LENGTH_SHORT).show();
        timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Khởi tạo định dạng ngày
        dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");

        // Anh xa
        recyclerView = findViewById(R.id.recyclerView_statistic);
        edt_loaiTask_filter = findViewById(R.id.edt_loaiTask_filter);
        tv_ngayTask_start = findViewById(R.id.tv_ngayTask_start);
        tv_ngayTask_end = findViewById(R.id.tv_ngayTask_end);
        tv_gioTask_start = findViewById(R.id.tv_gioTask_start);
        tv_gioTask_end = findViewById(R.id.tv_gioTask_end);
        btn_timKiem = findViewById(R.id.btn_timKiem);

        selectedDateTime = LocalDateTime.now();

        tv_gioTask_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(tv_gioTask_start);
                Toast.makeText(StatisticActivity.this, tv_gioTask_start.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        tv_gioTask_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(tv_gioTask_end);
            }
        });
        tv_ngayTask_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tv_ngayTask_start);
            }
        });
        tv_ngayTask_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tv_ngayTask_end);
            }
        });

        btn_timKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_gioTask_start.getText().equals("") || tv_gioTask_end.getText().equals("")
                        || tv_ngayTask_start.getText().equals("") || tv_ngayTask_end.getText().equals("")) {
                    showBlankErrorDialog();
                } else {
                    // Tao moi database va mang
                    myDB = new database(StatisticActivity.this);
                    maTasks = new ArrayList<>();
                    tenTasks = new ArrayList<>();
                    moTaTasks = new ArrayList<>();
                    loaiTasks = new ArrayList<>();
                    thoiGianTasks = new ArrayList<>();
                    trangThaiTasks = new ArrayList<>();
                    // Tao them vai mang moi de truyen vao adapter
                    thuTasks = new ArrayList<>();
                    ngayTasks = new ArrayList<>();
                    thangTasks = new ArrayList<>();
                    gioTasks = new ArrayList<>();

                    startTime = LocalDateTime.parse(datePlusTime(tv_ngayTask_start.getText().toString(),
                            tv_gioTask_start.getText().toString()), dateTimeFormat);
                    endTime = LocalDateTime.parse(datePlusTime(tv_ngayTask_end.getText().toString(),
                            tv_gioTask_end.getText().toString()), dateTimeFormat);
                    loaiTask_filter = edt_loaiTask_filter.getText().toString().trim();
                    Log.e("www", startTime.format(dateTimeFormat));
                    Log.e("www", endTime.format(dateTimeFormat));

                    // Luu data vao mang
                    storeDataInArrays(taikhoan);

                    //chuyen mang thoigian qua cac mang con thu, ngay, thang
                    extractTimeComponents(thoiGianTasks, thuTasks, ngayTasks, thangTasks, gioTasks);
                    // Tạo DividerItemDecoration với hướng dọc (VERTICAL)
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
                    // Đặt drawable cho đường phân chia (ví dụ: R.drawable.divider)
                    recyclerView.addItemDecoration(dividerItemDecoration);

                    // Truyen du lieu tu mang vao view
                    customAdapter = new CustomAdapter(StatisticActivity.this, maTasks, tenTasks, moTaTasks, loaiTasks, thoiGianTasks,
                            thuTasks, ngayTasks, thangTasks, gioTasks, trangThaiTasks, taikhoan, 0);
                    recyclerView.setAdapter(customAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(StatisticActivity.this));
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btn_timKiem.callOnClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StatisticActivity.this, MainsActivity.class);
        intent.putExtra("userTask", taikhoan);
        startActivity(intent);
    }

    public void showTimePickerDialog(final TextView targetTextView) {
        selectedDateTime = LocalDateTime.now();
        int hour = selectedDateTime.getHour();
        int minute = selectedDateTime.getMinute();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedDateTime = selectedDateTime.withHour(hourOfDay).withMinute(minute);
                        String formattedTime = selectedDateTime.format(timeFormat);
                        targetTextView.setText(formattedTime);
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    public void showDatePickerDialog(final TextView targetTextView) {
        selectedDateTime = LocalDateTime.now();
        int year = selectedDateTime.getYear();
        int month = selectedDateTime.getMonthValue() - 1; // DatePicker sử dụng giá trị từ 0 - 11 cho tháng
        int day = selectedDateTime.getDayOfMonth();


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDateTime = selectedDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth);
                        String formattedDate = selectedDateTime.format(dateFormat);
                        Log.e("realtime", selectedDateTime.format(dateTimeFormat));
                        targetTextView.setText(formattedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    public String datePlusTime(String date, String time) {
        return date + "-" + time;
    }

    void storeDataInArrays(String taikhoan) {
        Cursor cursor = myDB.readDataUserTask(taikhoan);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String thoiGianCursor = cursor.getString(5);
                recentTime = LocalDateTime.parse(thoiGianCursor, dateTimeFormat);
                String loaiTaskCursor = cursor.getString(4);
                // Neu loaiTask bo trong thi khong quan tam chi so sanh thoi gian
                // va nguoc lai, neu loaiTask co dien thi kiem tra dieu kien
                if ((loaiTask_filter.equals("") ? true : (loaiTaskCursor.equalsIgnoreCase(loaiTask_filter)))
                        && (recentTime.isAfter(startTime) && recentTime.isBefore(endTime))) {
                    maTasks.add(cursor.getString(0));
                    tenTasks.add(cursor.getString(2));
                    moTaTasks.add(cursor.getString(3));
                    loaiTasks.add(cursor.getString(4));
                    thoiGianTasks.add(cursor.getString(5));
                    trangThaiTasks.add(cursor.getString(6));
                    Log.e("www", recentTime.format(dateTimeFormat));
                }
            }
        }
    }

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

    private void showBlankErrorDialog() {
        ConstraintLayout customdialog = findViewById(R.id.error_dialog);
        View view = LayoutInflater.from(StatisticActivity.this).inflate(R.layout.error_dialog, customdialog);
        Button okButton = view.findViewById(R.id.successDone);
        TextView error_content = view.findViewById(R.id.error_content);
        error_content.setText("Vui lòng không bỏ trống ngày tháng");
        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        okButton.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}
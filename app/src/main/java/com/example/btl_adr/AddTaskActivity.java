package com.example.btl_adr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    EditText edtTenTask, edtmoTaTask, edtloaiTask, edtngayTask, edtgioTask;
    Button btnThemTask;
    String taikhoan, tenTaskCheck, thoiGianTaskCheck, tenTask, moTaTask, loaiTask, thoiGianTask;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Anh xa
        edtTenTask = findViewById(R.id.edt_tenTask);
        edtmoTaTask = findViewById(R.id.edt_moTaTask);
        edtloaiTask = findViewById(R.id.edt_loaiTask);
        edtngayTask = findViewById(R.id.edt_ngayTask);
        edtgioTask = findViewById(R.id.edt_gioTask);
        btnThemTask = findViewById(R.id.btn_themTask);
        taikhoan = getIntent().getStringExtra("userTask");
        calendar = Calendar.getInstance();
        Toast.makeText(AddTaskActivity.this, taikhoan, Toast.LENGTH_SHORT).show();

        btnThemTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tenTask = edtTenTask.getText().toString().trim();
                moTaTask = edtmoTaTask.getText().toString().trim();
                loaiTask = edtloaiTask.getText().toString().trim();
                thoiGianTask = edtngayTask.getText().toString() + "-"
                        + edtgioTask.getText().toString();

                // Kiem tra loi bo trong
                if (tenTask.equals("") || moTaTask.equals("") || loaiTask.equals("")
                        || edtngayTask.equals("") || edtgioTask.equals("")) {
                    showBlankErrorDialog();
                }

                // kiem tra loi trung lich trinh
                else if (checkDuplicate(taikhoan)) {
                    showDuplicateErrorDialog();
                }
//                else if (checkInvalidTime(thoiGianTask)) {
//                    showInvalidTimeErrorDialog();
//                }
                else {
                    database myDB = new database(AddTaskActivity.this);
                    myDB.addTask(taikhoan, tenTask, moTaTask, loaiTask, thoiGianTask);
                    Intent intent = new Intent(AddTaskActivity.this, MainsActivity.class);
                    intent.putExtra("userTask", taikhoan);
                    startActivity(intent);
                }
            }
        });
        edtngayTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonNgay();
                Log.e("Lich", calendar.getTime() + "");
            }
        });
        edtgioTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonGio();
            }
        });
    }
    public void chonGio() {
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        // Lưu giờ và phút đã chọn vào đối tượng calendar
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        edtgioTask.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, gio, phut, true);
        timePickerDialog.show();
    }
    public void chonNgay() {
        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                //i nam i1 thang i2 ngay
                calendar.set(i, i1, i2);
                edtngayTask.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
    private void showBlankErrorDialog(){
        ConstraintLayout customdialog = findViewById(R.id.error_dialog);
        View view = LayoutInflater.from(AddTaskActivity.this).inflate(R.layout.error_dialog, customdialog);
        Button successDone = view.findViewById(R.id.successDone);
        TextView error_content = view.findViewById(R.id.error_content);
        error_content.setText("Vui lòng không bỏ trống");
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        successDone.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private void showDuplicateErrorDialog(){
        ConstraintLayout customdialog = findViewById(R.id.error_dialog);
        View view = LayoutInflater.from(AddTaskActivity.this).inflate(R.layout.error_dialog, customdialog);
        Button successDone = view.findViewById(R.id.successDone);
        TextView error_content = view.findViewById(R.id.error_content);
        error_content.setText("Bạn đã nhập trùng lịch trình");
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        successDone.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private boolean checkDuplicate(String taikhoan) {
        database myDB = new database(AddTaskActivity.this);
        Cursor cursor = myDB.readDataUserTask(taikhoan);
        while (cursor.moveToNext()) {
            tenTaskCheck = cursor.getString(2);
            thoiGianTaskCheck = cursor.getString(5);
            if (tenTaskCheck.equals(tenTask) && thoiGianTaskCheck.equals(thoiGianTask)) {
                return true;
            }
        }
        return false;
    }
    private void showInvalidTimeErrorDialog() {
        ConstraintLayout customdialog = findViewById(R.id.error_dialog);
        View view = LayoutInflater.from(AddTaskActivity.this).inflate(R.layout.error_dialog, customdialog);
        Button successDone = view.findViewById(R.id.successDone);
        TextView error_content = view.findViewById(R.id.error_content);
        error_content.setText("Thời gian đặt không hợp lệ");
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        successDone.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
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

    private boolean checkInvalidTime(String timeString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        Calendar calendar1 = Calendar.getInstance();
        try {
            Date date = simpleDateFormat.parse(timeString);
            calendar1.setTime(date);
            if (calendar1.getTimeInMillis() < System.currentTimeMillis()) {
                return true;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
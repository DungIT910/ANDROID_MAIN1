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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateTaskActivity extends AppCompatActivity {
    EditText edtTenTask2, edtmoTaTask2, edtloaiTask2, edtngayTask2, edtgioTask2;
    Button btnCapNhatTask, btnXoaTask, btnQuayLai;
    String maTask, tenTask, moTaTask, loaiTask, thoiGianTask, ngayTask, gioTask;
    String tenTask_new, moTaTask_new, loaiTask_new, thoiGianTask_new, ngayTask_new, gioTask_new;
    String tenTaskCheck, moTaskCheck, loaiTask_check, thoiGianTaskCheck;
    String taikhoan;
    String PreActivity;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        PreActivity = getIntent().getStringExtra("tenActivity");
        // Anh xa view
        edtTenTask2 = findViewById(R.id.edt_tenTask2);
        edtmoTaTask2 = findViewById(R.id.edt_moTaTask2);
        edtloaiTask2 = findViewById(R.id.edt_loaiTask2);
        edtngayTask2 = findViewById(R.id.edt_ngayTask2);
        edtgioTask2 = findViewById(R.id.edt_gioTask2);

        btnXoaTask = findViewById(R.id.btn_xoaTask);
        btnQuayLai = findViewById(R.id.btn_quayLai);
        btnCapNhatTask = findViewById(R.id.btn_capNhatTask);
        if (PreActivity.equals("HistoryActivity")) {
            btnCapNhatTask.setEnabled(false);
            edtTenTask2.setEnabled(false);
            edtmoTaTask2.setEnabled(false);
            edtloaiTask2.setEnabled(false);
            edtngayTask2.setEnabled(false);
            edtgioTask2.setEnabled(false);
        }
        calendar = Calendar.getInstance();

        getAndSetIntentData();

        database db = new database(UpdateTaskActivity.this);

        edtngayTask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        edtgioTask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickHour();
            }
        });
        btnCapNhatTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   String tenTask_new, moTaTask_new, loaiTask_new, thoiGianTask_new;

                tenTask_new = edtTenTask2.getText().toString().trim();
                moTaTask_new = edtmoTaTask2.getText().toString().trim();
                loaiTask_new = edtloaiTask2.getText().toString().trim();
                ngayTask_new = edtngayTask2.getText().toString();
                gioTask_new = edtgioTask2.getText().toString();
                thoiGianTask_new = ngayTask_new + "-" + gioTask_new;

                // Kiem tra loi bo trong
                if (tenTask_new.equals("") || moTaTask_new.equals("") || loaiTask_new.equals("")
                        || ngayTask_new.equals("") || gioTask_new.equals("")) {
                    showBlankErrorDialog();
                }
                // kiem tra loi trung lich trinh
                else if (checkDuplicate(taikhoan)) {
                    showDuplicateErrorDialog();
                }else if (checkInvalidTime(thoiGianTask_new)) {
                    showInvalidTimeErrorDialog();
                }
                //
                else {
                    db.updateTask(maTask, taikhoan, tenTask_new, moTaTask_new, loaiTask_new, thoiGianTask_new);
                }
            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnXoaTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Xử lý các thao tác sau khi nút "Back" được gọi
        if (PreActivity.equals("MainsActivity")) {
            Intent intent = new Intent(UpdateTaskActivity.this, MainsActivity.class);
            intent.putExtra("userTask", taikhoan);
            startActivity(intent);
        } else if (PreActivity.equals("HistoryActivity")) {
            Intent intent = new Intent(UpdateTaskActivity.this, HistoryActivity.class);
            intent.putExtra("userTask", taikhoan);
            startActivity(intent);
        }
//        else if (PreActivity.equals("StatisticActivity")) {
//            Intent intent = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                intent = new Intent(UpdateTaskActivity.this, StatisticActivity.class);
//            }
//            intent.putExtra("userTask", taikhoan);
//            startActivity(intent);
//        }

    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("maTask") && getIntent().hasExtra("tenTask")
                && getIntent().hasExtra("moTaTask") && getIntent().hasExtra("loaiTask")
                && getIntent().hasExtra("thoiGianTask") && getIntent().hasExtra("userTask")) {
            //get intent data
            maTask = getIntent().getStringExtra("maTask");
            tenTask = getIntent().getStringExtra("tenTask");
            moTaTask = getIntent().getStringExtra("moTaTask");
            loaiTask = getIntent().getStringExtra("loaiTask");
            thoiGianTask = getIntent().getStringExtra("thoiGianTask");
            taikhoan = getIntent().getStringExtra("userTask");

            // chuyen chuoi thoigian thanh chuoi ngay thang
            ngayTask = thoiGianTask.substring(0, 10);
            gioTask = thoiGianTask.substring(11, 16);

            // Lay noi dung tu chuoi set cho edittext
            edtTenTask2.setText(tenTask);
            edtmoTaTask2.setText(moTaTask);
            edtloaiTask2.setText(loaiTask);
            edtngayTask2.setText(ngayTask);
            edtgioTask2.setText(gioTask);

        }
    }

    public void pickHour() {
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateTaskActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        // Lưu giờ và phút đã chọn vào đối tượng calendar
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        edtgioTask2.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, gio, phut, true);
        timePickerDialog.show();
    }

    private void pickDate() {
        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                //i nam i1 thang i2 ngay
                calendar.set(i, i1, i2);
                edtngayTask2.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        Log.e("GPT", ngay + " " + thang + " " + nam);
        datePickerDialog.show();
    }

    public void showConfirmDialog() {
        ConstraintLayout confirmLayout = findViewById(R.id.confirmDialog);
        View view = LayoutInflater.from(UpdateTaskActivity.this).inflate(R.layout.confirm_dialog, confirmLayout);
        Button btn_khong = view.findViewById(R.id.button_khong);
        Button btn_co = view.findViewById(R.id.button_co);
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btn_khong.findViewById(R.id.button_khong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_co.findViewById(R.id.button_co).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database myDB = new database(UpdateTaskActivity.this);
                Toast.makeText(UpdateTaskActivity.this, maTask, Toast.LENGTH_SHORT).show();
                myDB.deleteTask(maTask);
                onBackPressed();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showBlankErrorDialog() {
        ConstraintLayout customdialog = findViewById(R.id.error_dialog);
        View view = LayoutInflater.from(UpdateTaskActivity.this).inflate(R.layout.error_dialog, customdialog);
        Button okButton = view.findViewById(R.id.successDone);
        TextView error_content = view.findViewById(R.id.error_content);
        error_content.setText("Vui lòng không bỏ trống");
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
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

    private void showDuplicateErrorDialog() {
        ConstraintLayout customdialog = findViewById(R.id.error_dialog);
        View view = LayoutInflater.from(UpdateTaskActivity.this).inflate(R.layout.error_dialog, customdialog);
        Button successDone = view.findViewById(R.id.successDone);
        TextView error_content = view.findViewById(R.id.error_content);
        error_content.setText("Bạn đã nhập trùng lịch trình");
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
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

    private boolean checkDuplicate(String taikhoan) {
        database myDB = new database(UpdateTaskActivity.this);
        Cursor cursor = myDB.readDataUserTask(taikhoan);
        while (cursor.moveToNext()) {
            //d(cursor.getString(0));
            //                    tenTasks.add(cursor.getString(2));
            //                    moTaTasks.add(cursor.getString(3));
            //                    loaiTasks.add(cursor.getString(4));
            //                    thoiGianTasks.add(cursor.getString(5));
            //                    trangThaiTasks.add(cursor.getString(6));
            //                    Log.e("www", recentTime.format(dateTimeFormat));
            if (tenTask.equals(tenTask_new) && thoiGianTask.equals(thoiGianTask_new)) {
                tenTaskCheck = cursor.getString(2);
                moTaskCheck = cursor.getString(3);
                loaiTask_check = cursor.getString(4);
                thoiGianTaskCheck = cursor.getString(5);
                if (tenTaskCheck.equals(tenTask_new) && thoiGianTaskCheck.equals(thoiGianTask_new)
                        && moTaskCheck.equals(moTaTask_new) && loaiTask_check.equals(loaiTask_new)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void showInvalidTimeErrorDialog() {
        ConstraintLayout customdialog = findViewById(R.id.error_dialog);
        View view = LayoutInflater.from(UpdateTaskActivity.this).inflate(R.layout.error_dialog, customdialog);
        Button successDone = view.findViewById(R.id.successDone);
        TextView error_content = view.findViewById(R.id.error_content);
        error_content.setText("Thời gian đặt không hợp lệ");
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
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
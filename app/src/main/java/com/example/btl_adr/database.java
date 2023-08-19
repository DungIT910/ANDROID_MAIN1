package com.example.btl_adr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.security.cert.TrustAnchor;

public class database extends SQLiteOpenHelper {
    private Context context;
    public static String TB_TASK = "TASK";
    public static String TB_TASK_MATASK = "MATASK";
    public static String TB_TASK_USERTASK = "USERTASK";
    public static String TB_TASK_TENTASK = "TENTASK";
    public static String TB_TASK_MOTATASK = "MOTATASK";
    public static String TB_TASK_LOAITASK = "LOAITASK";
    public static String TB_TASK_THOIGIANTASK = "THOIGIANTASK";
    public static String TB_TASK_TRANGTHAITASK = "TRANGTHAITASK";
    public database(Context context) {
        super(context, "huys.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE user(taikhoan text PRIMARY KEY, matkhau text, hoten text)";
        db.execSQL(sql);
        sql = "INSERT INTO user VALUES('huy', 'huy', 'PHAM GIA HUY')";
        db.execSQL(sql);

        //  Tao bang chua cac Task
        sql = "CREATE TABLE " + TB_TASK + "(" + TB_TASK_MATASK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_TASK_USERTASK + " TEXT," + TB_TASK_TENTASK + " TEXT, " + TB_TASK_MOTATASK + " TEXT,"
                + TB_TASK_LOAITASK + " TEXT," + TB_TASK_THOIGIANTASK + " TEXT, " + TB_TASK_TRANGTHAITASK
                + " INTEGER DEFAULT '0'," + "FOREIGN KEY (" + TB_TASK_USERTASK +") REFERENCES user(taikhoan))";

        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
    // Con tro doc tat ca du lieu tu bang Task
    Cursor readAllDataTask() {
        String query = "SELECT * FROM " + TB_TASK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db!=null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    // Con tro doc du lieu theo user
    Cursor readDataUserTask(String taikhoan) {
        String query = "SELECT * FROM " + TB_TASK + " WHERE " + TB_TASK_USERTASK + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{taikhoan});
        }
        return cursor;
    }

    Cursor readDataUserTask(String taikhoan, int isDone) {
        String query = "SELECT * FROM " + TB_TASK + " WHERE " + TB_TASK_USERTASK + " = ? AND "
                + TB_TASK_TRANGTHAITASK + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{taikhoan, String.valueOf(isDone)});
        }
        return cursor;
    }
    //                myDB.addTask(taikhoan, tenTask, moTaTask, loaiTask, thoiGianTask);
    void addTask(String userTask, String tenTask, String moTaTask, String loaiTask,
                 String thoiGianTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TB_TASK_USERTASK, userTask);
        cv.put(TB_TASK_TENTASK, tenTask);
        cv.put(TB_TASK_MOTATASK, moTaTask);
        cv.put(TB_TASK_LOAITASK, loaiTask);
        cv.put(TB_TASK_THOIGIANTASK, thoiGianTask);

        long result = db.insert(TB_TASK, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Them that bai!!!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Them thanh cong!!", Toast.LENGTH_SHORT).show();
        }
    }

    void updateTask(String maTask, String userTask, String tenTask, String moTaTask,
                    String loaiTask, String thoiGianTask) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TB_TASK_MATASK, maTask);
        cv.put(TB_TASK_USERTASK, userTask);
        cv.put(TB_TASK_TENTASK, tenTask);
        cv.put(TB_TASK_MOTATASK, moTaTask);
        cv.put(TB_TASK_LOAITASK, loaiTask);
        cv.put(TB_TASK_THOIGIANTASK, thoiGianTask);
        cv.put(TB_TASK_TRANGTHAITASK, 0);

        long result = db.update(TB_TASK, cv, TB_TASK_MATASK + " = ?", new String[]{String.valueOf(maTask)});
        if (result == -1) {
            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        }
    }
    void updateCompleted(String maTask) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TB_TASK_TRANGTHAITASK, 1);

        long result = db.update(TB_TASK, cv, TB_TASK_MATASK + " = ?", new String[]{String.valueOf(maTask)});
        if (result == -1) {
            Toast.makeText(context, "Task da qua thoi gian nhung loi", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Task thành công", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteTask(String maTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TB_TASK, TB_TASK_MATASK+"=?", new String[]{maTask});
        if (result == -1) {
            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Xoa thành công", Toast.LENGTH_SHORT).show();
        }
    }
}

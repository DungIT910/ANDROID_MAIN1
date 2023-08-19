package com.example.btl_adr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

public class userDAO {
    public static ArrayList<user> getAll(Context context){
        ArrayList<user> ds = new ArrayList<>();
        database db = new database(context);
        SQLiteDatabase dbs = db.getReadableDatabase();
        Cursor cs = dbs.rawQuery("SELECT * FROM user", null);
        cs.moveToFirst();
        if (cs != null) {
            while (!cs.isAfterLast()) {
                String taikhoan = cs.getString(0);
                String matkhau = cs.getString(1);
                String hoten = cs.getString(2);
                user u = new user(taikhoan, matkhau, hoten);
                ds.add(u);
                cs.moveToNext();
            }
        }
        else {
            Toast.makeText(context, "Database trong khong", Toast.LENGTH_SHORT).show();
        }
        cs.close();
        return ds;
    }
    public static boolean insert(Context context, String taikhoan, String matkhau, String hoten)
    {
        database data = new database(context);
        SQLiteDatabase db = data.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("taikhoan", taikhoan);
        values.put("matkhau", matkhau);
        values.put("hoten", hoten);
        long row = db.insert("user", null, values);
        return (row > 0);

    }
    public static boolean update(Context context, user u)
    {
        database data = new database(context);
        SQLiteDatabase db = data.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("matkhau", u.getMatkhau());
        values.put("hoten", u.getHoten());
        long row = db.update("user", values, "taikhoan=?", new String[]{u.getTaikhoan()});
        return (row > 0);

    }

    public static boolean delete(Context context, String taikhoans)
    {
        database data = new database(context);
        SQLiteDatabase db = data.getWritableDatabase();
        long row = db.delete("user", "taikhoan=?", new String[]{taikhoans + ""});
        return (row > 0);

    }
}

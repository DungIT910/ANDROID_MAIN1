package com.example.btl_adr;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class AdminActivity extends AppCompatActivity {
    ArrayAdapter adapter;
    ArrayList<user> us;
    userDAO ud;
    database db;
    ListView lv;
    ImageView img;
    Button btn1;
    Button btn2;
    Button btn3;
    String tk ="";

    int a;
    String b = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        lv = (ListView) findViewById(R.id.listviewez);
        us = new ArrayList<>();
        us = userDAO.getAll(AdminActivity.this);
        adapter = new ArrayAdapter(AdminActivity.this, android.R.layout.simple_expandable_list_item_1, us);
        lv.setAdapter(adapter);
        btn1 = (Button)findViewById(R.id.btnthems);
        btn2 = (Button)findViewById(R.id.btncn);
        btn3 = (Button)findViewById(R.id.btnxoa);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog1();
//
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog2();

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               a = position;
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdminActivity.this, MainsActivity.class);
                user i = us.get(position);
                String h = i.getTaikhoan();
                intent.putExtra("userTask", h);
                startActivity(intent);
                return true;
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user u = us.get(a);
                String c = u.getTaikhoan();
                if (userDAO.delete(AdminActivity.this, c))
                {
                    Toast.makeText(AdminActivity.this, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                    us.clear();
                    us.addAll(userDAO.getAll(AdminActivity.this));
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }
    private void showDialog1() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogthem, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        user u = us.get(a);
        b = u.getTaikhoan();
        dialog.show();
       Button btn4 = (Button) view.findViewById(R.id.btnthemm);

       @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText ed1 = (EditText) view.findViewById(R.id.edadhoten);
       EditText ed2 = (EditText) view.findViewById(R.id.edadtaikhoan);
       EditText ed3 = (EditText) view.findViewById(R.id.edadmatkhau);

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taikhoan = ed2.getText().toString();
                String matkhau = ed3.getText().toString();

                String hoten = ed1.getText().toString();
                if (userDAO.insert(AdminActivity.this, taikhoan, matkhau, hoten)){
                    Toast.makeText(AdminActivity.this, "Them moi thanh cong", Toast.LENGTH_SHORT).show();
                    us.clear();
                    us.addAll(userDAO.getAll(AdminActivity.this));
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(AdminActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                user u = us.get(a);
//                u.setHoten(ed3.getText().toString());
//                u.setTaikhoan(ed1.getText().toString());
//                u.setMatkhau(ed2.getText().toString());
//                if (userDAO.update(AdminActivity.this, u))
//                {
//                    Toast.makeText(AdminActivity.this, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
//                    us.clear();
//                    us.addAll(userDAO.getAll(AdminActivity.this));
//                    adapter.notifyDataSetChanged();
//                    dialog.dismiss();
//
//                }
//            }
//        });
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (NHANVIENDAO.delete(MainActivity.this, nvs.getManv()))
//                {
//                    Toast.makeText(MainActivity.this, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
//                    nv.clear();
//                    nv.addAll(NHANVIENDAO.getAll(MainActivity.this));
//                    adapter.notifyDataSetChanged();
//                    dialog.dismiss();
//
//                }
//            }
//        });

    }
    private void showDialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogsua, null);
        builder.setView(view);
        Dialog dialog = builder.create();

        user u = us.get(a);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btn5 = (Button) view.findViewById(R.id.btnsuaa);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText ed5 = (EditText) view.findViewById(R.id.edsuahoten);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText ed7 = (EditText) view.findViewById(R.id.edsuamatkhau);

        ed5.setText(u.getHoten());

        ed7.setText(u.getMatkhau());
        dialog.show();
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user u = us.get(a);


                u.setMatkhau(ed7.getText().toString());
                u.setHoten(ed5.getText().toString());

                if (userDAO.update(AdminActivity.this, u))
                {
                    Toast.makeText(AdminActivity.this, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                    us.clear();
                    us.addAll(userDAO.getAll(AdminActivity.this));
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();

                }


            }
        });





    }
}

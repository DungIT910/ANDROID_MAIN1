package com.example.btl_adr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class loginActivity extends AppCompatActivity {
    Button btn1;
    EditText ed1;
    EditText ed2;
    EditText ed3;
    Button btn2;
    ArrayList<user> us;
    userDAO ud;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn1 = (Button) findViewById(R.id.btn1login);
        btn2 = (Button) findViewById(R.id.btn2login);
        ed1 = (EditText) findViewById(R.id.edhoten);
        ed2 = (EditText) findViewById(R.id.edtaikhoan);
        ed3 = (EditText) findViewById(R.id.edmatkhau);
        us = new ArrayList<>();
        us = userDAO.getAll(loginActivity.this);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hoten = ed1.getText().toString();
                String taikhoan = ed2.getText().toString();
                String matkhau = ed3.getText().toString();
                if (userDAO.insert(loginActivity.this, taikhoan, matkhau, hoten)) {
                    Toast.makeText(loginActivity.this, "Them moi thanh cong", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(loginActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}

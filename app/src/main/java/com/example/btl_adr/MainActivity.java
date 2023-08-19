package com.example.btl_adr;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn1s;
    Button btn2s;
    EditText ed1s;
    EditText ed2s;
    Button btn3s;
    ArrayList<user> us;
    public static String taikhoan ="";
    public static String matkhau="";


    userDAO ud;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1s = (Button) findViewById(R.id.btn1);
        btn2s = (Button) findViewById(R.id.btn2);
        ed1s = (EditText) findViewById(R.id.taikhoan);
        ed2s = (EditText) findViewById(R.id.matkhau);
        us = new ArrayList<>();


        btn1s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = 0;
                us = userDAO.getAll(MainActivity.this);

                String taikhoans = ed1s.getText().toString();
                String matkhaus = ed2s.getText().toString();

                for (int i = 0; i < us.size(); i++){
                    user u = us.get(i);
                    if (taikhoans.equals(u.getTaikhoan()) && matkhaus.equals(u.getMatkhau()))
                    {
                        taikhoan = taikhoans;
                        matkhau = matkhaus;
                        a++;
                    }
                }

                if (a == 0){
                    showFailDialog();
//                    Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (taikhoans.equals("huy") && matkhaus.equals("huy")){
                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(intent);
                    }

                    else{
                        Intent intent = new Intent(MainActivity.this, MainsActivity.class);
                        intent.putExtra("userTask", taikhoans);
//                        Toast.makeText(MainActivity.this, taikhoans, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
            }
        });
        btn2s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

    }
    private void showFailDialog(){
        ConstraintLayout customdialog = findViewById(R.id.error_dialog);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.error_dialog, customdialog);
        Button successDone = view.findViewById(R.id.successDone);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

}
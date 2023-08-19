package com.example.btl_adr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList maTasks, tenTasks, moTaTasks, loaiTasks, thoiGianTask, thuTasks, ngayTasks,
            thangTasks, gioTasks, trangThaiTasks;
    String taikhoan;
    private int defineAcivity;

    //constructor
    public CustomAdapter(Context context, ArrayList maTasks, ArrayList tenTasks, ArrayList moTaTasks,
                         ArrayList loaiTasks, ArrayList thoiGianTask, ArrayList thuTasks,
                         ArrayList ngayTasks, ArrayList thangTasks, ArrayList gioTasks,
                         ArrayList trangThaiTasks, String taikhoan, int defineActivity) {
        this.context = context;
        this.maTasks = maTasks;
        this.tenTasks = tenTasks;
        this.moTaTasks = moTaTasks;
        this.loaiTasks = loaiTasks;
        this.thoiGianTask = thoiGianTask;
        this.thuTasks = thuTasks;
        this.ngayTasks = ngayTasks;
        this.thangTasks = thangTasks;
        this.gioTasks = gioTasks;
        this.trangThaiTasks = trangThaiTasks;
        this.taikhoan = taikhoan;
        this.defineAcivity = defineActivity;
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_task, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        int itemPosition = position;

        holder.tv_tenTask.setText(String.valueOf(tenTasks.get(position)));
        holder.tv_loaiTask.setText(String.valueOf(loaiTasks.get(position)));
        holder.tv_thuTask.setText(String.valueOf(thuTasks.get(position)));
        holder.tv_ngayTask.setText(String.valueOf(ngayTasks.get(position)));
        holder.tv_thangTask.setText(String.valueOf(thangTasks.get(position)));
        holder.tv_gioTask.setText(String.valueOf(gioTasks.get(position)));
        holder.btn_tuyChonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.popup_menu_task);
                MenuItem menuItemChinhSua = popupMenu.getMenu().findItem(R.id.btn_popup_chinhSuaTask);
                // neu activity truoc do la HistoryActivity thi khong co nut chinh sua
                if (defineAcivity == 1) {
                    menuItemChinhSua.setVisible(false);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.btn_popup_xoatask) {
                            ConstraintLayout confirmLayout = holder.itemView.findViewById(R.id.confirmDialog);
                            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.confirm_dialog, confirmLayout);
                            Button btn_khong = view.findViewById(R.id.button_khong);
                            Button btn_co = view.findViewById(R.id.button_co);
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setView(view);
                            final AlertDialog alertDialog = builder.create();
                            btn_khong.findViewById(R.id.button_khong).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            btn_co.findViewById(R.id.button_co).setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onClick(View v) {
                                    database myDB = new database(holder.itemView.getContext());
                                    Toast.makeText(holder.itemView.getContext(), maTasks.get(itemPosition) + "", Toast.LENGTH_SHORT).show();
                                    myDB.deleteTask((String)maTasks.get(itemPosition));
                                    if (holder.itemView.getContext() instanceof StatisticActivity) {
                                        ((StatisticActivity) holder.itemView.getContext()).onRestart();
                                        alertDialog.dismiss();
                                    }
                                    else {
                                        OtherClass otherClass = new OtherClass(holder.itemView.getContext());
                                        otherClass.restartCurrentActivity();
                                    }
                                }
                            });
                            if (alertDialog.getWindow() != null)
                            {
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                            }
                            alertDialog.show();
                        }
                        else if (item.getItemId() == R.id.btn_popup_chinhSuaTask) {
                            Toast.makeText(context, "popup chinh sua", Toast.LENGTH_SHORT).show();
                            holder.dong_layout.callOnClick();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        holder.dong_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateTaskActivity.class);
                // putExtra de activity ben kia kiem tra activity nao ben nay
                intent.putExtra("tenActivity", context.getClass().getSimpleName());
                // truyen du lieu vao intent
                intent.putExtra("maTask", String.valueOf(maTasks.get(itemPosition)));
                intent.putExtra("tenTask", String.valueOf(tenTasks.get(itemPosition)));
                intent.putExtra("moTaTask", String.valueOf(moTaTasks.get(itemPosition)));
                intent.putExtra("loaiTask", String.valueOf(loaiTasks.get(itemPosition)));
                intent.putExtra("thoiGianTask", String.valueOf(thoiGianTask.get(itemPosition)));
                intent.putExtra("trangThaiTask", String.valueOf(trangThaiTasks.get(itemPosition)));
                intent.putExtra("userTask", taikhoan);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tenTasks.size();
    }
    public void showConfirmDialog(@NonNull View view, String maTask) {
        ConstraintLayout confirmLayout = view.findViewById(R.id.confirmDialog);
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.confirm_dialog, confirmLayout);
        Button btn_khong = v.findViewById(R.id.button_khong);
        Button btn_co = v.findViewById(R.id.button_co);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(v);
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
                database myDB = new database(view.getContext());
                Toast.makeText(view.getContext(), maTask, Toast.LENGTH_SHORT).show();
                myDB.deleteTask(maTask);
            }
        });
        if (alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenTask, tv_loaiTask, tv_thuTask, tv_ngayTask,
                tv_thangTask, tv_gioTask;
        Button btn_tuyChonTask;
        LinearLayout dong_layout;

        public MyViewHolder(@NonNull View itemView)  {
            super(itemView);
            tv_tenTask = itemView.findViewById(R.id.tv_tenTask);
            tv_loaiTask = itemView.findViewById(R.id.tv_loaiTask);
            tv_thuTask = itemView.findViewById(R.id.tv_thuTask);
            tv_ngayTask = itemView.findViewById(R.id.tv_ngayTask);
            tv_thangTask = itemView.findViewById(R.id.tv_thangTask);
            tv_gioTask = itemView.findViewById(R.id.tv_gioTask);
            btn_tuyChonTask = itemView.findViewById(R.id.btn_tuyChonTask);
            dong_layout = itemView.findViewById(R.id.dong_layout);
        }
    }
    public class OtherClass {
        private Context mContext;

        public OtherClass(Context context) {
            mContext = context;
        }

        public void restartCurrentActivity() {
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                activity.recreate();
            }
        }
    }
}

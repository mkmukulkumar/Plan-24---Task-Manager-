package com.babra.plan24;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Dialog detail_popup;
    Button cancel_button, save_button;
    EditText popup_task_name,popup_hh,popup_mm,popup_ss;
    RecyclerView task_list;
    String[] data = {"sajc", "sacbsa", "dfadsnf"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding XMLs by ids
        FloatingActionButton fab = findViewById(R.id.fab);
        task_list = findViewById(R.id.task_list);
        detail_popup = new Dialog(MainActivity.this);

        //RecyclerView Setup
        task_list.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapter adapter = new CustomAdapter(data);
        task_list.setAdapter(adapter);
        get_all st1 = new get_all();
        st1.start();
        //Popup functionality and floating buttons
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                detail_popup.setContentView(R.layout.detail_popup);
                popup_task_name=detail_popup.findViewById(R.id.popup_task_name);
                popup_hh=detail_popup.findViewById(R.id.popup_hh);
                popup_mm=detail_popup.findViewById(R.id.popup_mm);
                popup_ss=detail_popup.findViewById(R.id.popup_ss);
                cancel_button = detail_popup.findViewById(R.id.cancel_button);
                save_button = detail_popup.findViewById(R.id.save_button);
                Window window = detail_popup.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);
                detail_popup.show();
                cancel_button.setOnClickListener(view1 -> detail_popup.dismiss());
                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Room database setup
                        task_data data3 = new task_data();
                        data3.setTask_name(popup_task_name.getText().toString().trim());
                        data3.setHH(Integer.parseInt(popup_hh.getText().toString().trim()));
                        data3.setMM(Integer.parseInt(popup_mm.getText().toString().trim()));
                        data3.setSS(Integer.parseInt(popup_ss.getText().toString().trim()));
                        add_ask st = new add_ask(data3);
                        st.start();
                        get_all st1 = new get_all();
                        st1.start();
                        detail_popup.dismiss();

                    }
                });

            }
        });

    }
    class add_ask extends Thread {
        task_data data3;
        add_ask(task_data data3) {
            this.data3=data3;
        }
        public void run(){
            task_data_database db = Room.databaseBuilder(getApplicationContext(), task_data_database.class, "database-name").build();
            task_data_dao task_data_dao = db.task_data_dao();
            task_data_dao.insert(data3);
        }
    }
    class get_all extends Thread {
        public void run() {
            task_data_database db1 = Room.databaseBuilder(getApplicationContext(), task_data_database.class, "database-name").build();
            task_data_dao task_data_dao = db1.task_data_dao();
            List<String> data3 = task_data_dao.getAll();
            System.out.println(data3);
        }
    }
}

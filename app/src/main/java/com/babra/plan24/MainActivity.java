package com.babra.plan24;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    TextView instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding XMLs by ids
        FloatingActionButton fab = findViewById(R.id.fab);
        task_list = findViewById(R.id.task_list);
        instruction = findViewById(R.id.instruction);
        detail_popup = new Dialog(MainActivity.this);
        //getting all data in recyclerview
        get_all st = new get_all();
        st.start();
//        deltask dt = new deltask();
//        dt.start();
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
                        task_data input_data = new task_data();
                        input_data.setTask_name(popup_task_name.getText().toString().trim());
                        input_data.setHH(Integer.parseInt(popup_hh.getText().toString().trim()));
                        input_data.setMM(Integer.parseInt(popup_mm.getText().toString().trim()));
                        input_data.setSS(Integer.parseInt(popup_ss.getText().toString().trim()));
                        add_ask st1 = new add_ask(input_data);
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
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

//    class deltask extends Thread {
//        final task_data task = (task_data) getIntent().getSerializableExtra("task");
//        public void run(){
//            task_data_database db = Room.databaseBuilder(MainActivity.this, task_data_database.class, "database-name").build();
//            task_data_dao task_data_dao = db.task_data_dao();
//            task_data_dao.delete(task);
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        }
//    }

    class get_all extends Thread {
        public void run() {
            task_data_database db1 = Room.databaseBuilder(getApplicationContext(), task_data_database.class, "database-name").build();
            task_data_dao task_data_dao = db1.task_data_dao();
            List<task_data> all_data = task_data_dao.getAll();
            if (all_data.isEmpty())
                {
                    instruction.setVisibility(View.VISIBLE);
                }
            //RecyclerView Setup
            task_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            CustomAdapter adapter = new CustomAdapter(all_data);
            task_list.setAdapter(adapter);

        }
    }

}

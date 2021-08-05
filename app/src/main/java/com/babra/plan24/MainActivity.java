package com.babra.plan24;

import android.app.Dialog;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomAdapter.ItemClickListener {
    Dialog detail_popup;
    Button cancel_button, save_button;
    EditText popup_task_name;
    RecyclerView task_list;
    TextView instruction,delete_all, time_countdown;
    NumberPicker pickMM,pickHH,pickSS;
    List<task_data> all_data=new ArrayList<>();
    CustomAdapter adapter;
    task_data_dao task_data_dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //finding XMLs by ids
        FloatingActionButton fab = findViewById(R.id.fab);
        task_list = findViewById(R.id.task_list);
        instruction = findViewById(R.id.instruction);
        time_countdown=findViewById(R.id.time_countdown);
        delete_all=findViewById(R.id.delete_all);
        detail_popup = new Dialog(MainActivity.this);

        get_all st1 = new get_all();
        st1.start();

        task_list.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CustomAdapter(this, all_data);
        task_list.setAdapter(adapter);
        adapter.setClickListener(this);


        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyItemRangeRemoved(0,all_data.size());
                all_data.clear();
                delete_all st1= new delete_all();
                st1.start();
                Toast toast =Toast.makeText(MainActivity.this, "All Tasks Deleted Successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 150);
                toast.show();
                delete_all.setVisibility(View.GONE);
                instruction.setVisibility(View.VISIBLE);
            }
        });

        //Popup functionality and floating buttons
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detail_popup.setContentView(R.layout.detail_popup);
                popup_task_name=detail_popup.findViewById(R.id.popup_task_name);
                pickHH = detail_popup.findViewById(R.id.pickHH);
                pickHH.setMaxValue(23);
                pickHH.setMinValue(0);
                pickHH.setValue(0);
                pickHH.setOnValueChangedListener((numberPicker, i, i1) -> pickHH.setValue(i1));
                pickMM = detail_popup.findViewById(R.id.pickMM);
                pickMM.setMaxValue(59);
                pickMM.setMinValue(0);
                pickMM.setValue(0);
                pickMM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        pickMM.setValue(i1);
                    }
                });
                pickSS = detail_popup.findViewById(R.id.pickSS);
                pickSS.setMaxValue(59);
                pickSS.setMinValue(0);
                pickSS.setValue(0);
                pickSS.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        pickSS.setValue(i1);
                    }
                });
                cancel_button = detail_popup.findViewById(R.id.cancel_button);
                save_button = detail_popup.findViewById(R.id.save_button);
                Window window = detail_popup.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);
                detail_popup.show();
                cancel_button.setOnClickListener(view1 -> detail_popup.dismiss());
                save_button.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        //Room database setup
                        task_data input_data = new task_data();
                        input_data.setTask_name(popup_task_name.getText().toString().trim());
                        input_data.setHH(pickHH.getValue());
                        input_data.setMM(pickMM.getValue());
                        input_data.setSS(pickSS.getValue());
                        adapter.notifyItemInserted(all_data.size());
                        all_data.add(input_data);
                        if(seconds_to_plan()<0){

                            all_data.remove(all_data.size()-1);
                            adapter.notifyItemRemoved(all_data.size()-1);
                            Toast.makeText(getApplicationContext(), "Only "+time_to_plan()+" remaining", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            detail_popup.dismiss();
                            if(!all_data.isEmpty())
                            {
                                instruction.setVisibility(View.GONE);
                                delete_all.setVisibility(View.VISIBLE);
                            }
                            add_task st1 = new add_task(input_data);
                            st1.start();
                            Toast toast = Toast.makeText(MainActivity.this, "Task Added Successfully", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 150);
                            toast.show();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast toast =Toast.makeText(this,"Task Deleted Successfully", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 150);
        toast.show();
        task_data del=all_data.get(position);
        all_data.remove(position);
        adapter.notifyItemRemoved(position);
        if(all_data.isEmpty())
        {
            delete_all.setVisibility(View.GONE);
            instruction.setVisibility(View.VISIBLE);
        }
        del_task st1 = new del_task(del);
        st1.start();


    }

    int seconds_to_plan(){
        int seconds = 0;
        for(int i=0;i<all_data.size();i++) {
            seconds = seconds + (all_data.get(i).getHH() * 3600) + (all_data.get(i).getMM() * 60) + (all_data.get(i).getSS());
        }
        seconds=86400-seconds;
        return seconds;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    String time_to_plan(){
        int seconds = 0,HH,MM, SS;
        seconds=seconds_to_plan();
        SS = seconds % 60;
        HH = seconds / 60;
        MM = HH % 60;
        HH = HH / 60;
        DecimalFormat formatter = new DecimalFormat("00");
        return formatter.format(HH)+":"+formatter.format(MM)+":"+formatter.format(SS);
    }



    class get_all extends Thread {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            List <task_data> data;
            task_data_database db1 = Room.databaseBuilder(getApplicationContext(), task_data_database.class, "Plan24data").build();
            task_data_dao = db1.task_data_dao();
            data= task_data_dao.getAll();
            all_data.addAll(data);
            if(all_data.isEmpty())
            {
                delete_all.setVisibility(View.GONE);
                instruction.setVisibility(View.VISIBLE);
            }
            data.clear();
            time_countdown.setText(time_to_plan());
        }
    }

    class add_task extends Thread {
        task_data data3;
        add_task(task_data data3) {
            this.data3=data3;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            task_data_dao.insert(data3);
            time_countdown.setText(time_to_plan());

        }
    }

    class delete_all extends Thread {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            task_data_dao.deleteAll();
            time_countdown.setText(time_to_plan());

        }
    }

    class del_task extends Thread {
        task_data data3;
        del_task(task_data data3) {
            this.data3=data3;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            task_data_dao.delete(data3);
            time_countdown.setText(time_to_plan());
        }
    }


}

//TO DO
//Refresh after adding or deletion
//Swipe features if possible
//UI Enhancement
//Order change on long press Recycler View

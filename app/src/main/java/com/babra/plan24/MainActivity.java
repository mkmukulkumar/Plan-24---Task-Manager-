package com.babra.plan24;

import android.app.Dialog;
import android.content.SharedPreferences;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    Dialog detail_popup;
    EditText task_name;
    Button cancel_button;
    Button save_button;
    RecyclerView task_list;
    String[] data={"sajc","sacbsa","dfadsnf"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences Task_data = getPreferences( MODE_PRIVATE);
        FloatingActionButton fab = findViewById(R.id.fab);
        TextView instruction=findViewById(R.id.instruction);
        task_list=findViewById(R.id.task_list);
        detail_popup = new Dialog(MainActivity.this);
        task_list.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapter adapter=new CustomAdapter(data);
        task_list.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                detail_popup.setContentView(R.layout.detail_popup);
                task_name = detail_popup.findViewById(R.id.task_name);
                cancel_button = detail_popup.findViewById(R.id.cancel_button);
                save_button=detail_popup.findViewById(R.id.save_button);
                Window window = detail_popup.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);
                detail_popup.show();
                cancel_button.setOnClickListener(view1 -> detail_popup.dismiss());
                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor task_editor = Task_data.edit();
                        task_editor.putString(String.valueOf(R.string.task_name), task_name.getText().toString() );
                        task_editor.apply();
                        detail_popup.dismiss();
                    }
                });

            }
        });
        SharedPreferences sh = getPreferences(MODE_PRIVATE);
        String s1 = sh.getString(String.valueOf(R.string.task_name), "");
        instruction.setText(s1);
    }
}
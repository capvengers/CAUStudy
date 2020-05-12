package com.example.caustudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.caustudy.jesnk.JMainActivity;

public class StudyMenuActivity extends AppCompatActivity {
    private String study_key, study_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_menu);
        Intent intent = getIntent();
        study_key = intent.getStringExtra("study_key");
        study_name = intent.getStringExtra("study_name");


        Button notice_btn = (Button)findViewById(R.id.notice_btn);

        Button setting_btn = (Button)findViewById(R.id.setting_btn);

        notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudyMenuActivity.this, JMainActivity.class);
                intent.putExtra("study_key", study_key );
                intent.putExtra("study_name", study_name );
                startActivity(intent);
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudyMenuActivity.this, SettingStudyActivity.class);
                intent.putExtra("study_key", study_key );
                intent.putExtra("study_name", study_name );
                startActivity(intent);
            }
        });

    }
}

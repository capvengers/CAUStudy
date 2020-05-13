package com.example.caustudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;

import com.example.caustudy.jesnk.JMainActivity;

public class StudyMenuActivity extends AppCompatActivity {
    private String study_key, study_name, leader_email;
    String l_cate, s_cate, number;
    TextView title;
    private FirebaseUser userAuth;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference studyRef = FirebaseDatabase.getInstance().getReference("StudyList");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_menu);
        title = findViewById(R.id.textView_studyname);
        Intent intent = getIntent();
        study_key = intent.getStringExtra("study_key");
        study_name = intent.getStringExtra("study_name");
        title.setText(study_name);

//merge part
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();

        StringTokenizer tokens = new StringTokenizer(study_key, ":");
        l_cate = tokens.nextToken();
        s_cate = tokens.nextToken();
        number = tokens.nextToken();


        studyRef.child(l_cate).child(s_cate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (study_name.equals(ds.child("study_name").getValue().toString())) {
                        leader_email = ds.child("leader_email").getValue().toString();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        Button notice_btn = (Button)findViewById(R.id.notice_btn);
// merge part end

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
                if (leader_email.equals(userAuth.getEmail())) {
                    Intent intent = new Intent(StudyMenuActivity.this, SettingStudyActivity.class);
                    intent.putExtra("study_key", study_key);
                    intent.putExtra("study_name", study_name);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "스터디 관리에 접근할 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

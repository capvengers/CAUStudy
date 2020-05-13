package com.example.caustudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SettingStudyActivity extends AppCompatActivity {

    List<String> listName = new ArrayList<>();
    List<String> listEmail = new ArrayList<>();

    RecyclerView recyclerView;
    private ApplyAdapter adapter;
    private String study_key, study_name, email, name, id;
    String l_cate, s_cate, number;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("StudyList");
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("사용자");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_study);

        Intent intent = getIntent();
        study_key = intent.getStringExtra("study_key");
        study_name = intent.getStringExtra("study_name");

        StringTokenizer tokens = new StringTokenizer(study_key, ":");
        l_cate = tokens.nextToken();
        s_cate = tokens.nextToken();
        number = tokens.nextToken();

        recyclerView = findViewById(R.id.apply_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SettingStudyActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ApplyAdapter();
        adapter.setOnItemClickListener(new ApplyAdapter.OnItemClickListener() {

            @Override
            public void onAcceptClick(View v, int position) {
                String get_email = listEmail.get(position);
                StringTokenizer id_token = new StringTokenizer(get_email, "@");
                String id = id_token.nextToken();
                userRef.child(id).child("taken_study").child(l_cate + ":"+ s_cate + ":" + number).setValue(study_name);
                myRef.child(l_cate).child(s_cate).child(number).child("applier_list").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(SettingStudyActivity.this, "삭제 성공", Toast.LENGTH_LONG).show();
                    }
                });
                // Test
                myRef.child(l_cate).child(s_cate).child(number).child("member_list").child(id).setValue(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(SettingStudyActivity.this, "삭제 성공", Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onDeleteClick(View v, int position) {
                String get_email = listEmail.get(position);
                StringTokenizer id_token = new StringTokenizer(get_email, "@");
                String id = id_token.nextToken();
                myRef.child(l_cate).child(s_cate).child(number).child("applier_list").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(SettingStudyActivity.this, "삭제 성공", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        myRef.child(l_cate).child(s_cate).child(number).child("applier_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    listEmail.clear();
                    listName.clear();
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    email = ds.child("email").getValue().toString();
                    name = ds.child("username").getValue().toString();
                    listEmail.add(email);
                    listName.add(name);

                    Item item = new Item();
                    item.setName(name);
                    item.setEmail(email);
                    adapter.addItem(item);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        /*
        for (int i = 0; i < listName.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Item item = new Item();
            item.setName(listName.get(i));
            item.setEmail(listEmail.get(i));
            adapter.addItem(item);
        }
        */
    }


}

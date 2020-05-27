package com.example.caustudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class SettingStudyActivity extends AppCompatActivity {

    List<String> listName = new ArrayList<>();
    List<String> listEmail = new ArrayList<>();
    List<String> list_L = new ArrayList<>();
    List<String> list_S = new ArrayList<>();

    Switch fin;
    RecyclerView recyclerView;
    private ApplyAdapter adapter;
    private String study_key, study_name, email, name, l_dept, s_dept;
    DatabaseReference studyRef = FirebaseDatabase.getInstance().getReference("Study");
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("사용자");

    private Button setNextSchedule;
    private TextView next_location_view, next_time_view;
    String next_location;
    String next_time;
    public static Context mContext;



    class SwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
                studyRef.child(study_key).child("apply_status").setValue(1);
            else
                studyRef.child(study_key).child("apply_status").removeValue();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_study);
        Intent intent = getIntent();
        study_key = intent.getStringExtra("study_key");
        study_name = intent.getStringExtra("study_name");
        mContext = this;


        // 다음 모임 시간, 장소 텍스트뷰 업데이트



        next_location_view = findViewById(R.id.nextLocationView);
        next_time_view = findViewById(R.id.nextTimeView);
        next_location = "설정된 장소가 없습니다.";
        next_time = "설정된 시간이 없습니다.";

        refresh_nextSchedule_view();



        setNextSchedule = findViewById(R.id.schedule_setting_btn);
        setNextSchedule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Dialog_nextScheduleSetting dialog_nextScheduleSet = new Dialog_nextScheduleSetting(SettingStudyActivity.this);
                dialog_nextScheduleSet.callFunction(study_key);

            }
        });


        recyclerView = findViewById(R.id.apply_view);
        fin = findViewById(R.id.switch_fin);
        fin.setOnCheckedChangeListener(new SwitchListener());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SettingStudyActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ApplyAdapter();
        adapter.setOnItemClickListener(new ApplyAdapter.OnItemClickListener() {

            @Override
            public void onAcceptClick(View v, int position) {
                String get_email = listEmail.get(position);
                StringTokenizer id_token = new StringTokenizer(get_email, "@");
                String id = id_token.nextToken();
                userRef.child(id).child("taken_study").child(study_key).setValue(study_name);
                studyRef.child(study_key).child("applier_list").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(SettingStudyActivity.this, "삭제 성공", Toast.LENGTH_LONG).show();
                    }
                });
                // Test
                studyRef.child(study_key).child("member_list").child(id).setValue(email).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                studyRef.child(study_key).child("applier_list").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(SettingStudyActivity.this, "삭제 성공", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        studyRef.child(study_key).child("applier_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    listEmail.clear();
                    listName.clear();
                    list_L.clear();
                    list_S.clear();
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    email = ds.child("email").getValue().toString();
                    name = ds.child("username").getValue().toString();
                    l_dept = ds.child("L_deptname").getValue().toString();
                    s_dept = ds.child("S_deptname").getValue().toString();
                    listEmail.add(email);
                    listName.add(name);
                    list_L.add(l_dept);
                    list_S.add(s_dept);

                    Item item = new Item();
                    item.setName(name);
                    item.setEmail(email);
                    item.setL_dept(l_dept);
                    item.setS_dept(s_dept);
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
    public void refresh_nextSchedule_view() {

        studyRef.child(study_key).child("next_schedule").child("next_location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                Log.d("ds.getKey() test :",ds.getKey());
                if (ds.getValue().toString() != null) {
                    next_location_view.setText(ds.getValue().toString());
                    Log.d("locationsteted", "Succes");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        studyRef.child(study_key).child("next_schedule").child("next_time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                Log.d("ds.getKey() test :",ds.getKey());
                if (ds.getValue().toString() != null) {
                    next_time_view.setText(ds.getValue().toString());
                    Log.d("timesteted", "Succes");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}

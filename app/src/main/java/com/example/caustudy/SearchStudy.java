package com.example.caustudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchStudy extends AppCompatActivity {
    private RecyclerAdapter adapter;
    Spinner small_category, large_category;
    String l_cate, s_cate;
    Button btn;
    ArrayAdapter<CharSequence> adapter_large, adapter_small; //어댑터를 선언

    List<String> listTitle = new ArrayList<>();
    List<String> listPeriod = new ArrayList<>();
    List<String> listTime = new ArrayList<>();
    List<String> listLeader = new ArrayList<>();
    List<String> listOrg = new ArrayList<>();
    List<String> listInfo = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_study);
        btn = (Button)findViewById(R.id.search_btn);
        large_category=(Spinner)findViewById(R.id.large_category);
        small_category=(Spinner)findViewById(R.id.small_category);
        adapter_large = ArrayAdapter.createFromResource(this, R.array.spinner_do, android.R.layout.simple_spinner_dropdown_item);
        large_category.setAdapter(adapter_large);
        large_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter_large.getItem(i).equals("전공")) {
                    l_cate = "전공";
                    adapter_small = ArrayAdapter.createFromResource(SearchStudy.this, R.array.spinner_do_major, android.R.layout.simple_spinner_dropdown_item);
                    adapter_small.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    small_category.setAdapter(adapter_small);
                    small_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            s_cate = adapter_small.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } else if (adapter_large.getItem(i).equals("어학")) {
                    l_cate = "어학";
                    adapter_small = ArrayAdapter.createFromResource(SearchStudy.this, R.array.spinner_do_language, android.R.layout.simple_spinner_dropdown_item);
                    adapter_small.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    small_category.setAdapter(adapter_small);
                    small_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            s_cate = adapter_small.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } else if (adapter_large.getItem(i).equals("시험")) {
                    l_cate = "시험";
                    adapter_small = ArrayAdapter.createFromResource(SearchStudy.this, R.array.spinner_do_exam, android.R.layout.simple_spinner_dropdown_item);
                    adapter_small.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    small_category.setAdapter(adapter_small);
                    small_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            s_cate = adapter_small.getItem(i).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        init();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
     });
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.mt_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void getData() {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("StudyList");
        myRef.child(l_cate).child(s_cate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    listTitle.clear();
                    listPeriod.clear();
                    listTime.clear();
                    listLeader.clear();
                    listOrg.clear();
                    listInfo.clear();
                }
                for (DataSnapshot meetingName : dataSnapshot.getChildren()) {
                    String title = meetingName.child("study_name").getValue().toString();
                    String s_time = meetingName.child("s_period").getValue().toString();
                    String e_time = meetingName.child("e_period").getValue().toString();
                    String period = s_time + " ~ " + e_time;
                    String day = meetingName.child("study_day").getValue().toString();
                    String time = meetingName.child("study_time").getValue().toString();
                    day = day + " " + time;
                    String leader = meetingName.child("leader_email").getValue().toString();
                    String org = meetingName.child("organization").getValue().toString();
                    String info = meetingName.child("info").getValue().toString();
                    Log.v("리스트", "title"+ title);
                    listTitle.add(title);
                    listPeriod.add(period);
                    listTime.add(day);
                    listLeader.add(leader);
                    listOrg.add(org);
                    listInfo.add(info);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Log.v("리스트", "title"+listTitle.toString());
        Log.v("리스트", "period"+listPeriod.toString());
        Log.v("리스트", "title"+listTitle.toString());

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setPeriod(listPeriod.get(i));
            data.setTime(listTime.get(i));
            data.setLeader(listLeader.get(i));
            data.setOrg(listOrg.get(i));
            data.setInfo(listInfo.get(i));

            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

}

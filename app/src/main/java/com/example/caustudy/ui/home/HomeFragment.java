package com.example.caustudy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caustudy.R;
import com.example.caustudy.StudyDetailActivity;
import com.example.caustudy.ui.MyStudy.MyStudyViewModel;
import com.example.caustudy.ui.MyStudy.MyStudy_Adapter;
import com.example.caustudy.ui.MyStudy.MyStudy_SingerItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HomeFragment extends Fragment {

    private HomeStudyViewModel homeViewModel;
    String study_list_raw;
    HomeRecyclerAdapter singerAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser userAuth = mAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference_user = firebaseDatabase.getReference("사용자");
    DatabaseReference datebaseReference_study = firebaseDatabase.getReference("Study");
    StringTokenizer stringTokenizer = new StringTokenizer(userAuth.getEmail(), "@");
    String user_id = stringTokenizer.nextToken();
    List<String> study_list = new ArrayList<>();
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeStudyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = root.findViewById(R.id.mt_list);



        singerAdapter = new HomeRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.setAdapter(singerAdapter);

        // 스터디 리스트 목록부터 받아와야함(순서 중요)
        check_mystudy_list();

        Log.d("study_list check :",study_list.toString());



        return root;
    }



    void check_mystudy_list(){
        if (userAuth != null) {
            // 내가 가입한 스터디 목록 탐색
            databaseReference_user.child(user_id).child("taken_study").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        study_list_raw = ds.getKey();
                        // 리스트에 값이 저장이 안되는거 같아서 일단 때려박았어..
                        study_list.add(study_list_raw); // 키 값 저장 (format = L_cate:S_cate:001)

                        datebaseReference_study.addListenerForSingleValueEvent(new ValueEventListener() {
                            String num = study_list_raw;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.getKey().equals(num)) {
                                        String title = ds.child("study_name").getValue().toString();
                                        String s_period = ds.child("s_period").getValue().toString();   // 시작일
                                        String e_period = ds.child("e_period").getValue().toString();   // 종료일
                                        String day = ds.child("study_day").getValue().toString();  // 요일
                                        String time = ds.child("study_time").getValue().toString();  // 시간
                                        String org = ds.child("organization").getValue().toString();  // 소속
                                        Log.d("HomeFragment:addItem",title);
                                        singerAdapter.addItem(new HomeData(title, "test", "test", "test","test","test"));
                                        singerAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled (@NonNull DatabaseError databaseError){
                            }
                        });
                    }
                }
                @Override
                public void onCancelled (@NonNull DatabaseError databaseError) {

                }
            });
            //get_study_info();
        }
    }
    /*
    public void getData(RecyclerView rv) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter();
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position){
                Intent intent = new Intent(getActivity(), StudyDetailActivity.class);
                intent.putExtra("study_name",listTitle.get(position) );
                intent.putExtra("l_cate",l_cate);
                intent.putExtra("s_cate",s_cate);
                startActivity(intent);
            }
        });
        rv.setAdapter(adapter);

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
    */
}
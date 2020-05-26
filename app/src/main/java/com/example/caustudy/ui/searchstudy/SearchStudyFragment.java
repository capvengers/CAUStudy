package com.example.caustudy.ui.searchstudy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caustudy.MainActivity;
import com.example.caustudy.MakeStudyActivity;
import com.example.caustudy.R;
import com.example.caustudy.StudyDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class SearchStudyFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private RecyclerAdapter adapter;
    Spinner small_category, large_category;
    String l_cate, s_cate;
    Button btn;
    Button create_btn;
    Button serach_btn2;
    AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> fill_list;
    private String tag_search;
    ArrayAdapter<CharSequence> adapter_large, adapter_small; //어댑터를 선언
    DatabaseReference tagRef = FirebaseDatabase.getInstance().getReference("Hashtags");
    DatabaseReference studyRef = FirebaseDatabase.getInstance().getReference("Study");


    List<String> listTitle = new ArrayList<>();
    List<String> listPeriod = new ArrayList<>();
    List<String> listTime = new ArrayList<>();
    List<String> listLeader = new ArrayList<>();
    List<String> listOrg = new ArrayList<>();
    List<String> listInfo = new ArrayList<>();

    List<String> list_match_tag = new ArrayList<>();

    RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_searchstudy, container, false);

        //자동완성
        autoCompleteTextView = (AutoCompleteTextView) root.findViewById(R.id.autoCompleteTextView);
        fill_list = new ArrayList<String>();
        setting_fill_list();
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, fill_list));

        btn = (Button)root.findViewById(R.id.search_btn);
        serach_btn2 = (Button)root.findViewById(R.id.search_btn2);

        create_btn = (Button)root.findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MakeStudyActivity.class);
                startActivity(intent);
            }
        });

        large_category=(Spinner)root.findViewById(R.id.large_category);
        small_category=(Spinner)root.findViewById(R.id.small_category);
        recyclerView = root.findViewById(R.id.mt_list);
        adapter_large = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_do, android.R.layout.simple_spinner_dropdown_item);
        large_category.setAdapter(adapter_large);


        large_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter_large.getItem(i).equals("전공")) {
                    l_cate = "전공";
                    adapter_small = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_do_major, android.R.layout.simple_spinner_dropdown_item);
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
                    adapter_small = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_do_language, android.R.layout.simple_spinner_dropdown_item);
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
                    adapter_small = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_do_exam, android.R.layout.simple_spinner_dropdown_item);
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(recyclerView);
            }
        });

        serach_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData_2(recyclerView);
            }
        });

        return root;
    }

    // 자동완성 관련, 추천단어 추
    private void setting_fill_list() {
        DatabaseReference hashTagRef = FirebaseDatabase.getInstance().getReference("Hashtags");
        hashTagRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    fill_list.clear();
                }
                for (DataSnapshot tag : dataSnapshot.getChildren()) {
                    String tag_name = tag.getKey();
                    fill_list.add(tag_name);
                    Log.d("setting_fill_list added",tag_name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

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


    public void getData_2(RecyclerView rv) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter();
        tag_search = autoCompleteTextView.getText().toString();


        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position){
                Intent intent = new Intent(getActivity(), StudyDetailActivity.class);
                intent.putExtra("study_name",listTitle.get(position) );
                intent.putExtra("l_cate",l_cate);
                intent.putExtra("s_cate",s_cate);
                intent.putExtra("tag",tag_search);
                startActivity(intent);
            }
        });

        rv.setAdapter(adapter);






        // 태그와 일치하는 스터디 넘버를 리스트에 넣음
        tagRef.child(tag_search).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    list_match_tag.clear();
                }
                for (DataSnapshot studyNum : dataSnapshot.getChildren()) {
                    list_match_tag.add(studyNum.getKey());




                    Log.d("list_match_tag added",studyNum.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        studyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    listTitle.clear();
                    listPeriod.clear();
                    listTime.clear();
                    listLeader.clear();
                    listOrg.clear();
                    listInfo.clear();
                }
                for (DataSnapshot study : dataSnapshot.getChildren()) {
                    if (list_match_tag.contains(study.getKey())) {
                        String title = study.child("study_name").getValue().toString();
                        String s_time = study.child("s_period").getValue().toString();
                        String e_time = study.child("e_period").getValue().toString();
                        String period = s_time + " ~ " + e_time;
                        String day = study.child("study_day").getValue().toString();
                        String time = study.child("study_time").getValue().toString();
                        day = day + " " + time;
                        String leader = study.child("leader_email").getValue().toString();
                        String org = study.child("organization").getValue().toString();
                        String info = study.child("info").getValue().toString();
                        Log.v("리스트", "title"+ title);
                        listTitle.add(title);
                        listPeriod.add(period);
                        listTime.add(day);
                        listLeader.add(leader);
                        listOrg.add(org);
                        listInfo.add(info);


                    }

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


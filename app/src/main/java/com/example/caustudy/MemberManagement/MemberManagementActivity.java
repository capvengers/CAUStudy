package com.example.caustudy.MemberManagement;

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

import com.example.caustudy.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MemberManagementActivity extends AppCompatActivity {


    List<String> listName = new ArrayList<>();
    List<String> listEmail = new ArrayList<>();
    List<String> list_L = new ArrayList<>();
    List<String> list_S = new ArrayList<>();

    List<String> member_email = new ArrayList<>();
    List<String> member_name = new ArrayList<>();
    List<String> member_list_L = new ArrayList<>();
    List<String> member_list_S = new ArrayList<>();


    Switch fin;
    Switch fin2;
    RecyclerView apply_recyclerView;
    RecyclerView member_recyclerView;
    private MemberViewAdapter member_view_adapter;
    private ApplyViewAdapter apply_view_adapter;
    private String study_key, study_name, email, name, l_dept, s_dept;

    private Button set_apply_limit;
    private TextView applier_text_view;
    final String[] applier_limit = new String[1];

    DatabaseReference studyRef = FirebaseDatabase.getInstance().getReference("Study");
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("사용자");

    public static Context mContext;


    class ApplyableSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
                studyRef.child(study_key).child("apply_status").setValue(1);
            else
                studyRef.child(study_key).child("apply_status").removeValue();
        }
    }
    class ReApplyableSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
                studyRef.child(study_key).child("reApply_status").setValue(1);
            else
                studyRef.child(study_key).child("reApply_status").removeValue();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_management);
        Intent intent = getIntent();
        study_key = intent.getStringExtra("study_key");
        study_name = intent.getStringExtra("study_name");
        applier_text_view = findViewById(R.id.textView_applier_limit_value);


        set_apply_limit = findViewById(R.id.btn_set_applyNum);
        set_apply_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_NumApply dialog_numApply = new Dialog_NumApply(MemberManagementActivity.this);
                dialog_numApply.callFunction(study_key);
                update_view();

            }
        });

        studyRef.child(study_key).child("applier_limit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.getValue() != null) {
                    applier_limit[0] = ds.getValue().toString();
                    applier_text_view.setText(applier_limit[0] + " 명");
                }
                else {
                    applier_limit[0] = "-";
                    applier_text_view.setText(applier_limit[0] + " 명");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        mContext = this;
        get_switch_status();
        //스터디 모집 상태 가져오기
        fin = findViewById(R.id.switch_fin_apply);
        fin.setOnCheckedChangeListener(new ApplyableSwitchListener());
        fin2 = findViewById(R.id.switch_fin_reapply);
        fin2.setOnCheckedChangeListener(new ReApplyableSwitchListener());

        apply_recyclerView = findViewById(R.id.apply_view);
        member_recyclerView = findViewById(R.id.member_view);
        apply_view_adapter = new ApplyViewAdapter();
        apply_view_adapter.setStudyKey(study_key);

        member_view_adapter = new MemberViewAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MemberManagementActivity.this);
        apply_recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(MemberManagementActivity.this);
        member_recyclerView.setLayoutManager(linearLayoutManager2);

        apply_view_adapter.setOnItemClickListener(new ApplyViewAdapter.OnItemClickListener() {

            @Override
            public void onAcceptClick(View v, int position) {
                String get_email = listEmail.get(position);
                StringTokenizer id_token = new StringTokenizer(get_email, "@");

                // Final로 했는데, 괜찮으려
                final String id = id_token.nextToken();
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
                // Add Hashtag History

                Log.d("study_history ","check");
                userRef.child(id).child("study_history").child(study_key).child("score").setValue("-");
                update_member_view();
                update_applier_view();
                studyRef.child(study_key).child("hashtag").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            final String[] tag_value = new String[1];
                            final int[] tag_value_int = new int[1];
                            final String tag_key = ds.getKey();
                            if (ds.getKey() != null) {
                                userRef.child(id).child("hashtag_history").child(tag_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            tag_value[0] = dataSnapshot.getValue().toString();

                                            Log.d("key and value", dataSnapshot.getKey() + " " + tag_value[0]);
                                            tag_value_int[0] = Integer.parseInt(tag_value[0]);
                                            tag_value_int[0] += 1;
                                            Log.d("tag_value_int",Integer.toString(tag_value_int[0]));


                                        } else {
                                            tag_value_int[0] = 1;
                                        }
                                        userRef.child(id).child("hashtag_history").child(tag_key).setValue(Integer.toString(tag_value_int[0]));

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                Log.d("DB insert test ",tag_key + " " + Integer.toString(tag_value_int[0]));

                            }
                            // check

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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
                String declined_username = id;
                String declined_email = get_email;
                studyRef.child(study_key).child("declined_list").child(declined_username).setValue(declined_email);

            }
        });

        apply_recyclerView.setAdapter(apply_view_adapter);
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
                    Log.d("???",email);
                    listName.add(name);
                    list_L.add(l_dept);
                    list_S.add(s_dept);

                    ApplyViewItem applyViewItem = new ApplyViewItem();
                    applyViewItem.setName(name);
                    applyViewItem.setEmail(email);
                    applyViewItem.setL_dept(l_dept);
                    applyViewItem.setS_dept(s_dept);
                    apply_view_adapter.addItem(applyViewItem);
                    apply_view_adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        member_recyclerView.setAdapter(member_view_adapter);
        studyRef.child(study_key).child("member_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    member_email.clear();
                    member_name.clear();
                    member_list_L.clear();
                    member_list_S.clear();
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //member_email.add(ds.getValue().toString());
                    String user_email = ds.getValue().toString();
                    StringTokenizer id_token = new StringTokenizer(user_email, "@");
                    String user_id = id_token.nextToken();
                    final String[] name = new String[1];
                    userRef.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            if (ds.getValue() != null) {
                                name[0] = ds.child("username").getValue().toString();
                                String member_l_dept = ds.child("L_deptname").getValue().toString();
                                String member_s_dept = ds.child("S_deptname").getValue().toString();

                                member_email.add(user_email);
                                member_name.add(name[0]);
                                member_list_L.add(member_l_dept);
                                member_list_S.add(member_s_dept);
                                MemberViewItem memberViewItem = new MemberViewItem();
                                memberViewItem.setName(name[0]);
                                memberViewItem.setEmail(user_email);
                                memberViewItem.setL_dept(member_l_dept);
                                memberViewItem.setS_dept(member_s_dept);

                                member_view_adapter.addItem(memberViewItem);
                                // 아래 코드를 밖으로 뺴면 안되더
                                member_view_adapter.notifyDataSetChanged();

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void update_member_view() {
        studyRef.child(study_key).child("member_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    member_email.clear();
                    member_name.clear();
                    member_list_L.clear();
                    member_list_S.clear();
                    member_view_adapter.clearItem();
                }

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //member_email.add(ds.getValue().toString());
                    String user_email = ds.getValue().toString();
                    StringTokenizer id_token = new StringTokenizer(user_email, "@");
                    String user_id = id_token.nextToken();
                    final String[] name = new String[1];
                    userRef.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            if (ds.getValue() != null) {
                                name[0] = ds.child("username").getValue().toString();
                                String member_l_dept = ds.child("L_deptname").getValue().toString();
                                String member_s_dept = ds.child("S_deptname").getValue().toString();

                                member_email.add(user_email);
                                member_name.add(name[0]);
                                member_list_L.add(member_l_dept);
                                member_list_S.add(member_s_dept);
                                MemberViewItem memberViewItem = new MemberViewItem();
                                memberViewItem.setName(name[0]);
                                memberViewItem.setEmail(user_email);
                                memberViewItem.setL_dept(member_l_dept);
                                memberViewItem.setS_dept(member_s_dept);

                                member_view_adapter.addItem(memberViewItem);
                                // 아래 코드를 밖으로 뺴면 안되더
                                member_view_adapter.notifyDataSetChanged();

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void get_switch_status() {
        studyRef.child(study_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("apply_status").getValue() != null) {
                    fin.setChecked(true);
                }
                else{
                    fin.setChecked(false);
                }
                if (dataSnapshot.child("reApply_status").getValue() != null ) {
                    fin2.setChecked(true);
                }
                else {
                    fin2.setChecked(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    public void update_applier_view() {
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
                    Log.d("???",email);
                    listName.add(name);
                    list_L.add(l_dept);
                    list_S.add(s_dept);

                    ApplyViewItem applyViewItem = new ApplyViewItem();
                    applyViewItem.setName(name);
                    applyViewItem.setEmail(email);
                    applyViewItem.setL_dept(l_dept);
                    applyViewItem.setS_dept(s_dept);
                    apply_view_adapter.addItem(applyViewItem);
                    apply_view_adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void update_view() {
        studyRef.child(study_key).child("applier_limit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.getValue() != null) {
                    applier_limit[0] = ds.getValue().toString();
                    applier_text_view.setText(applier_limit[0] + " 명");
                }
                else {
                    applier_limit[0] = "-";
                    applier_text_view.setText(applier_limit[0] + " 명");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}

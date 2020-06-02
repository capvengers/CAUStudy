package com.example.caustudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caustudy.Models.Post;
import com.example.caustudy.ui.searchstudy.SearchStudyFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;

public class StudyDetailActivity extends AppCompatActivity {
    long count = 0;
    private String study_name;
    private String leader_email, auth_id;
    private String key;
    User applier;
    TextView tv_title, tv_period, tv_category, tv_time, tv_leader, tv_info, tv_org;
    String title, period, category, time, leader, info, org;
    private String auth_name, auth_email, auth_l_cate, auth_s_cate;
    Button apply, back;
    private FirebaseUser userAuth;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private PopupWindow mPopupWindow ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference studyRef = database.getReference("Study");
    DatabaseReference databaseReference_user = database.getReference("사용자");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_detail);

        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_period = (TextView)findViewById(R.id.tv_period);
        tv_category = (TextView)findViewById(R.id.tv_category);
        tv_leader = (TextView)findViewById(R.id.tv_leader);
        tv_time= (TextView)findViewById(R.id.tv_time);
        tv_info = (TextView)findViewById(R.id.tv_info);
        tv_org = (TextView)findViewById(R.id.tv_org);


        apply = (Button)findViewById(R.id.apply_btn);
        back = (Button)findViewById(R.id.back_btn);
        Intent intent = getIntent();
        study_name = intent.getStringExtra("study_name");


        get_study_info();
        set_view();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.apply_pop_up, null);
                mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setFocusable(true);
                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                final Button okButton = (Button) popupView.findViewById(R.id.okButton);
                final Button cancelButton = (Button) popupView.findViewById(R.id.cancelButton);
                final TextView tv = (TextView)popupView.findViewById(R.id.apply_msg);
                String message = study_name + "에 스터디 신청을 하시겠습니까?";
                tv.setText(message);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        apply_study();
                        mPopupWindow.dismiss();
                        finish();
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "스터디 신청 취소", Toast.LENGTH_SHORT).show();
                        mPopupWindow.dismiss();
                    }
                });
            }
        });

    }
    void get_user_info(){
        if (userAuth != null) {
            databaseReference_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (userAuth.getEmail().equals(ds.child("email").getValue())) {
                            //User(String email, String username, String L_deptname, String S_deptname)
                            auth_name = ds.child("username").getValue().toString();
                            auth_email = ds.child("email").getValue().toString();
                            auth_l_cate = ds.child("L_deptname").getValue().toString();
                            auth_s_cate = ds.child("S_deptname").getValue().toString();
                            applier = new User(auth_email, auth_name, auth_l_cate, auth_s_cate);
                            studyRef.child(key).child("applier_list").child(auth_id).setValue(applier);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    void apply_study(){
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        String email = userAuth.getEmail();
        StringTokenizer stringTokenizer = new StringTokenizer(email, "@");
        auth_id = stringTokenizer.nextToken(); //@ 분리

        if (userAuth != null) {
            studyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (study_name.equals(ds.child("study_name").getValue().toString())) {
                            // 스터디 키 값 받아서 데이터 올리기
                            StringTokenizer stringTokenizer_leader = new StringTokenizer(leader_email, "@");
                            String leader_id = stringTokenizer_leader.nextToken();
                            if (leader_id.equals(auth_id)){
                                Toast.makeText(getApplicationContext(), "자신이 만든 스터디는 신청할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                key = ds.getKey();
                                get_user_info();
                                Toast.makeText(getApplicationContext(), "스터디 신청 전송", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        } else {
                            //Toast.makeText(getApplicationContext(), "데이터베이스에 해당 스터디가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    void get_study_info(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Study");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (study_name.equals(ds.child("study_name").getValue().toString())) {
                        // 상세 데이터 가져오기
                        String L = ds.child("L_category").getValue().toString();
                        String S = ds.child("S_category").getValue().toString();
                        String e_day = ds.child("e_period").getValue().toString();
                        String s_day = ds.child("s_period").getValue().toString();
                        info = ds.child("info").getValue().toString();
                        leader_email = ds.child("leader_email").getValue().toString();

                        category = L + " / " + S;
                        period = s_day + " ~ " + e_day;
                        leader = ds.child("leader_email").getValue().toString();
                        org = ds.child("organization").getValue().toString();
                        String study_day = ds.child("study_day").getValue().toString();
                        String study_time = ds.child("study_time").getValue().toString();
                        time = "매주 " + study_day + " / " + study_time;
                        title = study_name;
                        tv_category.setText(category);
                        tv_title.setText(title);
                        tv_period.setText(period);
                        tv_time.setText(time);
                        tv_leader.setText(leader);
                        tv_org.setText(org);
                        tv_info.setText(info);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void set_view(){

    }
}


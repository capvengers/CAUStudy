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

public class StudyDetailActivity extends AppCompatActivity {
    long count = 0;
    private String study_name, l_cate, s_cate;
    TextView tv_name;
    Button apply, back;
    private FirebaseUser userAuth;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private PopupWindow mPopupWindow ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference studyRef = database.getReference("StudyList");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_detail);

        apply = (Button)findViewById(R.id.apply_btn);
        back = (Button)findViewById(R.id.back_btn);
        Intent intent = getIntent();
        study_name = intent.getStringExtra("study_name");
        l_cate = intent.getStringExtra("l_cate");
        s_cate = intent.getStringExtra("s_cate");

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
                        Toast.makeText(getApplicationContext(), "스터디 신청 전송", Toast.LENGTH_SHORT).show();
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
    void apply_study(){

        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();

        if (userAuth != null) {
            studyRef.child(l_cate).child(s_cate).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        count++;
                        if (study_name.equals(ds.child("study_name").getValue().toString())) {
                            // 스터디 키 값 받아서 데이터 올리기
                            String key = ds.getKey();
                            String email = userAuth.getEmail();
                            if (count >= 9) {
                                studyRef.child(l_cate).child(s_cate).child(key).child("applier_list").child("0" + (count + 1)).setValue(email);
                            } else {
                                studyRef.child(l_cate).child(s_cate).child(key).child("applier_list").child("00" + (count + 1)).setValue(email);
                            }
                            break;
                        } else {
                            Toast.makeText(getApplicationContext(), "데이터베이스에 해당 스터디가 없습니다.", Toast.LENGTH_SHORT).show();
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
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("StudyList");
        myRef.child(l_cate).child(s_cate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (study_name.equals(ds.child("study_name").getValue().toString())) {
                        // 상세 데이터 가져오기
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

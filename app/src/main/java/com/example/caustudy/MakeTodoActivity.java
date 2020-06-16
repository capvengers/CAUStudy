package com.example.caustudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MakeTodoActivity extends AppCompatActivity {
    DatabaseReference studyRef = FirebaseDatabase.getInstance().getReference("Study");
    String study_key;
    Button new_todo;
    List<String> listNum = new ArrayList<>();
    List<String> listTopic = new ArrayList<>();
    List<String> listTime = new ArrayList<>();
    TodoAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_todo);

        Intent intent = getIntent();
        study_key = intent.getStringExtra("study_key");
        new_todo = findViewById(R.id.new_todo_btn);
        recyclerView = findViewById(R.id.todo_list);

        new_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Todo dialog_todo = new Dialog_Todo(MakeTodoActivity.this);
                dialog_todo.callFunction(study_key);
            }
        });
        set_listview();

    }

    void set_listview(){
        adapter = new TodoAdapter();
        recyclerView.setAdapter(adapter);
        studyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    listNum.clear();
                    listTopic.clear();
                    listTime.clear();
                }
                for (DataSnapshot study : dataSnapshot.child("todo_list").getChildren()) {
                    if (study_key.contains(study.getKey())) {
                        if (study.getValue() == null) {

                            Log.v("pass", "pass");
                        }
                        else {
                            String num = study.child("num").getValue().toString();
                            String topic = study.child("topic").getValue().toString();
                            String time = study.child("time").getValue().toString();

                            Log.v("번호", "num" + num);
                            Log.v("토픽", "topic" + topic);
                            Log.v("시간", "time" + time);

                            listNum.add(num);
                            listTime.add(topic);
                            listTime.add(time);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        for (int i = 0; i < listNum.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Todo data = new Todo();
            data.setNum(listNum.get(i));
            data.setTopic(listTopic.get(i));
            data.setTime(listTime.get(i));
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}

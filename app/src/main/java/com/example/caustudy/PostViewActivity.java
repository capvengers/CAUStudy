package com.example.caustudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.caustudy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.noties.markwon.Markwon;
import io.noties.markwon.core.CorePlugin;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;

public class PostViewActivity extends AppCompatActivity {
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Study");
    private TextView textTitle;
    private TextView textContent;
    private String study_key, post_key;
    private String mark_text, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        Intent intent = getIntent();
        study_key = intent.getStringExtra("study_key");
        post_key = intent.getStringExtra("post_key");
        textTitle = findViewById(R.id.post_title_text);
        textContent = findViewById(R.id.post_contents_text);

        myRef.child(study_key).child("notice_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.getValue().toString() != null) {
                    mark_text = ds.child("content").getValue().toString();
                    title = ds.child("title").getValue().toString();
                    textTitle.setText(title);
                    simple();
                    //enabledBlockTypes();
                    //alreadyParsed();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void simple() {
        // 여기에 그냥 친거 그대로 받아오면 될 것 같은데?
        final Markwon markwon = Markwon.builder(this)
                .usePlugin(CorePlugin.create())
                .usePlugin(StrikethroughPlugin.create())
                .build();

        final String markdown = mark_text;
        // this will parse raw markdown and set parsed content to specified TextView
        markwon.setMarkdown(textContent, markdown);
    }
}

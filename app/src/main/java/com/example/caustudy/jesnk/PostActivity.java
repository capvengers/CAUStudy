package com.example.caustudy.jesnk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.caustudy.Models.FirebaseID;
import com.example.caustudy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private EditText mTitle, mContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mTitle = findViewById(R.id.post_title_edit);
        mContents = findViewById(R.id.post_contents_edit);
        findViewById(R.id.post_save_button).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(mAuth.getCurrentUser() != null) {
            String postId = mStore.collection(FirebaseID.post).document().getId();
            Log.d("PostActivity : postId",postId);
            Log.d("PostActivity : FirebaseID.post : ",FirebaseID.post);

            Map<String, Object> data = new HashMap<>();

            data.put("content", mContents.getText().toString());
            data.put("title", mTitle.getText().toString());
            data.put("author_uid", mAuth.getCurrentUser().getUid());
            data.put("time_stamp", FieldValue.serverTimestamp());

            //mStore.collection(FirebaseID.post).document(postId).set(data, SetOptions.merge());
            mStore.collection("Posts").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("data add ","is successs");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("data add ", "is fail");
                }
            });

            Log.d("test","Auth success");
            finish();
        } else {
            Log.d("test","Auth fail");
        }
    }
}
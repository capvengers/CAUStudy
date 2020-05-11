package com.example.caustudy.jesnk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.caustudy.Adaptors.PostAdaptor;
import com.example.caustudy.Models.FirebaseID;
import com.example.caustudy.Models.Post;
import com.example.caustudy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// git commit test
// jesnk
// Activity For Post Testing
public class JMainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private RecyclerView mPostRecyclerView;
    private PostAdaptor mAdapter;
    private List<Post> mDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j_main);
        mPostRecyclerView = findViewById(R.id.main_recyclerview);

        findViewById(R.id.jmain_post_edit).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mStore.collection("Posts")
                .orderBy("time_stamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() { // For real-time update
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear(); // For post-order
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String uId = String.valueOf(shot.get("author_uid"));
                                String user_name = String.valueOf(shot.get("user_name"));
                                String title = String.valueOf(shot.get("title"));
                                String contents = String.valueOf(shot.get("content"));

                                Timestamp time_stamp = (Timestamp)shot.get("time_stamp");
                                Post data = new Post(uId,user_name,title,contents);
                                data.time_stamp = time_stamp;

                                mDatas.add(data);
                                mAdapter = new PostAdaptor(mDatas);
                                mPostRecyclerView.setAdapter(mAdapter);

                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, PostActivity.class));

    }
}

package com.example.caustudy.jesnk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.caustudy.Adaptors.PostAdaptor;
import com.example.caustudy.Models.Post;
import com.example.caustudy.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


// jesnk
// Activity For Post Testing
public class JMainActivity extends AppCompatActivity {

    private RecyclerView mPostRecyclerView;
    private PostAdaptor mAdapter;
    private List<Post> mDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j_main);
        mPostRecyclerView = findViewById(R.id.main_recyclerview);
        mDatas = new ArrayList<>();
        mDatas.add(new Post("1","TestAuthor","Title_test","test"));
        mDatas.add(new Post("2","TestAuthor","Title_test2","test"));
        mDatas.add(new Post("3","TestAuthor","Title_test3","test"));
        mDatas.add(new Post("3","TestAuthor","Title_test4","test"));


        mAdapter = new PostAdaptor(mDatas);
        mPostRecyclerView.setAdapter(mAdapter);

    }
}

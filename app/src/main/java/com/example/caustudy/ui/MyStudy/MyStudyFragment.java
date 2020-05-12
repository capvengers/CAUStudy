package com.example.caustudy.ui.MyStudy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caustudy.Models.TextParsing;
import com.example.caustudy.R;
import com.example.caustudy.ui.searchstudy.RecyclerAdapter;
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

public class MyStudyFragment extends Fragment {

    private MyStudyViewModel myStudyViewModel;
    GridView gridView;
    EditText editText;
    EditText editText2;
    Button button;
    MyStudy_Adapter singerAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser userAuth = mAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference_user = firebaseDatabase.getReference("사용자");
    StringTokenizer stringTokenizer = new StringTokenizer(userAuth.getEmail(), "@");
    String user_id = stringTokenizer.nextToken();

    private String study_list_raw;
    private List<String> study_list;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Log.d("study",user_id);


        if (userAuth != null) {
            databaseReference_user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("study",ds.getKey().toString());
                        if (ds.getKey().equals(user_id)) {
                            try {
                                study_list_raw = ds.child("taken_study").getValue().toString();
                                study_list = TextParsing.toStringArray(study_list_raw);
                                Log.d("study",study_list.toString());
                                Log.d("study",study_list.get(0).toString());
                            } catch (NullPointerException e) {
                                Log.d("study","User not have any study");

                            }
                        } else {
                            Log.d("study", "Cannot found user");
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }




        myStudyViewModel = ViewModelProviders.of(this).get(MyStudyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mystudy, container, false);

        gridView = (GridView)root.findViewById(R.id.gridView);

        editText = (EditText)root.findViewById(R.id.editText);
        editText2 = (EditText)root.findViewById(R.id.editText2);
        button = (Button)root.findViewById(R.id.button);
        singerAdapter = new MyStudy_Adapter();
        singerAdapter.addItem(new MyStudy_SingerItem("dummy".toString(),"5.5~10.1","50:30","zp"));
        //singerAdapter.addItem(new MyStudy_SingerItem("test","period","time","zp"));

        gridView.setAdapter(singerAdapter);

        // 클릭시 행동할거!! 여기에 강의실 메뉴 뜨는 액티비티 넣으면 될거같은데
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return root;
    }


}
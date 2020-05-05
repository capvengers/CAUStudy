package com.example.caustudy.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.caustudy.R;

public class SearchStudyFragment extends Fragment {

    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_seachstudy, container, false);

        return root;
    }


     /*
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();
        tv_customer_name=(TextView)findViewById(R.id.tv_customer_name);

        if (userAuth != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (userAuth.getEmail().equals(ds.child("email").getValue().toString())) {
                            String username = ds.child("username").getValue().toString();
                            tv_customer_name.setText(username+"님, 환영합니다!");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        meetingListView = (ListView) findViewById(R.id.meeting);

        // 리스트뷰에 데이터 띄우기
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, meetingList);
        meetingListView.setAdapter(adapter);

        adapter.add("제순의 ML");
        adapter.notifyDataSetChanged();
        adapter.add("파이썬 정복기");
        adapter.notifyDataSetChanged();



*/
}
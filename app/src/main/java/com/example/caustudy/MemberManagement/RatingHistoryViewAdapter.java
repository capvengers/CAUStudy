package com.example.caustudy.MemberManagement;


import android.media.Rating;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caustudy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RatingHistoryViewAdapter extends RecyclerView.Adapter<RatingHistoryViewAdapter.ViewHolder> {

    private ArrayList<RatingHistoryViewItem> listItem = new ArrayList<>();
    private String study_key;
    DatabaseReference studyRef = FirebaseDatabase.getInstance().getReference("Study");

    RecyclerView member_recyclerView;
    List<String> listKey = new ArrayList<>();
    List<String> listRate = new ArrayList<>();
    List<String> listFeedback = new ArrayList<>();
    List<String> listStudyName = new ArrayList<>();



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Item을 하나씩 출력
        holder.onBind(listItem.get(position));

    }

    @Override
    public int getItemCount() {
        // ApplyViewItem 수
        return listItem.size();
    }

    public void addItem(RatingHistoryViewItem ratingHistoryViewItem) {
        listItem.add(ratingHistoryViewItem);
    }
    public void clearItem() {listItem.clear();}
    public void setStudyKey(String studyKey) {
        this.study_key = studyKey;
    }

    private OnItemClickListener mListener = null ;

    public interface OnItemClickListener {
        void onDeleteClick(View v, int position);
        void onAcceptClick(View v, int position) ;
        void onRatingInquiry(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public void update_rates_view(String id_mem) {
        studyRef.child(id_mem).child("ratings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    listKey.clear();
                    listFeedback.clear();
                    listRate.clear();
                    listItem.clear();
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    studyRef.child(study_key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String study_name = dataSnapshot.child("study_name").getValue().toString();
                            String study_key = ds.getKey().toString();
                            String rate = ds.child("rate").getValue().toString();
                            String feedback = ds.child("feedback").getValue().toString();
                            listKey.add(study_key);
                            listRate.add(rate);
                            listFeedback.add(feedback);
                            listStudyName.add(study_name);

                            RatingHistoryViewItem ratingViewItem = new RatingHistoryViewItem();
                            ratingViewItem.setFeedback(feedback);
                            ratingViewItem.setRate(rate);
                            ratingViewItem.setStudyName(study_name);
                            listItem.add(ratingViewItem);
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


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView email;
        private TextView dept;
        private ImageView accept;
        private ImageView delete;
        private Button btn_rating_view;

        ViewHolder(final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            email = itemView.findViewById(R.id.item_email);
            dept = itemView.findViewById(R.id.item_dept);


        }

        void onBind(RatingHistoryViewItem ratingViewItem) {
            name.setText(ratingViewItem.getName());
            email.setText(ratingViewItem.getEmail());
            String dept_temp = ratingViewItem.getL_dept() + " " + ratingViewItem.getS_dept();
            dept.setText(dept_temp);
        }
    }

}
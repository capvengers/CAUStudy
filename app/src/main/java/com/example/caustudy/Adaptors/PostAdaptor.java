package com.example.caustudy.Adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caustudy.Models.Post;
import com.example.caustudy.R;
import java.util.List;

// jesnk
// For Posting d


// https://www.youtube.com/watch?v=pOCP4FSWMFY
// 1:13:00
public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.PostViewHolder> {

    private List<Post> datas;

    public PostAdaptor(List<Post> datas){
        this.datas = datas;

    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.contents.setText(data.getContents());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView contents;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_title);
            contents = itemView.findViewById(R.id.item_post_contents);

        }
    }


}

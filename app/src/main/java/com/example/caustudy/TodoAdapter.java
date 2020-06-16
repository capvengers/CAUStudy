package com.example.caustudy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private ArrayList<Todo> listTodo = new ArrayList<>();
    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        // Item을 하나씩 출력
        holder.onBind(listTodo.get(position));

    }

    @Override
    public int getItemCount() {
        // Item 수
        return listTodo.size();
    }

    public void addItem(Todo todo) {
        listTodo.add(todo);
    }

    private OnItemClickListener mListener = null ;

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }


    class TodoViewHolder extends RecyclerView.ViewHolder {

        private TextView num;
        private TextView topic;
        private TextView time;

        TodoViewHolder(View itemView) {
            super(itemView);

            num = itemView.findViewById(R.id.item_num);
            topic = itemView.findViewById(R.id.item_topic);
            time = itemView.findViewById(R.id.item_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });
        }

        void onBind(Todo todo) {
            num.setText(todo.getNum());
            topic.setText(todo.getTopic());
            time.setText(todo.getTime());
        }
    }
}

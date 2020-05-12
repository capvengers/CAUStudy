package com.example.caustudy.ui.MyStudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caustudy.Adaptors.PostAdaptor;
import com.example.caustudy.Models.Post;
import com.example.caustudy.R;

import java.util.ArrayList;

public class MyStudy_Adapter extends BaseAdapter {
    ArrayList<MyStudy_SingerItem> items = new ArrayList<MyStudy_SingerItem>();
    String title;
    String period;
    String time;
    String organization;
    LayoutInflater inf;

    public MyStudy_Adapter() {

    }

    public MyStudy_Adapter(Context context, String title, String period, String time, String organization) {
        this.title = title;
        this.period = period;
        this.time = time;
        this.organization = organization;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(MyStudy_SingerItem singerItem) {
        items.add(singerItem);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyStudy_SingerViewer singerViewer = new MyStudy_SingerViewer(viewGroup.getContext().getApplicationContext());
        singerViewer.setItem(items.get(i));
        return singerViewer;
    }
}
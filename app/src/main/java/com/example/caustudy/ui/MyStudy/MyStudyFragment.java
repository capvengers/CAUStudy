package com.example.caustudy.ui.MyStudy;

import android.content.Context;
import android.os.Bundle;
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

import com.example.caustudy.R;
import com.example.caustudy.ui.searchstudy.RecyclerAdapter;

import java.util.ArrayList;

public class MyStudyFragment extends Fragment {

    private MyStudyViewModel myStudyViewModel;
    GridView gridView;
    EditText editText;
    EditText editText2;
    Button button;
    MyStudy_Adapter singerAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        myStudyViewModel = ViewModelProviders.of(this).get(MyStudyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mystudy, container, false);

        gridView = (GridView)root.findViewById(R.id.gridView);

        editText = (EditText)root.findViewById(R.id.editText);
        editText2 = (EditText)root.findViewById(R.id.editText2);
        button = (Button)root.findViewById(R.id.button);
        singerAdapter = new MyStudy_Adapter();
        singerAdapter.addItem(new MyStudy_SingerItem("title","5.5~10.1","50:30","zp"));
        singerAdapter.addItem(new MyStudy_SingerItem("test","period","time","zp"));

        gridView.setAdapter(singerAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return root;
    }


}
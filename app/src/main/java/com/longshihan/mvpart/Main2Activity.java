package com.longshihan.mvpart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.longshihan.baseadapter.MultiTypeAdapter;

public class Main2Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MultiTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView=findViewById(R.id.main_rv2);
        adapter=new MultiTypeAdapter();
        adapter.register(String.class,new StringTextItemViewBinder());
    }
}

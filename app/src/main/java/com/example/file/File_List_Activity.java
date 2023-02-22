package com.example.file;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class File_List_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextView noFileFounds = findViewById(R.id.noFileFounds);

        String path = getIntent().getStringExtra("path");
        File root = new File(path);
        File[] filesNfolders = root.listFiles();

        if(filesNfolders == null || filesNfolders.length ==0){
            noFileFounds.setVisibility(View.VISIBLE);
            return;
        }
        noFileFounds.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), filesNfolders));
    }
}
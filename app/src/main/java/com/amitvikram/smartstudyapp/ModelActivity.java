package com.amitvikram.smartstudyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ModelActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    ArrayList<Models> arrayList = new ArrayList<>();
    ModelListAdapter modelListAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        recyclerView = findViewById(R.id.model_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    arrayList.add(new Models(postSnapshot.getKey(), postSnapshot.getValue(String.class)));
                }
                initAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initAdapter() {
        modelListAdapter = new ModelListAdapter(arrayList, this);
        recyclerView.setAdapter(modelListAdapter);
        initListener();
    }

    private void initListener() {
        modelListAdapter.setOnItemClickListener(new ModelListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ModelActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ModelActivity.this, UploadTopicActivity.class);
                intent.putExtra("model_link", arrayList.get(position).getModelLink());
                intent.putExtra("model_name", arrayList.get(position).getModelName());
                startActivity(intent);
                finish();
            }
        });
    }
}
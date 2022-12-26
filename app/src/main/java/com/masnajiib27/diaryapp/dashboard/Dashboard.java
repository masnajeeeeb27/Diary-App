package com.masnajiib27.diaryapp.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masnajiib27.diaryapp.Signin;
import com.masnajiib27.diaryapp.databinding.ActivityDashboardBinding;
import com.masnajiib27.diaryapp.model.ModelDiary;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements UpdateDiary {
    ActivityDashboardBinding binding; // --> untuk mengakses view
    ArrayList<ModelDiary> diaries; //membuat arraylist
    DatabaseReference reference; //firebase
    SharedPreferences preferences; // --> untuk menyimpan data sementara
    SharedPreferences.Editor editor; // --> untuk mengedit data sementara
    LinearLayoutManager linearLayoutManager; // --> untuk membuat layout linear
    AdapterDiary adapterDiary; // --> untuk mengakses adapter
    ModelDiary modelDiary; // --> untuk mengakses model
    ProgressDialog dialog; // --> untuk membuat loading
    String unique; // --> untuk menyimpan data sementara


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferences = getSharedPreferences("userdiary", MODE_PRIVATE);
        unique = preferences.getString("unique", "");
        reference = FirebaseDatabase.getInstance().getReference("DataUser").child(unique);
        diaries = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        dialog = new ProgressDialog(Dashboard.this);

        binding.rvDiary.setLayoutManager(linearLayoutManager);
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, AddDiary.class));
            }
        });
        showDiary();

    }

    private void showDiary(){
        dialog.setMessage("Loading...");
        dialog.show();
        diaries.clear();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    modelDiary = ds.getValue(ModelDiary.class);
                    diaries.add(modelDiary);
                }
                adapterDiary = new AdapterDiary(Dashboard.this, diaries);
                binding.rvDiary.setAdapter(adapterDiary);
                binding.noItem.setVisibility(View.GONE);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.noItem.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void updateDiary(ModelDiary Diary) {
        reference.child(modelDiary.getUserid()).setValue(Diary).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Dashboard.this, "Update Success", Toast.LENGTH_SHORT).show();
                    showDiary();
                }
                else {
                    Toast.makeText(Dashboard.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void deleteDiary(ModelDiary Diary) {
        reference.child(modelDiary.getUserid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Dashboard.this, "Delete Success", Toast.LENGTH_SHORT).show();
                    showDiary();
                }
                else {
                    Toast.makeText(Dashboard.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showDiary();
    }
}

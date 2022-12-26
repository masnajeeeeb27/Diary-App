package com.masnajiib27.diaryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.masnajiib27.diaryapp.databinding.ActivityForgotBinding;

public class Forgot extends AppCompatActivity {

    ActivityForgotBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance(); // --> untuk mengakses firebase

        binding.reset.setOnClickListener(new View.OnClickListener() { // --> untuk mengirimkan email reset password
            @Override // --> override method fungsinya untuk mengganti method yang sudah ada
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                resetPassword(email);
            }
        });
    }
    private void resetPassword(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Forgot.this, "Silakan Periksa Email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Forgot.this, "Email Tidak Terdaftar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

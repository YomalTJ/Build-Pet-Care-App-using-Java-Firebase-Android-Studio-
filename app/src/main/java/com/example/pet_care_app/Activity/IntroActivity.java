package com.example.pet_care_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.pet_care_app.R;
import com.example.pet_care_app.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null){
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                }
                else {
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                }
            }
        });

        binding.SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, SignupActivity.class));
            }
        });
    }
}
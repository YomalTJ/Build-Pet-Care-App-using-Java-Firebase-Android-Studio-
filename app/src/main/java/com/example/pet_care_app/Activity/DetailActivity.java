package com.example.pet_care_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.pet_care_app.Domain.Pets;
import com.example.pet_care_app.Helper.ManagementCart;
import com.example.pet_care_app.R;
import com.example.pet_care_app.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Pets object;
    private int num = 1;
    private ManagementCart managementCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        managementCart = new ManagementCart(this);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);

        binding.priceTxt.setText("$" + object.getPrice());
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar() + " Rating");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText(num * object.getPrice() + "");


        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(num);
                managementCart.insertPets(object);
            }
        });
    }

    private void getIntentExtra() {
        object = (Pets) getIntent().getSerializableExtra("object");
    }
}
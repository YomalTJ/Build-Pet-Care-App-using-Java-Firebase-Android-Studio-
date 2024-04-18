package com.example.pet_care_app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.pet_care_app.Adapter.PetListAdapter;
import com.example.pet_care_app.Domain.Pets;
import com.example.pet_care_app.R;
import com.example.pet_care_app.databinding.ActivityListPetsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListPetsActivity extends BaseActivity {
    ActivityListPetsBinding binding;
    private RecyclerView.Adapter adapterListPets;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListPetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList();
        setVariable();
    }

    private void setVariable() {
    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Pets");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Pets> list = new ArrayList<>();

        Query query;
        if (isSearch){
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText + '\uf8ff');
        }
        else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Pets.class));
                    }
                    if (list.size() > 0){
                        binding.petListView.setLayoutManager(new GridLayoutManager(ListPetsActivity.this, 2));
                        adapterListPets = new PetListAdapter(list);
                        binding.petListView.setAdapter(adapterListPets);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        binding.titleTxt.setText(categoryName);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
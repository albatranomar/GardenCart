package com.android.gardencart.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gardencart.R;
import com.android.gardencart.adapters.ChipAdapter;
import com.android.gardencart.repositores.IItemsRepository;
import com.android.gardencart.repositores.MockItems;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvTags;
    private IItemsRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpRepository();

        setUpViews();
    }

    private void setUpRepository() {
        repository = MockItems.getInstance();
    }

    private void setUpViews() {
        rvTags = findViewById(R.id.rvTags);

        ChipAdapter chipAdapter = new ChipAdapter(this, repository.getTags(), label -> Toast.makeText(MainActivity.this, label + " clicked", Toast.LENGTH_SHORT).show());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvTags.setLayoutManager(layoutManager);
        rvTags.setAdapter(chipAdapter);
    }
}
package com.android.gardencart.activities;

import static com.android.gardencart.activities.LoginActivity.KEEP_LOGIN;
import static com.android.gardencart.activities.LoginActivity.USER_DATA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gardencart.R;
import com.android.gardencart.adapters.CardAdapter;
import com.android.gardencart.adapters.ChipAdapter;
import com.android.gardencart.models.Item;
import com.android.gardencart.models.User;
import com.android.gardencart.repositores.items.IItemsRepository;
import com.android.gardencart.repositores.items.MockItems;
import com.android.gardencart.repositores.users.IUsersRepository;
import com.android.gardencart.repositores.users.MockUsers;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    static final String USERS_DATA = "USERS_DATA";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private User user;

    private RecyclerView rvTags, rvCards;
    private ImageButton ibLogout, ibSearch;
    private TextView tvUsername;
    private EditText etSearch;

    private IItemsRepository itemsRepository;
    private IUsersRepository usersRepository;

    private CardAdapter cardAdapter;

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

        setupRepository();
        setupPreferences();
        setUpViews();
    }

    private void setupRepository() {
        itemsRepository = MockItems.getInstance();
        usersRepository = MockUsers.getInstance();
    }

    private void setupPreferences() {
        Gson gson = new Gson();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        String userData = prefs.getString(USER_DATA, null);
        if (userData != null) {
            try {
                user = gson.fromJson(userData, User.class);
            } catch (Exception ex) {
                Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpViews() {
        rvTags = findViewById(R.id.rvTags);
        rvCards = findViewById(R.id.rvCards);
        ibLogout = findViewById(R.id.ibLogout);
        tvUsername = findViewById(R.id.tvUsername);
        etSearch = findViewById(R.id.etSearch);
        ibSearch = findViewById(R.id.ibSearch);


        ArrayList<String> tags = new ArrayList<>();
        tags.add("all");
        tags.addAll(itemsRepository.getTags());
        ChipAdapter chipAdapter = new ChipAdapter(this, tags, this::onChipClicked);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvTags.setLayoutManager(layoutManager);
        rvTags.setAdapter(chipAdapter);


        cardAdapter = new CardAdapter(this, itemsRepository.getItems());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvCards.setLayoutManager(gridLayoutManager);
        rvCards.setAdapter(cardAdapter);

        tvUsername.setText(user.getUsername());

        ibLogout.setOnClickListener(this::onLogout);
        ibSearch.setOnClickListener(this::onSearchClick);
    }

    private void onSearchClick(View v) {
        String term = etSearch.getText().toString();

        if (term.isEmpty()) {
            cardAdapter.setItems(itemsRepository.getItems());
            return;
        }

        List<Item> filtered = itemsRepository.getItems().stream().filter(item -> item.getName().toLowerCase().contains(term.toLowerCase())).collect(Collectors.toList());
        cardAdapter.setItems(filtered);
    }

    private void onChipClicked(String tag) {
        if (tag.equalsIgnoreCase("all")) {
            cardAdapter.setItems(itemsRepository.getItems());
            return;
        }

        List<Item> filtered = itemsRepository.getItems().stream().filter(item -> item.getTags().contains(tag.toLowerCase())).collect(Collectors.toList());
        cardAdapter.setItems(filtered);
    }

    private void onLogout(View v) {
        Gson gson = new Gson();

        editor.putBoolean(KEEP_LOGIN, false);
        editor.putString(USERS_DATA, gson.toJson(usersRepository.getUsers()));
        editor.commit();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
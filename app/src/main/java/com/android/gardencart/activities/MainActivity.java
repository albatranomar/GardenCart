package com.android.gardencart.activities;

import static com.android.gardencart.activities.LoginActivity.KEEP_LOGIN;
import static com.android.gardencart.activities.LoginActivity.USER_DATA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
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
import com.android.gardencart.models.User;
import com.android.gardencart.repositores.items.IItemsRepository;
import com.android.gardencart.repositores.items.MockItems;
import com.android.gardencart.repositores.users.IUsersRepository;
import com.android.gardencart.repositores.users.MockUsers;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    static final String USERS_DATA = "USERS_DATA";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private User user;

    private RecyclerView rvTags, rvCards;
    private ImageButton ibLogout;
    private TextView tvUsername;

    private IItemsRepository itemsRepository;
    private IUsersRepository usersRepository;

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

        ChipAdapter chipAdapter = new ChipAdapter(this, itemsRepository.getTags(), label -> Toast.makeText(MainActivity.this, label + " clicked", Toast.LENGTH_SHORT).show());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvTags.setLayoutManager(layoutManager);
        rvTags.setAdapter(chipAdapter);

        CardAdapter cardAdapter = new CardAdapter(this, itemsRepository.getItems());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvCards.setLayoutManager(gridLayoutManager);
        rvCards.setAdapter(cardAdapter);

        tvUsername.setText(user.getUsername());

        ibLogout.setOnClickListener(this::onLogout);
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
package com.android.gardencart.activities;

import static com.android.gardencart.Constants.ITEMS_DATA;
import static com.android.gardencart.Constants.KEEP_LOGIN;
import static com.android.gardencart.Constants.USERS_DATA;
import static com.android.gardencart.Constants.USER_USERNAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.android.gardencart.models.CartItem;
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
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private User user;

    private RecyclerView rvTags, rvCards;
    private ImageButton ibLogout, ibSearch;
    private TextView tvUsername, tvCartItemsNumber;
    private EditText etSearch;
    private ImageView ivCart;

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

    @Override
    protected void onResume() {
        super.onResume();
        setUpViews();
    }

    private void setupRepository() {
        itemsRepository = MockItems.getInstance();
        usersRepository = MockUsers.getInstance();
    }

    private void setupPreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        String username = prefs.getString(USER_USERNAME, null);
        if (username != null) {
            try {
                user = usersRepository.getUserByUsername(username);
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
        tvCartItemsNumber = findViewById(R.id.tvCartItemNumbers);
        ivCart = findViewById(R.id.ivCart);

        ArrayList<String> tags = new ArrayList<>();
        tags.add("all");
        tags.addAll(itemsRepository.getTags());
        ChipAdapter chipAdapter = new ChipAdapter(this, tags, this::onChipClicked);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvTags.setLayoutManager(layoutManager);
        rvTags.setAdapter(chipAdapter);

        cardAdapter = new CardAdapter(this, itemsRepository.getItems(), this::onAddItemToCart);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvCards.setLayoutManager(gridLayoutManager);
        rvCards.setAdapter(cardAdapter);

        tvUsername.setText(user.getUsername());

        int units = 0;
        for (CartItem ci: user.getCart()) {
            units += ci.getAmount();
        }
        tvCartItemsNumber.setText(units + "");

        ibLogout.setOnClickListener(this::onLogout);
        ibSearch.setOnClickListener(this::onSearchClick);
        ivCart.setOnClickListener(this::onCartClicked);
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

    private void onAddItemToCart(Item item) {
        Gson gson = new Gson();

        boolean added = false;
        for (CartItem i: user.getCart()) {
            if (i.getId().equalsIgnoreCase(item.getId())) {
                i.setAmount(i.getAmount() + 1);
                added = true;
                break;
            }
        }

        item.setAvailableQuantity(item.getAvailableQuantity() - 1);

        if (!added) {
            user.getCart().add(new CartItem(item.getId(), item.getName(), item.getImage(), item.getTags(), item.getPricePerUnit(), 1, item.getAvailableQuantity()));
        }

        if (item.getAvailableQuantity() == 0) {
            cardAdapter.setItems(cardAdapter.getItems());
        }

        int units = 0;
        for (CartItem ci: user.getCart()) {
            units += ci.getAmount();
        }

        editor.putString(ITEMS_DATA, gson.toJson(itemsRepository.getItems()));
        editor.putString(USERS_DATA, gson.toJson(usersRepository.getUsers()));
        editor.commit();

        tvCartItemsNumber.setText(units + " ");
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

    private void onCartClicked(View v) {
        Intent intent = new Intent(MainActivity.this, CartActivity.class);
        startActivity(intent);
    }
}
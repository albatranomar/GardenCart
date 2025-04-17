package com.android.gardencart.activities;

import static com.android.gardencart.Constants.ITEMS_DATA;
import static com.android.gardencart.Constants.USERS_DATA;
import static com.android.gardencart.Constants.USER_USERNAME;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gardencart.R;
import com.android.gardencart.adapters.CartItemAdapter;
import com.android.gardencart.models.CartItem;
import com.android.gardencart.models.Item;
import com.android.gardencart.models.User;
import com.android.gardencart.repositores.items.IItemsRepository;
import com.android.gardencart.repositores.items.MockItems;
import com.android.gardencart.repositores.users.IUsersRepository;
import com.android.gardencart.repositores.users.MockUsers;
import com.google.gson.Gson;

import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private User user;

    private IItemsRepository itemsRepository;
    private IUsersRepository usersRepository;

    private CartItemAdapter cartItemAdapter;

    private RecyclerView rvItems;
    private AppCompatButton btGoBack, btCheckOut;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
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
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        String username = prefs.getString(USER_USERNAME, null);
        if (username != null) {
            try {
                user = usersRepository.getUserByUsername(username);
            } catch (Exception ex) {
                Toast.makeText(CartActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUpViews() {
        btGoBack = findViewById(R.id.btGoBack);
        rvItems = findViewById(R.id.rvCartItems);
        tvTotal = findViewById(R.id.tvTotal);
        btCheckOut = findViewById(R.id.btCheckOut);

        cartItemAdapter = new CartItemAdapter(this, user.getCart(), item -> {
            double total = 0;
            for (CartItem ci: user.getCart()) {
                total += ci.getAmount() * ci.getPricePerUnit();
            }
            tvTotal.setText(String.format(Locale.CANADA,"Total: %.2f", total));

            Item i = itemsRepository.getItemByItemId(item.getId());
            i.setAvailableQuantity(i.getAvailableQuantity() + item.getAmount());

            Gson gson = new Gson();
            editor.putString(ITEMS_DATA, gson.toJson(itemsRepository.getItems()));
            editor.putString(USERS_DATA, gson.toJson(usersRepository.getUsers()));
            editor.commit();
        });
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(gridLayoutManager);
        rvItems.setAdapter(cartItemAdapter);

        double total = 0;
        for (CartItem ci: user.getCart()) {
            total += ci.getAmount() * ci.getPricePerUnit();
        }
        tvTotal.setText(String.format(Locale.CANADA,"Total: %.2f", total));

        btGoBack.setOnClickListener(this::onGoBack);
        btCheckOut.setOnClickListener(this::onCheckOut);
    }

    private void onCheckOut(View v) {
        user.getCart().clear();
        Gson gson = new Gson();
        editor.putString(USERS_DATA, gson.toJson(usersRepository.getUsers()));
        editor.commit();

        Toast.makeText(this, "Checkout successfully ✔️", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void onGoBack(View v) {
        finish();
    }
}
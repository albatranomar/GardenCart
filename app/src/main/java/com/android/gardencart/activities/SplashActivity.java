package com.android.gardencart.activities;

import static com.android.gardencart.activities.MainActivity.USERS_DATA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.gardencart.R;
import com.android.gardencart.models.User;
import com.android.gardencart.repositores.users.MockUsers;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        ImageView ivLogo = findViewById(R.id.ivLogo);

        ivLogo.setAnimation(logoAnimation);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String usersData = prefs.getString(USERS_DATA, null);
        if (usersData != null) {
            Gson gson = new Gson();
            List<User> users = Arrays.asList(gson.fromJson(usersData, User[].class));
            MockUsers.getInstance().setUsers(users);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3500);
    }
}
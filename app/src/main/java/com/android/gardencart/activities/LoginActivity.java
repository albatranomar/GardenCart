package com.android.gardencart.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.gardencart.R;
import com.android.gardencart.models.User;
import com.android.gardencart.repositores.users.IUsersRepository;
import com.android.gardencart.repositores.users.MockUsers;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    static final String USER_DATA = "USER_DATA";
    static final String KEEP_LOGIN = "KEEP_LOGIN";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private IUsersRepository usersRepository;

    private EditText etUsername, etPassword;
    private CheckBox cbKeepLoggedIn;
    private AppCompatButton btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRepository();
        setupPreferences();
        setupViews();
    }

    private void setupRepository() {
        usersRepository = MockUsers.getInstance();
    }

    private void setupPreferences() {
        Gson gson = new Gson();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        String userData = prefs.getString(USER_DATA, null);
        if (userData != null) {
            try {
                User user = gson.fromJson(userData, User.class);
                if (prefs.getBoolean(KEEP_LOGIN, false)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (Exception ex) {
                Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbKeepLoggedIn = findViewById(R.id.cbKeepLoggedIn);
        btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(this::onLogin);

    }

    private void onLogin(View v) {
        Gson gson = new Gson();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        boolean keepLoggedIn = cbKeepLoggedIn.isChecked();

        User u = usersRepository.getUserByUsername(username);
        if (u == null) {
            Toast.makeText(LoginActivity.this, "Username not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!u.getPassword().equals(password)) {
            Toast.makeText(LoginActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
            return;
        }

        editor.putString(USER_DATA, gson.toJson(u));
        if (keepLoggedIn) {
            editor.putBoolean(KEEP_LOGIN, true);
        }
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
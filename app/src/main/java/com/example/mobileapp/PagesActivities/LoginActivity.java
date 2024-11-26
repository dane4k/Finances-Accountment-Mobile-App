package com.example.mobileapp.PagesActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.Controllers.AuthController;
import com.example.mobileapp.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private AuthController authController = new AuthController();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        Button redirectToRegisterButton = findViewById(R.id.redirectToRegisterButton);
        EditText loginUsernameinput = findViewById(R.id.loginUsernameinput);
        EditText loginPasswordInput = findViewById(R.id.loginPasswordInput);
        Button loginUserButton = findViewById(R.id.loginUserButton);

//
//        redirectToRegisterButton.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//            startActivity(intent);
//        });

        redirectToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginUsernameinput.getText().toString().trim();
                String password = loginPasswordInput.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                executorService.execute(() -> {
                    String result = authController.logInUser (username, password);
                    runOnUiThread(() -> handleLoginResult(result, username));
                });
            }
        });
    }

    private void handleLoginResult(String result, String username) {
        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();

        if ("Успешный вход".equals(result)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        }
    }
}
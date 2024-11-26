package com.example.mobileapp.PagesActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.Controllers.AuthController;
import com.example.mobileapp.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private AuthController authController = new AuthController();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        Button redirectToLoginButton = findViewById(R.id.redirectToLoginButton);
        Button registerUserButton = findViewById(R.id.registerUserButton);
        EditText registerUsernameinput = findViewById(R.id.registerUsernameinput);
        EditText registerPasswordInput = findViewById(R.id.registerPasswordInput);

        redirectToLoginButton.setOnClickListener(new View.OnClickListener() { // редирект на логин
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = registerUsernameinput.getText().toString().trim();
                String password = registerPasswordInput.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Пожалуйста, заполните все поля.", Toast.LENGTH_SHORT).show();
                    return;
                }

                executorService.execute(() -> {
                    String result = authController.registerUser (username, password);
                    runOnUiThread(() -> handleRegistrationResult(result));
                });
            }
        });


    }
    private void handleRegistrationResult(String result) {
        Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();

        if ("Пользователь зарегистрирован".equals(result)) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
//            finish();
        }
    }
}
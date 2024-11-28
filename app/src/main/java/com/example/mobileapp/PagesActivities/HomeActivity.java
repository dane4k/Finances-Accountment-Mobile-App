package com.example.mobileapp.PagesActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");


        TextView usernameLabel = findViewById(R.id.usernameLabelHome);
        usernameLabel.setText(username);

        Button logOutButton = findViewById(R.id.logOutButton);
        Button redirectToCategoriesButton = findViewById(R.id.redirectToCategoriesButton);
        Button redirectToTransactionsButton = findViewById(R.id.redirectToTransactionsButton);

        redirectToCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CategoriesActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        redirectToTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TransactionsActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

}

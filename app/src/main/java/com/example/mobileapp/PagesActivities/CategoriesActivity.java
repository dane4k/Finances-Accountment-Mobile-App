package com.example.mobileapp.PagesActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoriesActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_page);

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");


        TextView usernameLabelCategories = findViewById(R.id.usernameLabelCategories);
        usernameLabelCategories.setText(username);

        Button goBackButton = findViewById(R.id.goBackButton);
        EditText inputCategoryName = findViewById(R.id.inputCategoryName);
        Button submitAddCategoryButton = findViewById(R.id.submitAddCategoryButton);



        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this, HomeActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        submitAddCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    private void handleLoginResult(String result, String username) {
        Toast.makeText(CategoriesActivity.this, result, Toast.LENGTH_SHORT).show();

        if ("Успешный вход".equals(result)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        }
    }

}

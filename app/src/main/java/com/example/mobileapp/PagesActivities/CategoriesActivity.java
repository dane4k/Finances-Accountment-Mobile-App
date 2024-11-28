package com.example.mobileapp.PagesActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.Controllers.CategoryController;
import com.example.mobileapp.DTO.Category;
import com.example.mobileapp.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoriesActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private CategoryController categoryController = new CategoryController();
    private TableLayout categoriesTable;

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
        categoriesTable = findViewById(R.id.categoriesTable);


        fillCategoriesTable(username);

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
                String categoryName = inputCategoryName.getText().toString().trim();
                if (categoryName.isEmpty()) {
                    Toast.makeText(CategoriesActivity.this, "Сначала введите название категории", Toast.LENGTH_SHORT).show();
                    return;
                }

                executorService.execute(() -> {
                    String result = categoryController.addCategory(categoryName, username);
                    runOnUiThread(() -> handleAddResult(result));
                    inputCategoryName.getText().clear();
                    fillCategoriesTable(username);

                });
            }
        });
    }


    private void fillCategoriesTable(String username) {
        executorService.execute(() -> {
            List<Category> categoriesData = null;
            try {
                categoriesData = categoryController.getCategories(username);
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(CategoriesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                return;
            }


            if (categoriesData != null) {
                List<Category> finalCategoriesData = categoriesData;
                runOnUiThread(() -> {

                    categoriesTable.removeAllViews();


                    TableRow headerRow = new TableRow(this);
                    headerRow.setBackgroundColor(Color.LTGRAY);

                    String[] headers = {"Название", "Действия"};
                    for (String header : headers) {
                        TextView headerTextView = new TextView(this);
                        headerTextView.setText(header);
                        headerTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        headerTextView.setTextSize(20);
                        headerTextView.setTextColor(Color.BLACK);
                        headerRow.addView(headerTextView);
                    }
                    categoriesTable.addView(headerRow);

                    for (Category category : finalCategoriesData) {
                        TableRow tableRow = new TableRow(this);
                        tableRow.setBackgroundColor(Color.WHITE);

                        TextView nameTextView = new TextView(this);
                        nameTextView.setText(category.toString());
                        nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        tableRow.addView(nameTextView);


                        Button deleteButton = new Button(this);
                        deleteButton.setText("Удалить");
                        deleteButton.setBackgroundColor(Color.RED);
                        deleteButton.setTextColor(Color.WHITE);
                        deleteButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


                        deleteButton.setOnClickListener(v -> {

                            executorService.execute(() -> {
                                String result = categoryController.deleteCategory(username, category.getName());
                                runOnUiThread(() -> {
                                    handleDeleteResult(result);
                                    fillCategoriesTable(username);
                                });
                            });
                        });

                        tableRow.addView(deleteButton);
                        categoriesTable.addView(tableRow);
                    }
                });
            }
        });
    }

    private void handleDeleteResult(String result) {
        Toast.makeText(CategoriesActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    private void handleAddResult(String result) {
        Toast.makeText(CategoriesActivity.this, result, Toast.LENGTH_SHORT).show();
    }
}
package com.example.mobileapp.PagesActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileapp.Controllers.CategoryController;
import com.example.mobileapp.Controllers.TransactionController;
import com.example.mobileapp.DTO.Category;
import com.example.mobileapp.DTO.Transaction;
import com.example.mobileapp.DTO.User;
import com.example.mobileapp.R;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionsActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TransactionController transactionController = new TransactionController();
    private CategoryController categoryController = new CategoryController();
    private TableLayout transactionsTable;
    private Spinner inputType;
    private Spinner inputCategory;

    @SuppressLint("NewApi")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions_page);

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        TextView usernameLabelTransactions = findViewById(R.id.usernameLabelTransactions);
        usernameLabelTransactions.setText(username);

        Button goBackButton = findViewById(R.id.goBackButton);
        TextView inputAmount = findViewById(R.id.inputAmount);
        TextView inputDate = findViewById(R.id.inputDate);
        inputType = findViewById(R.id.inputType);
        inputCategory = findViewById(R.id.inputCategory);
        Button submitAddTransactionButton = findViewById(R.id.submitAddTransactionButton);
        transactionsTable = findViewById(R.id.transactionsTable);


        fillIncomeSpinner();
        fillCategoriesSpinner(username);


        fillTransactionsTable(username);


        inputDate.setOnClickListener(v -> {
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();
            int month = currentDate.getMonthValue() - 1;
            int day = currentDate.getDayOfMonth();

            DatePickerDialog datePickerDialog = new DatePickerDialog(TransactionsActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d.%02d.%d", selectedDay, selectedMonth + 1, selectedYear);
                        inputDate.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionsActivity.this, HomeActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        submitAddTransactionButton.setOnClickListener(v -> {
            String amountStr = inputAmount.getText().toString().trim();
            String dateStr = inputDate.getText().toString().trim();
            String type = inputType.getSelectedItem().toString();

            if (amountStr.isEmpty() || dateStr.isEmpty()) {
                Toast.makeText(TransactionsActivity.this, "Введите сумму и дату", Toast.LENGTH_SHORT).show();
                return;
            }

            LocalDate date;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                date = LocalDate.parse(dateStr, formatter);
            } catch (Exception e) {
                Toast.makeText(TransactionsActivity.this, "Неверный формат даты", Toast.LENGTH_SHORT).show();
                return;
            }

            executorService.execute(() -> {
                Transaction transaction = new Transaction();
                transaction.setAmount(Double.valueOf(amountStr));
                transaction.setDate(date);
                transaction.setIncome(type.equals("Доход"));


                Category selectedCategory = (Category) inputCategory.getSelectedItem();
                transaction.setCategory(selectedCategory);

                String result = transactionController.addTransaction(username, transaction);
                runOnUiThread(() -> handleAddResult(result));
                inputAmount.setText("");
                inputDate.setText("Выберите дату");
                fillTransactionsTable(username);
            });
        });
    }

    private void fillIncomeSpinner() {
        String[] types = {"Доход", "Расход"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputType.setAdapter(typeAdapter);
    }

    private void fillCategoriesSpinner(String username) {
        executorService.execute(() -> {
            List<Category> categories = null;
            try {
                categories = categoryController.getCategories(username);
            } catch (IOException e) {
                Toast.makeText(TransactionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            List<Category> finalCategories = categories;
            runOnUiThread(() -> {
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, finalCategories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                inputCategory.setAdapter(adapter);
            });
        });
    }

    private void fillTransactionsTable(String username) {
        executorService.execute(() -> {
            List<Transaction> transactionsData;
            try {
                transactionsData = transactionController.getTransactions(username);
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(TransactionsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                return;
            }

            if (transactionsData != null) {
                runOnUiThread(() -> {
                    transactionsTable.removeAllViews();


                    TableRow headerRow = new TableRow(this);
                    headerRow.setBackgroundColor(Color.LTGRAY);

                    String[] headers = {"Сумма", "Дата", "Тип", "Категория", "Действия"};
                    for (String header : headers) {
                        TextView headerTextView = new TextView(this);
                        headerTextView.setText(header);
                        headerTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        headerTextView.setTextSize(20);
                        headerTextView.setTextColor(Color.BLACK);
                        headerRow.addView(headerTextView);
                    }
                    transactionsTable.addView(headerRow);


                    for (Transaction transaction : transactionsData) {
                        TableRow tableRow = new TableRow(this);
                        tableRow.setBackgroundColor(Color.WHITE);

                        TextView amountTextView = new TextView(this);
                        amountTextView.setText(String.valueOf(transaction.getAmount()));
                        amountTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        tableRow.addView(amountTextView);

                        TextView dateTextView = new TextView(this);
                        dateTextView.setText(transaction.getDate().toString());
                        dateTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        tableRow.addView(dateTextView);

                        TextView typeTextView = new TextView(this);
                        typeTextView.setText(transaction.getIncome() ? "Доход" : "Расход");
                        typeTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        tableRow.addView(typeTextView);

                        TextView categoryTextView = new TextView(this);
                        categoryTextView.setText(transaction.getCategory().getName());
                        categoryTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        tableRow.addView(categoryTextView);

                        Button deleteButton = new Button(this);
                        deleteButton.setText("Удалить");
                        deleteButton.setBackgroundColor(Color.RED);
                        deleteButton.setTextColor(Color.WHITE);
                        deleteButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                        deleteButton.setOnClickListener(v -> {
                            executorService.execute(() -> {
                                Transaction transactionToDelete = new Transaction();
                                transactionToDelete.setId(transaction.getId());
                                String result = transactionController.deleteTransactions(username, transaction);
                                runOnUiThread(() -> {
                                    handleDeleteResult(result);
                                    fillTransactionsTable(username);
                                });
                            });
                        });

                        tableRow.addView(deleteButton);
                        transactionsTable.addView(tableRow);
                    }
                });
            }
        });
    }

    private void handleAddResult(String result) {
        Toast.makeText(TransactionsActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    private void handleDeleteResult(String result) {
        Toast.makeText(TransactionsActivity.this, result, Toast.LENGTH_SHORT).show();
    }
}
package com.example.mobileapp.ApiUtils;


import com.example.mobileapp.DTO.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class TransactionApi {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/transactions";
    private final OkHttpClient client = new OkHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    public TransactionApi() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * реквест на получение транз
     *
     * @param username ник
     * @return список транз юзера
     * @throws IOException
     */
    public List<Transaction> getTransactions(String username) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + username)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            return objectMapper.readValue(response.body().string(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Transaction.class));
        }
    }


    /**
     * реквест на добавление транзакции
     *
     * @param username    юзернейм
     * @param transaction для передачи
     * @return результат запроса
     * @throws IOException
     */
    public String addTransaction(String username, Transaction transaction) throws IOException {
        String url = BASE_URL + "/" + username;

        if (transaction.getCategory() != null) {
            transaction.getCategory().setId(transaction.getCategory().getId());
        }

        String json = objectMapper.writeValueAsString(transaction);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return "Транзакция добавлена";
            } else {
                return response.body().string();
            }
        }
    }


    /**
     * реквест на удаление транзакции
     *
     * @param username      ник
     * @param transactionId айди транзы
     * @return результат реквеста
     * @throws IOException
     */
    public String deleteTransaction(String username, Long transactionId) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + username + "/" + transactionId)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return "Транзакция удалена";
            } else {
                return response.body().string();
            }
        }
    }
}

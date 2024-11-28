package com.example.mobileapp.Controllers;

import com.example.mobileapp.ApiUtils.TransactionApi;
import com.example.mobileapp.DTO.Transaction;


import java.io.IOException;
import java.util.List;

public class TransactionController {
    private TransactionApi transactionApi = new TransactionApi();

    public List<Transaction> getTransactions(String username) throws IOException {
        return transactionApi.getTransactions(username);
    }

    public String addTransaction(String username, Transaction transaction) {
        try {
            return transactionApi.addTransaction(username, transaction);
        } catch (IOException e) {
            return "Не удалось добавить транзакцию";
        }
    }

    public String deleteTransactions(String username, Transaction transaction) {
        if (transaction == null) {
            return "Выберите транзакцию для удаления";
        }

        try {
            String responseMessage = transactionApi.deleteTransaction(username, transaction.getId());
            return responseMessage;
        } catch (IOException e) {
            return "Не удалось удалить транзакцию";
        }
    }

}

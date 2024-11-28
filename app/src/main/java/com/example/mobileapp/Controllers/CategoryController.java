package com.example.mobileapp.Controllers;

import android.util.Log;

import com.example.mobileapp.ApiUtils.CategoryApi;
import com.example.mobileapp.DTO.Category;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CategoryController {
    private CategoryApi categoryApi = new CategoryApi();

    public String addCategory(String categoryName, String currentUsername) {
        try {
            Category category = new Category();
            category.setName(categoryName);
            String responseMessage = categoryApi.addCategory(currentUsername, category);
            return responseMessage;
        } catch (IOException e) {
            return "Не удалось добавить категорию";
        }
    }

    public List<Category> getCategories(String username) throws IOException {
        return categoryApi.getCategories(username);
    }

    public String deleteCategory(String username, String categoryName) {
        try {
            String responseMessage = categoryApi.deleteCategory(username, categoryName);
            if (!Objects.equals(responseMessage, "Категория удалена")) {
                responseMessage = "Вы не можете удалить общую категорию";
            }
            return responseMessage;
        } catch (IOException e) {
            return "Не удалось удалить категорию";
        }
    }

}

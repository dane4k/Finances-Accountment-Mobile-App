package com.example.mobileapp.Controllers;


import com.example.mobileapp.ApiUtils.AuthApi;


public class AuthController {
    AuthApi authApi = new AuthApi();

    public String registerUser(String username, String password) {
        try {
            String responseMessage = authApi.addUser(username, password);
            return responseMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    /**
     * вход
     */
    public String logInUser(String username, String password) {
        try {
            String responseMessage = authApi.logInUser(username, password);
            return responseMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}


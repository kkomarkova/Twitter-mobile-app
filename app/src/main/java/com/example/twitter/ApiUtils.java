package com.example.twitter;

public class ApiUtils {
    
    private ApiUtils() {
    }

    private static final String BASE_URL = "https://anbo-restmessages.azurewebsites.net/api/";

    public static MessageService getMessageService() {
        return RetrofitClient.getClient(BASE_URL).create(MessageService.class);
    }
}

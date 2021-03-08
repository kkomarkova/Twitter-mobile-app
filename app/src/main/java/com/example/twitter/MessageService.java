package com.example.twitter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MessageService {
    @GET("messages")
    Call<List<Message>> getAllMessages();

    @GET("messages/{messageId}")
    Call<Message> getMessageById(@Path("id") int messageId);

   // @POST("books")
  //  @FormUrlEncoded
        // I had problems making this work. I used saveBookBody instead
   // Call<Message> saveBook(@Field("Id") String author, @Field("Title") String title,
                       // @Field("Publisher") String publisher, @Field("Price") double price);

    @POST("messages")
    Call<Message> saveMessageBody(@Body Message message);

    @DELETE("messages/{id}")
    Call<Message> deleteMessage(@Path("id") int id);

    @PUT("messages/{id}")
    Call<Message> updateMessage(@Path("id") int id, @Body Message message);
}

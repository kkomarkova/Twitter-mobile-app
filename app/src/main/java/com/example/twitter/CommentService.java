package com.example.twitter;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentService {
    @GET("messages/{messageId}/comments")
    Call<List<Comment>> getAllComments();
    @GET("messages/{messageId}/comments?user=andersb@gmail.com")
    Call<List<Comment>> getCommentsbyuser();
    @GET("messages/{messageId}/comments")
    Call<Message> getCommentById(@Path("id") int messageId);

    // @POST("books")
    //  @FormUrlEncoded
    // I had problems making this work. I used saveBookBody instead
    // Call<Message> saveBook(@Field("Id") String author, @Field("Title") String title,
    // @Field("Publisher") String publisher, @Field("Price") double price);

    @POST("comments")
    Call<Comment> saveCommentBody(@Body Comment comment);

    @DELETE("comments/{id}")
    Call<Comment> deleteComment(@Path("id") int id);

    @PUT("comments/{id}")
    Call<Comment> updateComment(@Path("id") int id, @Body Comment comment);
}

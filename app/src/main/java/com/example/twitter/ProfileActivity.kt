package com.example.twitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    val userfb: FirebaseUser = FirebaseAuth.getInstance().currentUser;
    val user: String = userfb.getEmail()
    private var theComment: Comment? = null
    private var theMessage: Message? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        // ProfileText.setText(getResources().getString(R.string.MyProfile) + " " + user)
    }
    override fun onStart() {
        super.onStart()
        getAndShowMyMessages();
    }

    fun getAndShowMyMessages() {
        val services = ApiUtils.getMessageService()
        val getMyMessagesCall = services.getMessagesByUser(user)
        getMyMessagesCall.enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                Log.d("message", response.raw().toString())
                if (response.isSuccessful) {
                    val allMessages = response.body()!!
                    Log.d("message", allMessages.toString())
                    populateRecycleView(allMessages)
                } else {
                    val message = "Problem " + response.code() + " " + response.message()
                    Log.d("message", "the problem is: $message")
                }
            }
            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Log.e("message", t.message!!)
            }
        })
    }
    private fun populateRecycleView(allMessages: List<Message>) {
        val recyclerView = findViewById<RecyclerView>(R.id.ProfileMessageRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerViewMessageAdapter(this, allMessages)
        recyclerView.adapter = adapter

    }
}

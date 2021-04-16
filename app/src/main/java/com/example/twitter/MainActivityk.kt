package com.example.twitter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivityk : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val i = Intent(this@MainActivityk, Allmessages::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out")
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        //updateUI(currentUser);
        Log.d("UserApple", "Current user$currentUser")
    }

    fun login(view: View?) {
        val emailView = findViewById<EditText>(R.id.activity_main_emailEditText)
        val passwordView = findViewById<EditText>(R.id.activity_main_passwordEditText)
        val email = emailView.text.toString().trim { it <= ' ' }
        val password = passwordView.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "All fields must be filled", Toast.LENGTH_LONG).show()
        } else {
            mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("UserApple", "signInWithEmail:success")
                            val user = mAuth!!.currentUser
                            val intent = Intent(baseContext, Allmessages::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("UserApple", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, task.exception!!.message,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    fun register(view: View?) {
        val emailView = findViewById<EditText>(R.id.activity_main_emailEditText)
        val passwordView = findViewById<EditText>(R.id.activity_main_passwordEditText)
        val email = emailView.text.toString().trim { it <= ' ' }
        val password = passwordView.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(applicationContext, "All fields must be filled", Toast.LENGTH_LONG).show()
        } else {
            mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("UserApple", "signInWithEmail:success")
                            val user = mAuth!!.currentUser
                            Toast.makeText(applicationContext, "Welcome to the Twitter" + user.email + "Please sign in", Toast.LENGTH_LONG).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("UserApple", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, task.exception!!.message,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    fun logout(view: View?) {
        FirebaseAuth.getInstance().signOut()
        val messageView = findViewById<TextView>(R.id.textViewmessage)
        messageView.text = "You are signed out"
    }

    companion object {
        private const val TAG = "LOGIN"
    }
}
package com.example.twitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        Log.d("UserApple", "Current user" + currentUser);

    }
    public void login(View view) {
        EditText emailView = findViewById(R.id.activity_main_emailEditText);
        EditText passwordView = findViewById(R.id.activity_main_passwordEditText);
        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_LONG).show();
        } else {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("UserApple", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent intent = new Intent(getBaseContext(), Allmessages.class);

                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("UserApple", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getBaseContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }

    }
    public void register(View view) {
        EditText emailView = findViewById(R.id.activity_main_emailEditText);
        EditText passwordView = findViewById(R.id.activity_main_passwordEditText);
        String email = emailView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_LONG).show();
        } else {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("UserApple", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "Welcome to the Twitter" + user.getEmail() + "Please sign in", Toast.LENGTH_LONG).show();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("UserApple", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getBaseContext(), task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }

    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        TextView messageView = findViewById(R.id.textViewmessage);
        messageView.setText("You are signed out");
    }
}

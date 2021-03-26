package com.example.twitter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Addnewmessage extends AppCompatActivity {
    private static final String LOG_TAG = "MYMESSAGES";
    private ProgressBar progressBar;
    private TextView messageView;
    private EditText insertideditText;
    private Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_message);
        progressBar = findViewById(R.id.addMessageProgressbar);
        messageView = findViewById(R.id.addMessageMessageTextView);
        FloatingActionButton fab = findViewById(R.id.user);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();
                //Add intent to add new Message
                Intent intent = new Intent(Addnewmessage.this, Messagebyuser.class);
                startActivity(intent);
            }
        });
        initView();
    }
    private void initView() {
        insertideditText = findViewById(R.id.insertideditText);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonDelete(view);
            }
        });
    }
    private void buttonDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("CONFIRM");
        builder.setMessage("ARE YOU SURE?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int id = Integer.parseInt(insertideditText.getText().toString());
                MessageService messageService = ApiUtils.getMessageService();

                messageService.deleteMessage(id).enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        try {
                            if (response.isSuccessful()) {
                                Toast.makeText(Addnewmessage.this,"Deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void addMessageButtonClicked(View view) {
       // EditText idField = findViewById(R.id.addMessageIdEditText);
        EditText contentField = findViewById(R.id.addMessageContentEditText);
        EditText userField = findViewById(R.id.addMessageUserEditText);
        //EditText totalcommentsField = findViewById(R.id.addMessageTotalCommentsEditText);

        //Integer id = idField.getText().length();
        // TODO check if author is empty string?
        String content = contentField.getText().toString().trim();
        String user = userField.getText().toString().trim();
       // Integer totalComments = totalcommentsField.getText().length();


        MessageService messageService = ApiUtils.getMessageService();

        // Call<Book> saveBookCall = bookStoreService.saveBook(author, title, publisher, price);
        Message message = new Message(content,user);

        Call<Message> saveMessageCall = messageService.saveMessageBody(message);
        progressBar.setVisibility(View.VISIBLE);
        saveMessageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    Message theNewMessage = response.body();
                    Log.d(LOG_TAG, theNewMessage.toString());
                    Toast.makeText(Addnewmessage.this, "Message added, id: " + theNewMessage.getId(), Toast.LENGTH_SHORT).show();
//                    Snackbar.make(view, "Book added, id: " + theNewBook.getId(), Snackbar.LENGTH_LONG).show();
                } else {
                    String problem = "Problem: " + response.code() + " " + response.message();
                    Log.e(LOG_TAG, problem);
                    messageView.setText("Problem");
                    //Toast.makeText(AddBookActivity.this, problem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                messageView.setText(t.getMessage());
                Log.e(LOG_TAG, t.getMessage());

            }
        });
    }

}

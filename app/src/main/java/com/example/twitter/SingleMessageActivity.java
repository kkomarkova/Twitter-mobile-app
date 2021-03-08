package com.example.twitter;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleMessageActivity  extends AppCompatActivity  {
    public static final String MESSAGE = "MESSAGE";
    private static final String LOG_TAG = "MYMESSAGES";
    private Message originalMessage;
    private TextView messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_message);
        messageView = findViewById(R.id.singleMessageMessageTextView);

        Intent intent = getIntent();
        originalMessage = (Message) intent.getSerializableExtra(MESSAGE);

        Log.d(LOG_TAG, originalMessage.toString());
        TextView headingView = findViewById(R.id.singleMessageHeadingTextview);
        headingView.setText("Message Id=" + originalMessage.getId());

        EditText contentView = findViewById(R.id.singleMessageContentEditText);
        contentView.setText(originalMessage.getContent());

        EditText userView = findViewById(R.id.singleMessageUserEditText);
        userView.setText(originalMessage.getUser());

        EditText totalcommentsView = findViewById(R.id.singleBookTotalCommentEditText);
        totalcommentsView.setText(originalMessage.getTotalComments());

    }
    public void backButtonClicked(View view) {
        Log.d(LOG_TAG, "backButtonClicked");
        finish();
    }

    public void deleteBookButtonClicked(View view) {
        MessageService MessageService = ApiUtils.getMessageService();
        int messageId = originalMessage.getId();
        Call<Message> deleteMessageCall = MessageService.deleteMessage(messageId);
        messageView.setText("");

        deleteMessageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    //Snackbar.make(view, "Book deleted, id: " + originalBook.getId(), Snackbar.LENGTH_LONG).show();
                    String message = "Message deleted, id: " + originalMessage.getId();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, message);
                } else {
                    //Snackbar.make(view, "Problem: " + response.code() + " " + response.message(), Snackbar.LENGTH_LONG).show();
                    String problem = call.request().url() + "\n" + response.code() + " " + response.message();
                    messageView.setText(problem);
                    //Toast.makeText(getBaseContext(), problem, Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, problem);
                }
            }
            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                //Snackbar.make(view, "Problem: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                Log.e(LOG_TAG, "Problem: " + t.getMessage());
            }
        });
    }
    public void updateButtonClicked(View view) {
        Log.d(LOG_TAG, "anotherButtonClicked");
        Toast.makeText(this, "anotherButtonClicked", Toast.LENGTH_SHORT).show();

        EditText idField = findViewById(R.id.singleMessageIdEditText);
        EditText contentField = findViewById(R.id.singleMessageContentEditText);
        EditText userField = findViewById(R.id.singleMessageUserEditText);
        EditText totalcommentsField = findViewById(R.id.singleBookTotalCommentEditText);
        // REST bug: price cannot be updated!

        Integer id = idField.getText().length();
        // TODO check if author is empty string?
        String content = contentField.getText().toString().trim();
        String user = userField.getText().toString().trim();
        Integer totalComments = totalcommentsField.getText().length();



        //double price;
        //try {
           // price = Double.parseDouble(priceString);
        //} catch (NumberFormatException ex) {
          //  priceField.setError("Not a valid price");
         //   return;
        //}
        Message messageToUpdate = new Message(id,content,user,totalComments);
        Log.d(LOG_TAG, "Update " + messageToUpdate);

        MessageService MessageService = ApiUtils.getMessageService();
        Call<Message> callUpdate = MessageService.updateMessage(originalMessage.getId(), messageToUpdate);
        callUpdate.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, response.body().toString());
                    messageView.setText("Updated " + response.body().toString());
                } else {
                    messageView.setText("Problem: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                messageView.setText("Problem: " + t.getMessage());
            }
        });
    }
}

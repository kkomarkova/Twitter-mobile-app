package com.example.twitter;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Allmessages extends AppCompatActivity {
    private static final String LOG_TAG = "MYMESSAGES";

    private TextView messageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmessages);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Find by id
        messageView = findViewById(R.id.mainMessageTextView);
        progressBar = findViewById(R.id.mainProgressbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();
                //Add intent to add new Message

                Intent intent = new Intent(Allmessages.this, Addnewmessage.class);

                startActivity(intent);
            }
        });
        SwipeRefreshLayout refreshLayout = findViewById(R.id.mainSwiperefresh);
        refreshLayout.setOnRefreshListener(() -> {
            getAndShowAllMessages();
            refreshLayout.setRefreshing(false);
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        getAndShowAllMessages();
    }
    private void getAndShowAllMessages() {
        MessageService bookStoreService = ApiUtils.getMessageService();
        Call<List<Message>> getAllMessagesCall = bookStoreService.getAllMessages();
        messageView.setText("");
        progressBar.setVisibility(View.VISIBLE);

        getAllMessagesCall.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                //ResponseBody body = response.raw();
                Log.d(LOG_TAG, response.raw().toString());
                /*try {
                    Thread.sleep(5000);
                    // sleep a little to get a chance to see the progressbar in action
                    // don't do this at home
                } catch (InterruptedException e) {
                }*/
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    List<Message> allMessages = response.body();
                    Log.d(LOG_TAG, allMessages.toString());
                    populateRecyclerView(allMessages);
                } else {
                    String message = "Problem " + response.code() + " " + response.message();
                    Log.d(LOG_TAG, message);
                    messageView.setText(message);
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(LOG_TAG, t.getMessage());
                messageView.setText(t.getMessage());
            }
        });
    }
    private void populateRecyclerView(List<Message> allMessages) {
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewMessageAdapter adapter = new RecyclerViewMessageAdapter(this, allMessages);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener((view, position, item) -> {
            Message message = (Message) item;
            Log.d(LOG_TAG, item.toString());
            Log.d(LOG_TAG, "putExtra " + message.toString());
            Intent intent = new Intent(this, CommentActivity.class);
            intent.putExtra(CommentActivity.MESSAGE, message);
            startActivity(intent);

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
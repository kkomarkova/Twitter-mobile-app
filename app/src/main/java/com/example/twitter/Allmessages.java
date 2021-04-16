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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.MotionEvent;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Allmessages extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final String LOG_TAG = "MYMESSAGES";
    private static final String TAG = "GESTURES";
    private GestureDetector gestureDetector;

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
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log.d(TAG, "onTuch: " + event);
        // boolean eventHandlingFinished = true;
        //return eventHandlingFinished;
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //Toast.makeText(this, "onDown", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //Toast.makeText(this, "onShowPress", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onSingleTapUp");
        return true; // done
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //Toast.makeText(this, "onScroll", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onScroll");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //Toast.makeText(this, "onLongPress", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onLongPress");
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //Toast.makeText(this, "onFling", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onFling " + e1.toString() + "::::" + e2.toString());

        boolean leftSwipe = e1.getX() > e2.getX();
        Log.d(TAG, "onFling left: " + leftSwipe);
        if (leftSwipe) {
            Intent intent = new Intent(this, CommentActivity.class);
            startActivity(intent);
            //ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
            //Bundle options = activityOptionsCompat.toBundle();
            //startActivity(intent, options);

        }
        return true; // done
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
        switch (item.getItemId()) {
            case R.id.action_menu1:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_menu2:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
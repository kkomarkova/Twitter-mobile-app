package com.example.twitter;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Commentbyuser extends AppCompatActivity {

        private static final String LOG_TAG = "MYCOMMENTS";

        private TextView messageView;
        private ProgressBar progressBar;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_messagebyuser);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //Find by id
            messageView = findViewById(R.id.userMessageTextView);
            progressBar = findViewById(R.id.userProgressbar);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            SwipeRefreshLayout refreshLayout = findViewById(R.id.userSwiperefresh);
            refreshLayout.setOnRefreshListener(() -> {
                getAndShowcommentsbyuser();
                refreshLayout.setRefreshing(false);
            });

        }

        @Override
        protected void onStart() {
            super.onStart();
            getAndShowcommentsbyuser();
        }

        private void getAndShowcommentsbyuser() {
            MessageService bookStoreService = ApiUtils.getMessageService();
            Call<List<Comment>> getCommentsbyuserCall= bookStoreService.getCommentById(1);
            messageView.setText("");
            progressBar.setVisibility(View.VISIBLE);
            getCommentsbyuserCall.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
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
                        List<Comment> Commentsbyuser = response.body();
                        Log.d(LOG_TAG, Commentsbyuser.toString());
                        commentRecyclerView(Commentsbyuser);
                    } else {
                        String message = "Problem " + response.code() + " " + response.message();
                        Log.d(LOG_TAG, message);
                        messageView.setText(message);
                    }
                }
                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.e(LOG_TAG, t.getMessage());
                    messageView.setText(t.getMessage());
                }
            });
        }

        private void commentRecyclerView(List<Comment> Commentsbyuser) {
            RecyclerView recyclerView = findViewById(R.id.usercommentRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            RecyclerViewSimpleAdapter<Comment> adapter = new RecyclerViewSimpleAdapter<>(Commentsbyuser);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener((view, position, item) -> {
                Comment comment = (Comment) item;
                Log.d(LOG_TAG, item.toString());

                Log.d(LOG_TAG, "putExtra " + comment.toString());

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

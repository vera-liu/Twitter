package com.example.vera_liu.twitter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vera_liu.twitter.adapters.TweetsAdapter;
import com.example.vera_liu.twitter.models.Tweet;
import com.example.vera_liu.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity implements ComposeTweetFragment.ComposeTweetListener  {
    private TwitterClient client;
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    private RecyclerView recyclerView;
    private TweetsAdapter tweetsAdapter;
    private User currentUser;
    public void loadNextDataFromApi(final boolean newQuery) {
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("refresh", "success");
                if (newQuery) {
                    tweets.clear();
                }
                try {
                    for(int i=0; i<response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        JSONObject userObj = object.getJSONObject("user");



                        User user = new User(
                                userObj.getString("name"),
                                userObj.getString("screen_name"),
                                userObj.getString("profile_image_url"),
                                userObj.getString("description"),
                                userObj.getInt("followers_count"),
                                userObj.getInt("friends_count"),
                                userObj.getLong("id"));
                        user.save();
                        Tweet tweet = new Tweet(
                                object.getString("text"),
                                object.getString("created_at"),
                                user,
                                object.getLong("id"));
                        tweet.save();
                        tweets.add(tweet);
                    }
                    tweetsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("error", e.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tweetsAdapter = new TweetsAdapter(this, tweets);
        recyclerView = (RecyclerView) findViewById(R.id.tweetsView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);;
        recyclerView.setAdapter(tweetsAdapter);
        recyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataFromApi(false);
                // or loadNextDataFromApi(totalItemsCount);
            }
        });
        client = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, this);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        loadNextDataFromApi(true);
        getCurrentUser();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    private void getCurrentUser() {
        client.getCurrentUserInfo(new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject userObj) {
                try {
                    currentUser = new User(
                            userObj.getString("name"),
                            userObj.getString("screen_name"),
                            userObj.getString("profile_image_url"),
                            userObj.getString("description"),
                            userObj.getInt("followers_count"),
                            userObj.getInt("friends_count"),
                            userObj.getLong("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("error", e.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.composeTweet) {
            FragmentManager fm = getSupportFragmentManager();
            ComposeTweetFragment editFragment = ComposeTweetFragment.newInstance(currentUser);
            editFragment.show(fm,"fragment_compose_tweet");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPost(String tweet) {
        client.postTweet(tweet, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getBaseContext(), "Posted!", Toast.LENGTH_SHORT);
                loadNextDataFromApi(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("error", e.toString());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}

package com.example.vera_liu.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vera_liu.twitter.adapters.TweetsPagerAdapter;
import com.example.vera_liu.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements ComposeTweetFragment.ComposeTweetListener {
    private TwitterClient client;
    private User currentUser;
    TweetsPagerAdapter adapterViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, this);
        setContentView(R.layout.activity_main);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getCurrentUser();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
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
    public void onUserClicked(User user) {
        Intent i = new Intent(this, UserProfileActivity.class).putExtra("USER", user.getUserId());
        startActivity(i);
    }

    @Override
    public void onPost(String tweet) {
        client.postTweet(tweet, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before re quest is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String fragmentTag = "android:switcher:" + R.id.vpPager + ":" + "0";
                TweetsFragment fragment = (TweetsFragment) adapterViewPager.getRegisteredFragment(0);
                fragment.loadNextDataFromApi(true);
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

package com.example.vera_liu.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserProfileActivity extends AppCompatActivity {
    private long userId;
    private TwitterClient client;
    private ImageView profileImage;
    private TextView userNameTv, userDsptTv, userFollowers, userFollowings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        userId = intent.getLongExtra("USER", -1);
        client = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, this);
        if (userId != -1) {
            getUserInfo();
        }
        profileImage = (ImageView) findViewById(R.id.userProfile);
        userNameTv = (TextView) findViewById(R.id.otherUserName);
        userDsptTv = (TextView) findViewById(R.id.userDescription);
        userFollowers = (TextView) findViewById(R.id.userFollowers);
        userFollowings = (TextView) findViewById(R.id.userFollowing);
    }
    private void getUserInfo() {
        client.getUserInfo(userId, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject userObj) {
                try {
                    Picasso.with(UserProfileActivity.this).load(userObj.getString("profile_image_url"))
                            .fit().centerCrop().placeholder(R.mipmap.ic_launcher)
                            .into(profileImage);
                    userNameTv.setText(userObj.getString("name"));
                    userDsptTv.setText(userObj.getString("description"));
                    userFollowers.setText("Followers: " + Integer.toString(userObj.getInt("followers_count")));
                    userFollowings.setText("Friends: " + Integer.toString(userObj.getInt("friends_count")));
                    UserTimelineFragment fragment = UserTimelineFragment.newInstance(userId);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.userTimelineContainer, fragment);
                    ft.commit();
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
}

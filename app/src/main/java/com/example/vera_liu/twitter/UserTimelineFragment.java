package com.example.vera_liu.twitter;

import android.os.Bundle;
import android.util.Log;

import com.example.vera_liu.twitter.models.Tweet;
import com.example.vera_liu.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserTimelineFragment extends TweetsFragment {

    private long userId;

    // TODO: Rename and change types and number of parameters
    public static UserTimelineFragment newInstance(long userId) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("USER_TIMELINE", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getLong("USER_TIMELINE");
    }


    @Override
    public void loadNextDataFromApi(final boolean newQuery) {
        client.getUserTimeline(userId, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
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
}

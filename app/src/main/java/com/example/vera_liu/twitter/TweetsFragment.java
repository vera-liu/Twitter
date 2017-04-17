package com.example.vera_liu.twitter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vera_liu.twitter.adapters.TweetsAdapter;
import com.example.vera_liu.twitter.models.Tweet;
import com.example.vera_liu.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TweetsFragment extends Fragment implements ComposeTweetFragment.ComposeTweetListener  {
    protected TwitterClient client;
    protected ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    protected RecyclerView recyclerView;
    protected TweetsAdapter tweetsAdapter;
    public static TweetsFragment newInstance() {
        return new TweetsFragment();
    }
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
    public void onAttach(Context context) {
        super.onAttach(context);
        client = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
        recyclerView = (RecyclerView) view.findViewById(R.id.tweetsView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadNextDataFromApi(true);
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

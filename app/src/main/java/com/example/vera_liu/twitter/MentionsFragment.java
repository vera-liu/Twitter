package com.example.vera_liu.twitter;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.vera_liu.twitter.models.Tweet;
import com.example.vera_liu.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MentionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MentionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MentionsFragment extends TweetsFragment {

    private OnFragmentInteractionListener mListener;

    public MentionsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MentionsFragment newInstance() {
        MentionsFragment fragment = new MentionsFragment();
        return fragment;
    }

    @Override
    public void loadNextDataFromApi(final boolean newQuery) {
        client.getMentions(1, new JsonHttpResponseHandler() {

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package com.example.vera_liu.twitter;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.vera_liu.twitter.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by vera_liu on 4/9/17.
 */

public class ComposeTweetFragment extends DialogFragment implements Button.OnClickListener {
    private User currentUser;
    private TwitterClient client;
    private EditText editTweet;
    private Button postBtn;
    private ImageView userImage;
    public ComposeTweetFragment() {
        // Required empty public constructor
    }
    public interface ComposeTweetListener {
        void onPost(String tweet);
    }
    public static ComposeTweetFragment newInstance(User user) {
        ComposeTweetFragment fragment = new ComposeTweetFragment();
        Bundle args = new Bundle();
        args.putParcelable("USER", user);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentUser = getArguments().getParcelable("USER");
        }
        client = (TwitterClient) TwitterClient.getInstance(TwitterClient.class, getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        editTweet = (EditText) view.findViewById(R.id.edit_tweet);
        Log.d("fragment", editTweet.getText().toString());
        postBtn = (Button) view.findViewById(R.id.postBtn);
        postBtn.setOnClickListener(this);
        userImage = (ImageView) view.findViewById(R.id.owner_profile);
        Picasso.with(getContext()).load(currentUser.getProfile_image_url())
                .fit().centerCrop().placeholder(R.mipmap.ic_launcher)
                .into(userImage);
        return view;
    }
    @Override
    public void onClick(View v) {
        ComposeTweetListener listener = (ComposeTweetListener) getActivity();
        String tweetToPost = editTweet.getText().toString();
        listener.onPost(tweetToPost);
        dismiss();
    }
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x), (int) (size.y));
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}

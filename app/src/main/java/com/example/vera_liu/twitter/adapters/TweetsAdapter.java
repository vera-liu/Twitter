package com.example.vera_liu.twitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vera_liu.twitter.MainActivity;
import com.example.vera_liu.twitter.R;
import com.example.vera_liu.twitter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    private ArrayList<Tweet> tweets;
    private Context mContext;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvUserName;
        public ImageView profileImage;
        public TextView tvTimeStamp;
        public TextView tvTweet;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            final View iView = itemView;
            iView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(iView, position);
                        }
                    }
                }
            });
            tvUserName = (TextView) itemView.findViewById(R.id.userName);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
            tvTweet = (TextView) itemView.findViewById(R.id.tweetText);
        }
    }
    public TweetsAdapter(Context context, ArrayList<Tweet> tweets) {
        this.tweets = tweets;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View articleView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Tweet tweet = tweets.get(position);
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        String timestamp = getRelativeTimeAgo(tweet.getCreated_at());
        viewHolder.tvTimeStamp.setText(timestamp);
        viewHolder.tvTweet.setText(tweet.getText());

            Picasso.with(mContext).load(tweet.getUser().getProfile_image_url())
                    .fit().centerCrop().placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.profileImage);
        viewHolder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).onUserClicked(tweet.getUser());
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return tweets.size();
    }
}


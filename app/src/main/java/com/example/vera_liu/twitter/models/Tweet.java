package com.example.vera_liu.twitter.models;
import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Tweets")
public class Tweet extends Model implements Parcelable {

    @Column(name="text")
    protected String mText;

    @Column(name="created_at")
    protected String mCreated_at;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    protected User mUser;

    @Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    protected long mTweetId;

    @Column(name="origin")
    protected String mOrigin;

    public Tweet(
            String text,
            String created_at,
            User user,
            long id) {
        super();
        mText = text;
        mCreated_at = created_at;
        mUser = user;
        mTweetId = id;
    }

    public Tweet() {
    }

    public String getText() { return mText; }

    public void setText(String value) { mText = value; }

    public String getCreated_at() { return mCreated_at; }

    public void setCreated_at(String value) { mCreated_at = value; }

    public User getUser() { return mUser; }

    public void setUser(User value) { mUser = value; }

    public long getTweetId() { return mTweetId; }

    public void setTweetId(long value) { mTweetId = value; }

    public String getOrigin() { return mOrigin; }

    public void setOrigin(String origin) { mOrigin = origin; }

    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mText);
        out.writeString(mCreated_at);
        out.writeParcelable(mUser, 0);
        out.writeLong(mTweetId);
        out.writeString(mOrigin);
    }

    private Tweet(Parcel in){
        mText = in.readString();
        mCreated_at = in.readString();
        mUser = in.readParcelable(User.class.getClassLoader());
        mTweetId = in.readLong();
        mOrigin = in.readString();
    }
    public static final Creator<Tweet> CREATOR
            = new Creator<Tweet>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

}
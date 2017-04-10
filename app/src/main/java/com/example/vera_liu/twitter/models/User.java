package com.example.vera_liu.twitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model implements Parcelable {

    @Column(name="name")
    protected String mName;
    @Column(name="screen_name")
    protected String mScreen_name;
    @Column(name="profile_image_url")
    protected String mProfile_image_url;
    @Column(name="description")
    protected String mDescription;
    @Column(name="followers_count")
    protected int mFollowers_count;
    @Column(name="friends_count")
    protected int mFriends_count;

    @Column(name = "user_id")
    protected long mUserId;

    public User(
            String name,
            String screen_name,
            String profile_image_url,
            String description,
            int followers_count,
            int friends_count,
            long id) {
        super();
        mName = name;
        mScreen_name = screen_name;
        mProfile_image_url = profile_image_url;
        mDescription = description;
        mFollowers_count = followers_count;
        mFriends_count = friends_count;
        mUserId = id;
    }

    public User() {
        super();
    }

    public String getName() { return mName; }

    public void setName(String value) { mName = value; }

    public String getScreen_name() { return mScreen_name; }

    public void setScreen_name(String value) { mScreen_name = value; }

    public String getProfile_image_url() { return mProfile_image_url; }

    public void setProfile_image_url(String value) { mProfile_image_url = value; }

    public String getDescription() { return mDescription; }

    public void setDescription(String value) { mDescription = value; }

    public int getFollowers_count() { return mFollowers_count; }

    public void setFollowers_count(int value) { mFollowers_count = value; }

    public int getFriends_count() { return mFriends_count; }

    public void setFriends_count(int value) { mFriends_count = value; }

    public long getUserId() { return mUserId; }

    public void setUserId(long value) { mUserId = value; }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeString(mScreen_name);
        out.writeString(mProfile_image_url);
        out.writeString(mDescription);
        out.writeInt(mFollowers_count);
        out.writeInt(mFriends_count);
        out.writeLong(mUserId);
    }

    private User(Parcel in){
        mName = in.readString();
        mScreen_name = in.readString();
        mProfile_image_url = in.readString();
        mDescription = in.readString();
        mFollowers_count = in.readInt();
        mFriends_count = in.readInt();
        mUserId = in.readLong();
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}


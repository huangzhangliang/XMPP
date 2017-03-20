package com.leon.chat.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leon on 17/2/13.
 */

public class SessionBean implements Parcelable{

    public String account;
    public String nickname;
    public String avatar;
    public String pinyin;
    public String message;

    public SessionBean() {}

    protected SessionBean(Parcel in) {
        account = in.readString();
        nickname = in.readString();
        avatar = in.readString();
        pinyin = in.readString();
        message = in.readString();
    }

    public static final Creator<SessionBean> CREATOR = new Creator<SessionBean>() {
        @Override
        public SessionBean createFromParcel(Parcel in) {
            return new SessionBean(in);
        }

        @Override
        public SessionBean[] newArray(int size) {
            return new SessionBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account);
        dest.writeString(nickname);
        dest.writeString(avatar);
        dest.writeString(pinyin);
        dest.writeString(message);
    }
}

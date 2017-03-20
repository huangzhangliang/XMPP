package com.leon.chat.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leon on 17/2/8.
 */

public class ContactBean implements Parcelable {
    public String account;
    public String nickname;
    public String avatar;
    public String pinyin;

    public ContactBean(){}

    protected ContactBean(Parcel in) {
        account = in.readString();
        nickname = in.readString();
        avatar = in.readString();
        pinyin = in.readString();
    }

    public static final Creator<ContactBean> CREATOR = new Creator<ContactBean>() {
        @Override
        public ContactBean createFromParcel(Parcel in) {
            return new ContactBean(in);
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
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
    }
}

package com.leon.chat;


import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.leon.chat.dbhelper.ContactOpenHelper;
import com.leon.chat.provider.ContactsProvider;
import com.leon.chat.utils.LogUtils;


/**
 * Created by leon on 17/2/7.
 */

public class TestContactProvider extends AndroidTestCase{

    public void testInsert(){
        /**
         *  public static final String ACCOUNT = "account";
            public static final String NICKNAME = "nickname";
            public static final String AVATAR = "avatar";
            public static final String PINYIN = "pinyin";
         * */

        ContentValues values = new ContentValues();
        values.put(ContactOpenHelper.ContactTable.ACCOUNT,"billy@hzlleon.com");
        values.put(ContactOpenHelper.ContactTable.NICKNAME,"老伍");
        values.put(ContactOpenHelper.ContactTable.AVATAR,"0");
        values.put(ContactOpenHelper.ContactTable.PINYIN,"laowu");
        getContext().getContentResolver().insert(ContactsProvider.URI_CONTACT,values);
    }

    public void testDelete(){
        getContext().getContentResolver().delete(ContactsProvider.URI_CONTACT,ContactOpenHelper.ContactTable.ACCOUNT + "=?",
                new String[]{"billy@hzlleon.com"});
    }
    public void testUpdate(){
        ContentValues values = new ContentValues();
        values.put(ContactOpenHelper.ContactTable.ACCOUNT,"billy@hzlleon.com");
        values.put(ContactOpenHelper.ContactTable.NICKNAME,"我是老伍");
        values.put(ContactOpenHelper.ContactTable.AVATAR,"0");
        values.put(ContactOpenHelper.ContactTable.PINYIN,"woshilaowu");
        getContext().getContentResolver().update(ContactsProvider.URI_CONTACT,values,ContactOpenHelper.ContactTable.ACCOUNT + "=?",
                new String[]{"billy@hzlleon.com"});
    }

    public void testQuery(){
        Cursor cursor = getContext().getContentResolver().query(ContactsProvider.URI_CONTACT,null,null,null,null);
        int columnCount = cursor.getColumnCount();
        while (cursor.moveToNext()){
            for (int i = 0;i<columnCount;i++){
                LogUtils.sf(cursor.getString(i) + "    ");
            }
            LogUtils.sf("");
        }
    }
}

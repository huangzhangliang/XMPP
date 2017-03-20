package com.leon.chat;


import android.content.ContentValues;
import android.database.Cursor;

import com.leon.chat.dbhelper.ContactOpenHelper;
import com.leon.chat.provider.ContactsProvider;
import com.leon.chat.utils.LogUtils;

import org.junit.Before;
import org.junit.Test;


/**
 * Created by leon on 17/2/7.
 */

public class ContactProviderUnitTest {

    ContactsProvider mContactsProvider;
    @Before
    public void init(){
        mContactsProvider = new ContactsProvider();
    }

    @Test
    public void testInsert() throws Exception{
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
        mContactsProvider.insert(ContactsProvider.URI_CONTACT,values);
    }

    @Test
    public void testDelete(){
        mContactsProvider.delete(ContactsProvider.URI_CONTACT,ContactOpenHelper.ContactTable.ACCOUNT + "=?",
                new String[]{"billy@hzlleon.com"});
    }
    @Test
    public void testUpdate(){
        ContentValues values = new ContentValues();
        values.put(ContactOpenHelper.ContactTable.ACCOUNT,"billy@hzlleon.com");
        values.put(ContactOpenHelper.ContactTable.NICKNAME,"我是老伍");
        values.put(ContactOpenHelper.ContactTable.AVATAR,"0");
        values.put(ContactOpenHelper.ContactTable.PINYIN,"woshilaowu");
        mContactsProvider.update(ContactsProvider.URI_CONTACT,values,ContactOpenHelper.ContactTable.ACCOUNT + "=?",
                new String[]{"billy@hzlleon.com"});
    }

    @Test
    public void testQuery(){
        Cursor cursor = mContactsProvider.query(ContactsProvider.URI_CONTACT,null,null,null,null);
        int columnCount = cursor.getColumnCount();
        while (cursor.moveToNext()){
            for (int i = 0;i<columnCount;i++){
                LogUtils.sf(cursor.getString(i) + "    ");
            }
            LogUtils.sf("");
        }
    }
}

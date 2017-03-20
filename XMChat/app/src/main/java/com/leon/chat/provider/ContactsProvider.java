package com.leon.chat.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.leon.chat.dbhelper.ContactOpenHelper;
import com.leon.chat.utils.LogUtils;

/**
 * Created by leon on 17/2/7.
 */

public class ContactsProvider extends ContentProvider {

    // 主机地址的常量 -> 当前类的完整路径
    public static final String AUTHORITIES = ContactsProvider.class.getCanonicalName();
    // 地址匹配对象
    static UriMatcher mUriMatcher;

    // 对应联系人表的一个uri常量
    public static Uri URI_CONTACT = Uri.parse("content://" + AUTHORITIES+"/contact");
    private static final int CONTACT = 1;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 添加一个匹配的规则 content://com.leon.chat.provider.ContactsProvider/contact->CONTACT
        mUriMatcher.addURI(AUTHORITIES,"/contact",CONTACT);
    }

    private ContactOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new ContactOpenHelper(getContext());
        if (mHelper != null){
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        int code = mUriMatcher.match(uri);
        switch (code){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                cursor = db.query(ContactOpenHelper.T_CONTACT,projection,selection,selectionArgs,null,null,sortOrder);
                LogUtils.sf("ContactsProvider querySuccess");
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // 数据存到SQLite->创建db文件
        int code = mUriMatcher.match(uri);
        switch (code){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                long id = db.insert(ContactOpenHelper.T_CONTACT,"",values);
                if (id != -1){
                    LogUtils.sf("ContactsProvider insertSuccess");
                    // 拼接最新uri
                    uri = ContentUris.withAppendedId(uri,id);
                    getContext().getContentResolver().notifyChange(URI_CONTACT,null);
                }
            break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        int deleteCount = 0;
        switch (code){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                deleteCount = db.delete(ContactOpenHelper.T_CONTACT,selection,selectionArgs);
                if (deleteCount > 0){
                    LogUtils.sf("ContactsProvider deleteSuccess");
                    getContext().getContentResolver().notifyChange(URI_CONTACT,null);
                }
                break;
        }
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        int updateCount = 0;
        switch (code){
            case CONTACT:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                updateCount = db.update(ContactOpenHelper.T_CONTACT,values,selection,selectionArgs);
                if (updateCount > 0){
                    LogUtils.sf("ContactsProvider updateSuccess");
                    getContext().getContentResolver().notifyChange(URI_CONTACT,null);
                }
                break;
        }
        return updateCount;
    }

}

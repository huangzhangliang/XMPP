package com.leon.chat.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.leon.chat.dbhelper.SmsOpenHelper;
import com.leon.chat.utils.LogUtils;

/**
 * Created by leon on 17/2/10.
 */

public class SmsProvider extends ContentProvider {

    // 主机地址的常量 -> 当前类的完整路径
    public static final String AUTHORITIES = SmsProvider.class.getCanonicalName();
    // 地址匹配对象
    static UriMatcher mUriMatcher;

    public static Uri URI_SMS = Uri.parse("content://" + AUTHORITIES+"/sms");
    public static Uri URI_SESSION = Uri.parse("content://" + AUTHORITIES+"/session");

    private static final int SMS = 1;
    private static final int SESSION = 2;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 添加一个匹配的规则 content://com.leon.chat.provider.SmsProvider/sms->CONTACT
        mUriMatcher.addURI(AUTHORITIES,"/sms",SMS);
        mUriMatcher.addURI(AUTHORITIES,"/session",SESSION);
    }

    private SmsOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new SmsOpenHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        int code = mUriMatcher.match(uri);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        switch (code){
            case SMS:
                cursor = db.query(SmsOpenHelper.T_SMS,projection,selection,selectionArgs,null,null,sortOrder);
                LogUtils.sf("SmsProvider querySuccess");
                break;
            case SESSION:
//                cursor = db.query(SmsOpenHelper.T_SMS,projection,selection,selectionArgs,null,null,sortOrder);
                cursor = db.rawQuery("SELECT * FROM (SELECT * FROM t_sms WHERE from_account = ? OR to_account = ? ORDER BY time ASC) GROUP BY session_account;",selectionArgs);
                LogUtils.sf("SmsProvider Session querySuccess");
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
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id;
        switch (code){
            case SMS:
                LogUtils.sf("ContentValues"+ values.getAsString("from_account"));
                id = db.insert(SmsOpenHelper.T_SMS,"",values);
                if (id != -1){
                    LogUtils.sf("SmsProvider insertSuccess");
                    // 拼接最新uri
                    uri = ContentUris.withAppendedId(uri,id);
                    getContext().getContentResolver().notifyChange(URI_SMS,null);
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
            case SMS:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                deleteCount = db.delete(SmsOpenHelper.T_SMS,selection,selectionArgs);
                if (deleteCount > 0){
                    LogUtils.sf("SmsProvider deleteSuccess");
                    getContext().getContentResolver().notifyChange(URI_SMS,null);
                }
                break;
            case SESSION:
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int code = mUriMatcher.match(uri);
        int updateCount = 0;
        switch (code){
            case SMS:
                SQLiteDatabase db = mHelper.getWritableDatabase();
                updateCount = db.update(SmsOpenHelper.T_SMS,values,selection,selectionArgs);
                if (updateCount > 0){
                    LogUtils.sf("SmsProvider updateSuccess");
                    getContext().getContentResolver().notifyChange(URI_SMS,null);
                }
                break;
        }
        return 0;
    }


}

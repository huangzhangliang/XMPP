package com.leon.chat.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by leon on 17/2/10.
 */

public class SmsOpenHelper extends SQLiteOpenHelper {
    public static final String T_SMS = "t_sms";

    public class SmsTable implements BaseColumns { // BaseColumns默认添加_ID
        /**
         * 1. _id:主键
         * 2. from_account:接收者账号
         * 3. to_account:发送者账号
         * 4. body:消息内容
         * 5. status:状态
         * 6. type:消息类型
         * 7. time:发送时间
         * 8. session_account:会话id:->最近和哪些人聊天
         * */

        public static final String FROM_ACCOUNT = "from_account";
        public static final String TO_ACCOUNT = "to_account";
        public static final String BODY = "body";
        public static final String STATUS = "status";
        public static final String MESSAGE_TYPE = "message_type";
        public static final String TIME = "time";
        public static final String SESSION_ACCOUNT = "session_account";

    }

    public SmsOpenHelper(Context context) {
        super(context, "sms.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + T_SMS + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                SmsTable.FROM_ACCOUNT + " TEXT,"+
                SmsTable.TO_ACCOUNT + " TEXT,"+
                SmsTable.BODY + " TEXT,"+
                SmsTable.STATUS + " TEXT,"+
                SmsTable.MESSAGE_TYPE + " TEXT,"+
                SmsTable.TIME + " TEXT,"+
                SmsTable.SESSION_ACCOUNT + " TEXT"+
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

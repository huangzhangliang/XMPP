package com.leon.chat.utils;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * Created by leon on 17/2/7.
 */

public class PinyinUtils {

    public static String getPinyin(String str){
        if (str == null || !str.equals("")){
            return "";
        }
        //PinyinHelper.convertToPinyinString("内容","分割符","拼音的格式");
        return PinyinHelper.convertToPinyinString(str,"", PinyinFormat.WITHOUT_TONE);
    }

}

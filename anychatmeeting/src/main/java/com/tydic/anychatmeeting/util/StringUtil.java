package com.tydic.anychatmeeting.util;

import java.io.UnsupportedEncodingException;

/**
 * 字符串工具类
 * Created by like on 2018/1/4.
 */

public class StringUtil {

    /**
     * 字节转化为字符串
     * @param lpBuf
     * @return
     */
    public static String byteToString(byte[] lpBuf) {
        String msg = null;
        try {
            msg = new String(lpBuf, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return msg;
    }

}

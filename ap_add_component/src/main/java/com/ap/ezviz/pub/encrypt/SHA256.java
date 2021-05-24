package com.ap.ezviz.pub.encrypt;

import android.text.TextUtils;
import com.ap.ezviz.pub.utils.LogUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    private final static String TAG = "SHA256";
    private final static String HEX = "0123456789ABCDEF";

    /**
     * 摘要
     *
     * @param text 需加密内容
     */
    public static String digest(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        return toHex(digest(text.getBytes()));
    }

    /**
     * 摘要
     *
     * @param bytes 需加密内容
     */
    public static byte[] digest(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            return sha256.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.w(TAG, e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * byte转hex
     */
    public static String toHex(byte[] buf) {
        if (buf == null) return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}

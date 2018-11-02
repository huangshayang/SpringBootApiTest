package com.apitest.security;

import com.apitest.log.ExceptionLog;
import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHACrypt {

    @Getter
    private String cryptStr;

    public SHACrypt(final String strText, final String strType) {
        // 返回值
        String strResult;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest
                        .getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte[] byteBuffer = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuilder strHexString = new StringBuilder();
                // 遍歷 byte buffer
                for (byte b : byteBuffer) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
                this.cryptStr = strResult;
            } catch (Exception e) {
                new ExceptionLog(e, null);
            }
        }
    }

    public SHACrypt() {
    }

    public boolean matches(String a, String b) {
        char[] caa = a.toCharArray();
        char[] cab = b.toCharArray();
        if (caa.length != cab.length) {
            return false;
        } else {
            byte ret = 0;

            for(int i = 0; i < caa.length; ++i) {
                ret = (byte)(ret | caa[i] ^ cab[i]);
            }

            return ret == 0;
        }
    }
}

package com.apitest.util;

import java.util.HashMap;
import java.util.Map;

public class RevertUtil {

    public static String cookieToMap(String reqCookie){
        Map<String, String> map = new HashMap<>(8);
        String[] str = reqCookie.split(";");
        for (String s : str) {
            String[] str2 = s.split("=");
            map.put(str2[0].trim(), str2[1]);
        }
        return map.get("user_session");
    }
}

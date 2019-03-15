package com.apitest.util;

import com.apitest.configconsts.ConfigConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class RevertUtil {

    public static String cookieToMap(String reqCookie) {
        Map<String, String> map = new HashMap<>(8);
        String[] str = reqCookie.split(";");
        for (String s : str) {
            String[] str2 = s.split("=");
            map.put(str2[0].trim(), str2[1]);
        }
        return map.get(ConfigConsts.USERSESSION_KEY);
    }

    public static void resToJson(HttpServletResponse response, ServerResponse serverResponse) {
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON_UTF8));
        ObjectMapper jsonObject = new ObjectMapper();
        try {
            String json = jsonObject.writeValueAsString(serverResponse);
            OutputStream os = response.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            new ExceptionUtil(e);
        }
    }
}

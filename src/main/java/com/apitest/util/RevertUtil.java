package com.apitest.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Log4j2
public class RevertUtil {

    public static String cookieToMap(String reqCookie) {
        String[] str = reqCookie.split("; ");
        String str2 = null;
        for (String s : str) {
            if (s.contains("user_session")) {
                str2 = s.substring(s.indexOf("=")+1);
            }
        }
        return str2;
    }

    public static void resToJson(HttpServletResponse response, ServerResponse serverResponse) {
        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON));
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

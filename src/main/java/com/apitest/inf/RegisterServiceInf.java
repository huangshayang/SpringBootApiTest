package com.apitest.inf;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface RegisterServiceInf {
    Object registerService(HttpSession httpSession, Map<String, String> models);
}

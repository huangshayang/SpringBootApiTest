package com.apitest.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Random;

@Service
public class CaptchaService {

    public void captchaService(HttpServletResponse response, HttpSession httpSession) {
        try{
            BufferedImage image = new BufferedImage(61, 20, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            Random r = new Random();
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.fillRect(0, 0, 61, 20);
            //获取生成的验证码
            String code = getNumber();
            //绑定验证码
            httpSession.setAttribute("captcha", code);
            httpSession.setMaxInactiveInterval(60);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.drawString(code, 5, 18);
            //设置消息头
            response.setContentType("image/jpeg");
            response.addHeader("cache-control", "no-cache, no-store");
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "jpeg", os);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getNumber(){
        String str = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        int size = 4;
        for (int i = 0; i < size; i++) {
            int index = (int)(Math.random()*str.length());
            code.append(str.charAt(index));
        }
        return code.toString();
    }
}

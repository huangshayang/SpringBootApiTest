package com.apitest.inf;

/**
 * @author huangshayang
 */
public interface MailSendCompoentInf {

    /**
     * 简单文本邮件
     * @param mail
     * @param subject
     * @param content
     */
    void sendSimpleTextMail(String mail, String subject, Object content);
}

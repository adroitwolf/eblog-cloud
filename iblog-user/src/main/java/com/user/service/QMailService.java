package com.user.service;

/**
 * <pre>QMailService</pre>
 * QQ邮箱服务
 * @author <p>ADROITWOLF</p> 2021-05-08
 */
public interface QMailService {
    /**
     * 发送文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);


    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendHtmlMail(String to, String subject, String content);
}

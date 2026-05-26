package com.bugucloud.service.mail;

import jakarta.mail.MessagingException;

/**
 * 功能描述: 邮件服务
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/25 - 23:06
 */

public interface EmailService {

    void sendVerificationCode(String toEmail, String code) throws MessagingException;
}

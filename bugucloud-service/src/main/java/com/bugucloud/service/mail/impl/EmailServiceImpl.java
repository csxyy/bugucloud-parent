package com.bugucloud.service.mail.impl;

import com.bugucloud.service.mail.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 功能描述:
 *
 * @author achen
 * @version 1.0
 * @date 2026/5/25 - 23:08
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    /**
     * 发送验证码邮件（纯邮件发送逻辑）
     *
     * @param toEmail 收件人邮箱
     * @param code    验证码
     * @throws MessagingException 邮件发送失败时抛出，由调用方决定如何处理
     */
    @Override
    public void sendVerificationCode(String toEmail, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("布谷云验证码服务 <3025267039@qq.com>");
        helper.setTo(toEmail);
        helper.setSubject("【布谷云】您的验证码：" + code);

        // 构建邮件内容，可以是简单的文本，也可以是HTML
        // 使用 String.format 让内容更清晰
        String emailContent = String.format(
                "<div style='padding: 20px; font-family: Arial, sans-serif;'>" +
                        "  <h2 style='color: #333;'>验证码</h2>" +
                        "  <p>您的验证码是：<strong style='color: #1a73e8; font-size: 24px;'>%s</strong></p>" +
                        "  <p style='color: #666;'>该验证码5分钟内有效，请勿泄露给他人。</p>" +
                        "  <br/>" +
                        "  <p style='color: #999;'>—— 布谷云团队</p>" +
                        "</div>", code);

        helper.setText(emailContent, true); // true 表示发送HTML邮件
        mailSender.send(message);
    }
}

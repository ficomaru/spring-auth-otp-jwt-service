package com.ninehub.authentication.service;

import com.ninehub.authentication.entity.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender javaMailSender;

    // Create a method to notify user when validate
    public void sendNotification(Validation validation){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@dishup.com");
        message.setTo(validation.getUser().getEmail());
        message.setSubject("Your activation code");

        String text = String.format("""
    <html>
      <body style="margin:0; padding:0; font-family: Arial, sans-serif; background-color:#f4f4f4;">
        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f4f4; padding: 20px;">
          <tr>
            <td align="center">
              <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; padding: 15px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                <tr>
                  <td style="font-size: 18px; color: #333;">
                    <p>Hi <strong>%s</strong>,</p>
                    <p>Thank you for registering with us.</p>
                    <p>Your activation code is:</p>
                    <p style="font-size: 28px; color: #2E86C1; font-weight: bold; margin: 20px 0;">%s</p>
                    <p>Please enter this code to activate your account.</p>
                    <p style="margin-top: 15px;">See you soon!</p>
                    <p style="color: #888888; font-size: 14px;">— The DishUp Team</p>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </body>
    </html>
    """, validation.getUser().getFirstName(), validation.getCode());


        message.setText(text);

        javaMailSender.send(message);
    }
}

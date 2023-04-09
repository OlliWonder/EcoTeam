package com.sber.java13.ecoteam.config;

import com.sber.java13.ecoteam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MailNotifier {
    private UserService userService;
    private JavaMailSender javaMailSender;
    
    @Autowired
    public void getUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Autowired
    public void getEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    
    @Scheduled(cron = "0 0/15 * 1/1 * *")//каждые 15 минут
    //    @Scheduled(cron = "0 0/1 * 1/1 * *") //Каждую минуту
    public void sendMailsToAgents() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        List<String> emails = userService.getAgentsEmailsWithNotCompletedAndNotInWorkOrders();
        if (emails.size() > 0) {
            simpleMailMessage.setTo(emails.toArray(new String[0]));
            simpleMailMessage.setSubject("Напоминание о непринятых в работу заказах");
            simpleMailMessage.setText("Добрый день. Вы получили это письмо, так как у вас есть не принятый в работу заказ. " +
                    "Пожалуйста, проверьте личный кабинет на наличие новых заказов." +
                    "С уважением, EcoTeam.");
            javaMailSender.send(simpleMailMessage);
        }
        log.info("Планировщик работает!");
    }
}

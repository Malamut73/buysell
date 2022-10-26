package com.shop.buysell.services;

public interface MailSenderService {
    void send(String emailTo, String subject, String message);
}

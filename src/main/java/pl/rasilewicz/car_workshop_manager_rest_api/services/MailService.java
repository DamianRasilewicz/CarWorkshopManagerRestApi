package pl.rasilewicz.car_workshop_manager_rest_api.services;

public interface MailService {

        void sendEmail(String to, String subject, String content);
    }

package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey;

    // Brevo वर तुम्ही जो ईमेल व्हेरिफाय केला आहे, तो इथे टाका
    @Value("${BREVO_SENDER_EMAIL}")
    private String senderEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void sendMail(String to, String subject, String text) {
        String url = "https://api.brevo.com/v3/smtp/email";

        // १. Headers सेट करा
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        // २. Sender माहिती (हा ईमेल Brevo वर व्हेरिफाय केलेला असावा)
        Map<String, String> sender = new HashMap<>();
        sender.put("name", "Water Quality Monitor");
        sender.put("email", senderEmail);

        // ३. Receiver माहिती
        Map<String, String> recipient = new HashMap<>();
        recipient.put("email", to);
        List<Map<String, String>> toList = new ArrayList<>();
        toList.add(recipient);

        // ४. ईमेलचा मुख्य डेटा (Body)
        Map<String, Object> body = new HashMap<>();
        body.put("sender", sender);
        body.put("to", toList);
        body.put("subject", subject);
        body.put("htmlContent", "<p>" + text + "</p>"); // मजकूर HTML मध्ये पाठवला जातो

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Email sent successfully via Brevo API to: " + to);
            }
        } catch (Exception e) {
            System.err.println("Failed to send email via Brevo: " + e.getMessage());
        }
    }
}
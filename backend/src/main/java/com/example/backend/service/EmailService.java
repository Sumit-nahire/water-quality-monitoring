package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${RESEND_API_KEY}")
    private String resendApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void sendMail(String to, String subject, String text) {
        String url = "https://api.resend.com/emails";

        // Resend API साठी आवश्यक हेडर्स
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + resendApiKey);

        // ईमेलचा डेटा (JSON Body)
        Map<String, Object> body = new HashMap<>();
        body.put("from", "onboarding@resend.dev"); // Resend चे डिफॉल्ट ईमेल
        body.put("to", to);
        body.put("subject", subject);
        body.put("html", "<p>" + text + "</p>"); // मजकूर HTML मध्ये

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Email sent successfully via Resend API to: " + to);
            }
        } catch (Exception e) {
            System.err.println("Failed to send email via Resend: " + e.getMessage());
        }
    }
}
package com.email.email_writer_sb.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorService(WebClient.Builder webClientBuilder) {

        this.webClient = webClientBuilder.build();
    }

    public String generateEmailReply(EmailRequest emailRequest){
        String prompt=buildPrompt(emailRequest); //build prompt

        //craft the request
        Map<String, Object> requestBody=Map.of("contents", new Object[]{
            Map.of("parts", new Object[]{
                Map.of("text", prompt)
            })
        });

        //do request

        String response=webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper=new ObjectMapper();
            JsonNode rootNode=mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            return "error processing request :" + e.getMessage();
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt=new StringBuilder();
        prompt.append("You are replying to the following email. ");
        prompt.append("Write a professional email reply based on the tone provided. If tone is not provided write according to the email, don't give tone options");
        prompt.append("Do not include a subject line.\n\n");
        //prompt.append("Original Email:\n").append(emailRequest.getEmailContent()).append("\n");
        if(emailRequest.getTone() !=null && !emailRequest.getTone().isEmpty()){
            prompt.append("use a ").append(emailRequest.getTone()).append("tone");
        }
        prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent());

        return prompt.toString();
    }

}

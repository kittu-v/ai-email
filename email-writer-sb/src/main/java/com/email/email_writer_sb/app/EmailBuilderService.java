package com.email.email_writer_sb.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailBuilderService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailBuilderService(WebClient.Builder webClientBuilder) {

        this.webClient = webClientBuilder.build();
    }

    public String buildEmail(EmailBuild emailBuild){
        String prompt=buildPrompt(emailBuild); //build prompt

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

    private String buildPrompt(EmailBuild emailBuild) {
        StringBuilder prompt=new StringBuilder("You are an expert assistant that writes polished and professional emails.\\n\\n");

        if (emailBuild.getRecipientName() != null && !emailBuild.getRecipientName().isBlank()) {
            prompt.append("The recipient's name is ").append(emailBuild.getRecipientName()).append(".\n");
        }

        if(emailBuild.getTone() !=null && !emailBuild.getTone().isEmpty()){
            prompt.append("use a ").append(emailBuild.getTone()).append("tone");
        }

        if (emailBuild.getLength() != null && !emailBuild.getLength().isBlank()) {
            prompt.append("The email should be ").append(emailBuild.getLength().toLowerCase()).append(" in length.\n");
        }

        prompt.append("\nContext:\n").append(emailBuild.getEmailBuildRequest()).append("\n");

        prompt.append("\nGenerate the complete email");

        if (emailBuild.getSenderName() != null && !emailBuild.getSenderName().isBlank()) {
            prompt.append(", and sign it as ").append(emailBuild.getSenderName()).append("\n");
        }

        prompt.append("don't give any other things only generate email");

        return prompt.toString();
    }

}

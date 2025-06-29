package com.email.email_writer_sb.app;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;
    private final EmailBuilderService emailBuilderService;

    public EmailGeneratorController(EmailGeneratorService emailGeneratorService, EmailBuilderService emailBuilderService) {
        this.emailGeneratorService = emailGeneratorService;
        this.emailBuilderService = emailBuilderService;
    }


    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail (@RequestBody EmailRequest emailRequest){
        String response=emailGeneratorService.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/build")
    public ResponseEntity<String> generateEmailBuild (@RequestBody EmailBuild emailBuild){
        String response=emailBuilderService.buildEmail(emailBuild);
        return ResponseEntity.ok(response);
    }
}

package com.email.email_writer_sb.app;

import lombok.Data;

@Data
public class EmailBuild {

    private String emailBuildRequest;
    private String tone;
    private String length;
    private String recipientName;
    private String senderName;

    public String getEmailBuildRequest() {
        return emailBuildRequest;
    }

    public String getTone() {
        return tone;
    }

    public String getLength() {
        return length;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getSenderName() {
        return senderName;
    }
}

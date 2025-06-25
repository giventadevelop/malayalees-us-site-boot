package com.nextjstemplate.service;

import com.nextjstemplate.config.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.SesException;

@Service
public class EmailSenderService {
  private final SesClient sesClient;
  private final String fromAddress;

  @Autowired
  public EmailSenderService(AwsProperties awsProperties, @Value("${jhipster.mail.from}") String fromAddress) {
    this.sesClient = SesClient.builder()
        .region(Region.of(awsProperties.getS3().getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(
                awsProperties.getS3().getAccessKey(),
                awsProperties.getS3().getSecretKey())))
        .build();
    this.fromAddress = fromAddress;
  }

  public void sendEmail(String to, String subject, String body, boolean isHtml) {
    try {
      Body emailBody;
      if (isHtml) {
        emailBody = Body.builder().html(Content.builder().data(body).build()).build();
      } else {
        emailBody = Body.builder().text(Content.builder().data(body).build()).build();
      }
      SendEmailRequest emailRequest = SendEmailRequest.builder()
          .destination(Destination.builder().toAddresses(to).build())
          .message(Message.builder()
              .subject(Content.builder().data(subject).build())
              .body(emailBody)
              .build())
          .source(fromAddress)
          .build();
      SendEmailResponse response = sesClient.sendEmail(emailRequest);
      // Optionally log response.messageId()
    } catch (SesException e) {
      // Log error
      throw new RuntimeException("Failed to send email via SES", e);
    }
  }

  // For backward compatibility
  public void sendEmail(String to, String subject, String body) {
    sendEmail(to, subject, body, false);
  }
}
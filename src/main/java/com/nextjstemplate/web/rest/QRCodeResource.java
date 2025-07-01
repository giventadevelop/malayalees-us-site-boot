package com.nextjstemplate.web.rest;

import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class QRCodeResource {
  private final EventTicketTransactionRepository transactionRepository;
  private final S3Service s3Service;

  @Autowired
  public QRCodeResource(EventTicketTransactionRepository transactionRepository, S3Service s3Service) {
    this.transactionRepository = transactionRepository;
    this.s3Service = s3Service;
  }

  @GetMapping("/events/{eventId}/transactions/{transactionId}/qrcode")
  public ResponseEntity<?> getQRCodeImage(
      @PathVariable Long eventId,
      @PathVariable Long transactionId) {
    Optional<EventTicketTransaction> transactionOpt = transactionRepository.findById(transactionId);
    if (transactionOpt.isEmpty() || !eventId.equals(transactionOpt.get().getEventId())) {
      return ResponseEntity.notFound().build();
    }
    String qrCodeUrl = transactionOpt.get().getQrCodeImageUrl();
    if (qrCodeUrl == null) {
      return ResponseEntity.notFound().build();
    }
    try {
      /*URL url = new URL(qrCodeUrl);
      URLConnection conn = url.openConnection();
      String contentType = conn.getContentType();
      InputStream inputStream = conn.getInputStream();
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType != null ? contentType : "image/png"))
          .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
          .body(new InputStreamResource(inputStream));*/
        URL url = new URL(qrCodeUrl);
        URLConnection conn = url.openConnection();
        String contentType = conn.getContentType();
        InputStream inputStream = conn.getInputStream();
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "image/png"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
            .body(qrCodeUrl);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Could not retrieve QR code image");
    }
  }
}

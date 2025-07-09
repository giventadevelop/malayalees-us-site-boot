package com.nextjstemplate.web.rest;

import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.dto.*;
import com.nextjstemplate.service.mapper.EventTicketTransactionMapper;
import com.nextjstemplate.service.mapper.EventTicketTransactionItemMapper;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.service.mapper.EventDetailsMapper;
import com.nextjstemplate.repository.EventTicketTypeRepository;
import com.nextjstemplate.service.mapper.EventTicketTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import com.nextjstemplate.service.EmailSenderService;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class QRCodeResource {
  private final EventTicketTransactionRepository transactionRepository;
  private final S3Service s3Service;
  private final EventTicketTransactionMapper eventTicketTransactionMapper;
  private final EventTicketTransactionItemMapper eventTicketTransactionItemMapper;
  private final EventTicketTransactionItemRepository eventTicketTransactionItemRepository;
  private final EventDetailsRepository eventDetailsRepository;
  private final EventDetailsMapper eventDetailsMapper;
  private final EventTicketTypeRepository eventTicketTypeRepository;
  private final EventTicketTypeMapper eventTicketTypeMapper;
  private final EmailSenderService emailSenderService;

  @Autowired
  public QRCodeResource(
      EventTicketTransactionRepository transactionRepository,
      S3Service s3Service,
      EventTicketTransactionMapper eventTicketTransactionMapper,
      EventTicketTransactionItemMapper eventTicketTransactionItemMapper,
      EventTicketTransactionItemRepository eventTicketTransactionItemRepository,
      EventDetailsRepository eventDetailsRepository,
      EventDetailsMapper eventDetailsMapper,
      EventTicketTypeRepository eventTicketTypeRepository,
      EventTicketTypeMapper eventTicketTypeMapper,
      EmailSenderService emailSenderService) {
    this.transactionRepository = transactionRepository;
    this.s3Service = s3Service;
    this.eventTicketTransactionMapper = eventTicketTransactionMapper;
    this.eventTicketTransactionItemMapper = eventTicketTransactionItemMapper;
    this.eventTicketTransactionItemRepository = eventTicketTransactionItemRepository;
    this.eventDetailsRepository = eventDetailsRepository;
    this.eventDetailsMapper = eventDetailsMapper;
    this.eventTicketTypeRepository = eventTicketTypeRepository;
    this.eventTicketTypeMapper = eventTicketTypeMapper;
    this.emailSenderService = emailSenderService;
  }

  private QrCodeUsageDTO buildQrCodeUsageDTO(Long eventId, Long transactionId) {
    Optional<EventTicketTransaction> transactionOpt = transactionRepository.findById(transactionId);
    if (transactionOpt.isEmpty() || !eventId.equals(transactionOpt.get().getEventId())) {
      return null;
    }
    EventTicketTransactionDTO transactionDTO = eventTicketTransactionMapper.toDto(transactionOpt.get());
    List<EventTicketTransactionItemDTO> itemDTOs = eventTicketTransactionItemRepository
        .findByTransactionId(transactionId)
        .stream()
        .map(eventTicketTransactionItemMapper::toDto)
        .collect(Collectors.toList());
    Optional<EventDetailsDTO> eventDetailsDTO = eventDetailsRepository.findOneWithEagerRelationships(eventId)
        .map(eventDetailsMapper::toDto);
    if (eventDetailsDTO.isEmpty()) {
      return null;
    }
    List<EventTicketTypeDTO> eventTicketTypeDTOs = eventTicketTypeRepository.findByEvent_Id(eventId)
        .stream()
        .map(eventTicketTypeMapper::toDto)
        .collect(Collectors.toList());
    return new QrCodeUsageDTO(transactionDTO, itemDTOs, eventDetailsDTO.get(), eventTicketTypeDTOs);
  }

  @GetMapping("/qrcode-scan/tickets/events/{eventId}/transactions/{transactionId}")
  public ResponseEntity<QrCodeUsageDTO> getQRCodeScanDetails(
      @PathVariable Long eventId,
      @PathVariable Long transactionId) {
    QrCodeUsageDTO result = buildQrCodeUsageDTO(eventId, transactionId);
    if (result == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(result);
  }

  @GetMapping("/events/{eventId}/transactions/{transactionId}/qrcode")
  public ResponseEntity<?> getQRCodeImage(
      @PathVariable Long eventId,
      @PathVariable Long transactionId) {
    QrCodeUsageDTO dto = buildQrCodeUsageDTO(eventId, transactionId);
    if (dto == null) {
      return ResponseEntity.notFound().build();
    }
    // You can now extract details from dto as needed
    sendTicketEmail(eventId, transactionId, dto.getTransaction().getEmail());
    String qrCodeImageUrl = dto.getTransaction().getQrCodeImageUrl();
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("image/png"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
        .body(qrCodeImageUrl);
  }

  @PostMapping("/events/{eventId}/transactions/{transactionId}/send-ticket-email")
  @Async
  public void sendTicketEmail(
      @PathVariable Long eventId,
      @PathVariable Long transactionId,
      @RequestParam(value = "to", required = false) String to) {
    QrCodeUsageDTO dto = buildQrCodeUsageDTO(eventId, transactionId);
    if (dto == null) {
      return;
    }
    String recipient = (to != null && !to.isBlank()) ? to : "test@example.com";
    String tenantId = dto.getTransaction().getTenantId();
    String headerImageUrl = String.format(
        "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/%s/event-id/%d/tickets/email-templates/email_header_image.jpeg",
        tenantId, eventId);
    String qrCodeImageUrl = dto.getTransaction().getQrCodeImageUrl();
    String eventName = dto.getEventDetails().getTitle();
    // Format event date and time
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH);
    DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", Locale.ENGLISH);
    DateTimeFormatter timeRangeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
    DateTimeFormatter timeWithZoneFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
    String eventDate = "";
    String eventStart = "";
    String eventEnd = "";
    String eventTimeZoneId = dto.getEventDetails().getTimezone();
    java.time.ZoneId eventZoneId = java.time.ZoneId.systemDefault();
    String eventTimeZoneAbbr = "";
    if (eventTimeZoneId != null && !eventTimeZoneId.isBlank()) {
      try {
        eventZoneId = java.time.ZoneId.of(eventTimeZoneId);
        java.time.LocalDate endDate = dto.getEventDetails().getEndDate();
        java.time.ZonedDateTime endZdt = endDate.atStartOfDay(eventZoneId);
        eventTimeZoneAbbr = endZdt.getZone().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
      } catch (Exception e) {
        eventZoneId = java.time.ZoneId.systemDefault();
        eventTimeZoneAbbr = eventZoneId.getId();
      }
    }
    if (dto.getEventDetails().getStartDate() != null && dto.getEventDetails().getEndDate() != null) {
      java.time.LocalDate startDate = dto.getEventDetails().getStartDate();
      java.time.LocalDate endDate = dto.getEventDetails().getEndDate();
      java.time.ZonedDateTime start = startDate.atStartOfDay(eventZoneId);
      java.time.ZonedDateTime end = endDate.atStartOfDay(eventZoneId);
      eventDate = start.format(dateOnlyFormatter);
      eventStart = start.format(timeRangeFormatter);
      eventEnd = end.format(timeRangeFormatter) + " (" + eventTimeZoneAbbr + ")";
    }
    String dateOfPurchase = "";
    if (dto.getTransaction().getCreatedAt() != null) {
      dateOfPurchase = dto.getTransaction().getCreatedAt().format(dateTimeFormatter);
    }
    String checkInTime = "";
    if (dto.getTransaction().getCheckInTime() != null) {
      checkInTime = dto.getTransaction().getCheckInTime().format(dateTimeFormatter);
    }
    // Transaction details
    String name = dto.getTransaction().getFirstName();
    String email = dto.getTransaction().getEmail();
    String amountPaid = dto.getTransaction().getTotalAmount() != null
        ? String.format("$%.2f", dto.getTransaction().getFinalAmount())
        : "";

    EventTicketTransactionDTO txn = dto.getTransaction();
    String ticketNumber;
    if (txn.getTransactionReference() != null && !txn.getTransactionReference().isEmpty()) {
      ticketNumber = txn.getTransactionReference();
    } else if (txn.getId() != null) {
      ticketNumber = "TKTN" + txn.getId().toString();
    } else {
      ticketNumber = null; // or handle as needed
    }

    String checkInStatus = dto.getTransaction().getCheckInStatus();

    // Build ticket breakdown rows with centered text
    StringBuilder ticketBreakdownRows = new StringBuilder();
    for (EventTicketTransactionItemDTO item : dto.getItems()) {
      String typeName = "";
      for (EventTicketTypeDTO type : dto.getEventTicketTypes()) {
        if (type.getId().equals(item.getTicketTypeId())) {
          typeName = type.getName();
          break;
        }
      }
      ticketBreakdownRows.append(String.format(
          "<tr>"
              + "<td style='padding:8px; text-align:center;'>%s</td>"
              + "<td style='padding:8px; text-align:center;'>%d</td>"
              + "<td style='padding:8px; text-align:center;'>$%.2f</td>"
              + "<td style='padding:8px; text-align:center;'>$%.2f</td>"
              + "</tr>",
          typeName, item.getQuantity(), item.getPricePerUnit(), item.getTotalAmount()));
    }

    // Emojis for section headings
    String moneyEmoji = "💵";
    String ticketEmoji = "🎟️";
    String calendarEmoji = "📅";
    String qrEmoji = "🎫";
    String discountEmoji = "🏷️";
    String discountAmount = "";
    if (dto.getTransaction().getDiscountAmount() != null
        && dto.getTransaction().getDiscountAmount().doubleValue() > 0) {
      discountAmount = String.format("%s $%.2f", discountEmoji, dto.getTransaction().getDiscountAmount());
    }

    // Build HTML email body
    String discountRow = "";
    if (!discountAmount.isEmpty()) {
      discountRow = String.format("<tr><td style='font-weight:bold;'>Discount</td><td colspan='3'>%s</td></tr>",
          discountAmount);
    }
    String template = String.format(
        """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset=\"UTF-8\">
              <title>Your Event Ticket</title>
            </head>
            <body style=\"font-family: Arial, sans-serif; background: #f9f9f9; padding: 20px;\">
              <div style=\"max-width: 650px; margin: auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 12px #eee; overflow: hidden;\">
                <img src=\"%s\" alt=\"Event Header\" style=\"width: 100%%; display: block; border-bottom: 1px solid #eee;\">
                <div style=\"padding: 32px;\">
                  <h2 style=\"margin-top: 0; text-align:center; color:#6b207c;\">%s Your Ticket QR Code</h2>
                  <div style=\"text-align:center; margin-bottom: 32px;\">
                    <img src=\"%s\" alt=\"QR Code\" style=\"width:180px;height:180px; border: 1px solid #eee; border-radius: 8px; background: #fafafa; padding: 12px;\">
                  </div>
                  <div style=\"margin-bottom: 32px;\">
                    <h3 style=\"margin-bottom: 12px; color:#6b207c;\">%s Ticket # <span style='color:#2d3748;font-weight:bold;'>%s</span></h3>
                    <h3 style=\"margin-bottom: 12px; color:#6b207c;\">%s Ticket Breakdown</h3>
                    <table style=\"width:100%%; border-collapse:collapse;\">
                      <tr style=\"background:#f5f5f5;\">
                        <th style=\"padding:8px; text-align:center;\">Ticket Type</th>
                        <th style=\"padding:8px; text-align:center;\">Quantity</th>
                        <th style=\"padding:8px; text-align:center;\">Price Per Unit</th>
                        <th style=\"padding:8px; text-align:center;\">Total</th>
                      </tr>
                      %s
                    </table>
                  </div>
                  <div style=\"margin-bottom: 32px;\">
                    <h3 style=\"margin-bottom: 12px; color:#6b207c;\">%s Event Details</h3>
                    <p>
                      <strong>%s</strong><br>
                      <span>%s | %s - %s</span><br>
                      <span>%s</span>
                    </p>
                  </div>
                </div>
              </div>
            </body>
            </html>
            """,
        headerImageUrl,
        qrEmoji,
        qrCodeImageUrl,
        ticketEmoji,
        ticketNumber,
        ticketEmoji,
        ticketBreakdownRows.toString(),
        calendarEmoji,
        eventName,
        eventDate,
        eventStart,
        eventEnd,
        dto.getEventDetails().getLocation());

    // Prepare S3 URLs for footer and logo, replacing tenant_demo_001 with actual
    // tenantId
    String tenantIdPath = tenantId != null ? tenantId : "tenant_demo_001";
    String footerHtmlS3Url = String.format(
        "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/%s/email-templates/email_footer.html",
        tenantIdPath);
    String logoS3Url = String.format(
        "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/%s/email-templates/email_footer_logo.png",
        tenantIdPath);

    // Download the footer HTML from S3
    String footerHtml = "";
    try {
      footerHtml = s3Service.downloadHtmlFromUrl(footerHtmlS3Url); // Implement this method in S3Service
      // Replace the logo placeholder
      footerHtml = footerHtml.replace("{{LOGO_URL}}", logoS3Url);
    } catch (Exception e) {
      // If download fails, fallback to empty or default footer
      footerHtml = "";
    }

    // ... build the main email HTML as before ...
    String fullEmailHtml = template + footerHtml;
    emailSenderService.sendEmail(recipient, "Your Event Ticket for " + eventName, fullEmailHtml, true);
  }

  @PostMapping("/send-promotion-emails")
  public ResponseEntity<Map<String, Object>> sendPromotionEmail(
      @RequestBody PromotionEmailRequestDTO requestDTO) {
    String tenantId = requestDTO.getTenantId();
    String recipient = requestDTO.getTo();
    String subject = requestDTO.getSubject();
    String promoCode = requestDTO.getPromoCode();
    String bodyHtml = requestDTO.getBodyHtml();
    String headerImagePath = requestDTO.getHeaderImagePath();
    String footerPath = requestDTO.getFooterPath();

    sendPromoEmails(tenantId, promoCode, footerPath, bodyHtml, recipient, subject);

    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    return ResponseEntity.ok(response);
  }

  @Async
  protected void sendPromoEmails(String tenantId, String promoCode, String footerPath, String bodyHtml,
      String recipient, String subject) {
    String headerImagePath = "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/"
        + tenantId + "/promotions/promocode/" + promoCode + "/email-templates/email_header_image.jpeg";

    // Download footer HTML if provided
   /* String footerHtml = "";
    if (footerPath != null && !footerPath.isBlank()) {
      try {
        footerHtml = s3Service.downloadHtmlFromUrl(footerPath);
        String logoUrl = String.format(
            "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/%s/email-templates/email_footer_logo.png",
            tenantId);
        footerHtml = footerHtml.replace("{{LOGO_URL}}", logoUrl);
      } catch (Exception e) {
        footerHtml = "";
      }
    }

    // Add a default footer if footerHtml is still empty
    if (footerHtml == null || footerHtml.isBlank()) {
      footerHtml = "<footer style=\"background:#1a202c; color:#fff; padding:32px 16px; margin-top:32px;\">"
          + "<div style=\"text-align:center; color:#a0aec0; margin-top:32px;\">"
          + "© 2025 United Team India. All rights reserved."
          + "</div></footer>";
    }*/

      // Prepare S3 URLs for footer and logo, replacing tenant_demo_001 with actual
      // tenantId
      String tenantIdPath = tenantId != null ? tenantId : "tenant_demo_001";
      String footerHtmlS3Url = String.format(
          "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/%s/email-templates/email_footer.html",
          tenantIdPath);
      String logoS3Url = String.format(
          "https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/%s/email-templates/email_footer_logo.png",
          tenantIdPath);

      // Download the footer HTML from S3
      String footerHtml = "";
      try {
          footerHtml = s3Service.downloadHtmlFromUrl(footerHtmlS3Url); // Implement this method in S3Service
          // Replace the logo placeholder
          footerHtml = footerHtml.replace("{{LOGO_URL}}", logoS3Url);
      } catch (Exception e) {
          // If download fails, fallback to empty or default footer
          footerHtml = "";
      }

    // Build the full HTML email
    String fullEmailHtml = String.format(
        """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset=\"UTF-8\">
              <title>%s</title>
            </head>
            <body style=\"font-family: Arial, sans-serif; background: #f9f9f9; padding: 20px;\">
              <div style=\"max-width: 650px; margin: auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 12px #eee; overflow: hidden;\">
                <img src=\"%s\" alt=\"Event Header\" style=\"width: 100%%; display: block; border-bottom: 1px solid #eee;\">
                <div style=\"padding: 32px;\">
                  %s
                </div>
                %s
              </div>
            </body>
            </html>
            """,
        subject,
        headerImagePath,
        bodyHtml,
        footerHtml);

      /*String fullEmailHtml = String.format(
          """
              <!DOCTYPE html>
              <html>
              <head>
                <meta charset=\"UTF-8\">
                <title>%s</title>
              </head>
              <body style=\"font-family: Arial, sans-serif; background: #f9f9f9; padding: 20px;\">
                <div style=\"max-width: 650px; margin: auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 12px #eee; overflow: hidden;\">
                  <img src=\"%s\" alt=\"Event Header\" style=\"width: 100%%; display: block; border-bottom: 1px solid #eee;\">
                  <div style=\"padding: 32px;\">
                  </div>
                  %s
                </div>
              </body>
              </html>
              """,
          subject,
          headerImagePath,

          footerHtml);*/

      /*fullEmailHtml = ""
          + "<div style=\"max-width: 650px; margin: auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 12px #eee; overflow: hidden;\">"
          + "  <img src=\"https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/promotions/promotion_code_001/email-templates/email_header_image.jpeg\" alt=\"Event Header\" style=\"width: 100%; display: block; border-bottom: 1px solid #eee;\">"
          + "  <div style=\"padding: 32px;\">"
          + "    <div style=\"font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto; background: #f9f9f9; border-radius: 8px; padding: 24px; text-align: center;\">"
          + "      <h2 style=\"color: #1a237e; margin-bottom: 12px;\">Special Offer Just for You!</h2>"
          + "      <p style=\"font-size: 18px; color: #333; margin-bottom: 8px;\">Use the code below to get an exclusive discount:</p>"
          + "      <div style=\"font-size: 24px; font-weight: bold; color: #1565c0; background: #e3f2fd; border-radius: 6px; display: inline-block; padding: 12px 32px; margin-bottom: 12px;\">SAVE20</div>"
          + "      <p style=\"font-size: 16px; color: #444;\">Enter this code at checkout to enjoy your savings!</p>"
          + "    </div>"
          + "  </div>"
          + "  <footer style=\"background:#1a202c; color:#fff; padding:32px 16px; margin-top:32px;\">"
          + "    <div style=\"max-width:900px; margin:auto; display:flex; flex-wrap:wrap; justify-content:space-between; gap:32px;\">"
          + "      <div style=\"flex:1; min-width:200px; text-align:center;\">"
          + "        <a href=\"https://www.adwiise.com/\">"
          + "          <img src=\"https://eventapp-media-bucket.s3.us-east-2.amazonaws.com/events/tenantId/tenant_demo_001/email-templates/email_footer_logo.png\" alt=\"Malayalees US Logo\" style=\"width:128px; margin-bottom:16px;\" />"
          + "        </a>"
          + "        <div style=\"font-weight:600; font-size:1.125rem;\">Follow us</div>"
          + "      </div>"
          + "      <div style=\"flex:1; min-width:200px; text-align:center;\">"
          + "        <div style=\"font-weight:600; font-size:1.125rem; margin-bottom:8px;\">Main menu</div>"
          + "        <ul style=\"list-style:none; padding:0; margin:0;\">"
          + "          <li><a href=\"#\" style=\"color:#fff; text-decoration:none;\">Home</a></li>"
          + "          <li><a href=\"#\" style=\"color:#fff; text-decoration:none;\">About</a></li>"
          + "          <li><a href=\"#\" style=\"color:#fff; text-decoration:none;\">Events</a></li>"
          + "          <li><a href=\"#\" style=\"color:#fff; text-decoration:none;\">Team</a></li>"
          + "          <li><a href=\"#\" style=\"color:#fff; text-decoration:none;\">Contact</a></li>"
          + "        </ul>"
          + "      </div>"
          + "      <div style=\"flex:1; min-width:200px; text-align:center;\">"
          + "        <div style=\"font-weight:600; font-size:1.125rem; margin-bottom:8px;\">Contacts</div>"
          + "        <div>Unite India</div>"
          + "        <div>New Jersey, USA</div>"
          + "        <a href=\"tel:+16317088442\" style=\"color:#63b3ed; text-decoration:none;\">+1 (631) 708-8442</a>"
          + "      </div>"
          + "    </div>"
          + "    <div style=\"text-align:center; color:#a0aec0; margin-top:32px;\">"
          + "      © 2025 United Team India. All rights reserved."
          + "    </div>"
          + "  </footer>"
          + "</div>";*/

      List<String> emails = List.of(
//          "gain@hotmail.com",
//          "giventa.develop@gmail.com",
          "giventauser@gmail.com",
          "giventauser@gmail.com",
          "giventauser@gmail.com"
          // add more emails here
      );

      for (String email : emails) {
          // Replace this with your actual email sending logic
          System.out.println("Sending email to: " + email);

          // Pause for 2 seconds (2000 milliseconds)
          try {
              Thread.sleep(2000);
              recipient =email;
              emailSenderService.sendEmail(recipient, subject, fullEmailHtml, true);
          } catch (InterruptedException e) {
              // Handle interruption
              Thread.currentThread().interrupt();
              System.out.println("Thread was interrupted, stopping email sending.");
              break;
          }
      }

  }
}

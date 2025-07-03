package com.nextjstemplate.web.rest;

import com.nextjstemplate.domain.EventTicketTransaction;
import com.nextjstemplate.repository.EventTicketTransactionRepository;
import com.nextjstemplate.service.S3Service;
import com.nextjstemplate.service.mapper.EventTicketTransactionMapper;
import com.nextjstemplate.service.mapper.EventTicketTransactionItemMapper;
import com.nextjstemplate.repository.EventTicketTransactionItemRepository;
import com.nextjstemplate.service.dto.EventTicketTransactionDTO;
import com.nextjstemplate.service.dto.EventTicketTransactionItemDTO;
import com.nextjstemplate.service.dto.QrCodeUsageDTO;
import com.nextjstemplate.repository.EventDetailsRepository;
import com.nextjstemplate.service.mapper.EventDetailsMapper;
import com.nextjstemplate.service.dto.EventDetailsDTO;
import com.nextjstemplate.repository.EventTicketTypeRepository;
import com.nextjstemplate.service.mapper.EventTicketTypeMapper;
import com.nextjstemplate.service.dto.EventTicketTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import com.nextjstemplate.service.EmailSenderService;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
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

  // @PostMapping("/events/{eventId}/transactions/{transactionId}/send-ticket-email")
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
    String eventDate = "";
    String eventStart = "";
    String eventEnd = "";
    if (dto.getEventDetails().getStartDate() != null && dto.getEventDetails().getEndDate() != null) {
      java.time.LocalDate startDate = dto.getEventDetails().getStartDate();
      java.time.LocalDate endDate = dto.getEventDetails().getEndDate();
      java.time.ZonedDateTime start = startDate.atStartOfDay(java.time.ZoneId.systemDefault());
      java.time.ZonedDateTime end = endDate.atStartOfDay(java.time.ZoneId.systemDefault());
      eventDate = start.format(dateOnlyFormatter);
      eventStart = dto.getEventDetails().getStartTime();
      eventEnd = dto.getEventDetails().getEndTime();
    }
    String dateOfPurchase = "";
    if (dto.getTransaction().getCreatedAt() != null) {
      dateOfPurchase = dto.getTransaction().getCreatedAt().format(dateTimeFormatter);
    }
    String checkInTime = "";
    if (dto.getTransaction().getCheckInTime() != null) {
      checkInTime = dto.getTransaction().getCheckInTime().format(dateTimeFormatter);
    }
    // SVG icons
    String calendarIcon = "<svg width='20' height='20' fill='none' stroke='currentColor' stroke-width='2' viewBox='0 0 24 24' style='vertical-align:middle;'><rect x='3' y='4' width='18' height='18' rx='4' stroke='#2d3748' fill='#f5f5f5'/><path d='M16 2v4M8 2v4M3 10h18' stroke='#2d3748'/></svg>";
    String emailIcon = "<svg width='20' height='20' fill='none' stroke='currentColor' stroke-width='2' viewBox='0 0 24 24' style='vertical-align:middle;'><rect x='3' y='5' width='18' height='14' rx='2' stroke='#2d3748' fill='#f5f5f5'/><path d='M3 7l9 6 9-6' stroke='#2d3748'/></svg>";
    String moneyIcon = "<svg width='20' height='20' fill='none' stroke='currentColor' stroke-width='2' viewBox='0 0 24 24' style='vertical-align:middle;'><rect x='2' y='6' width='20' height='12' rx='4' stroke='#2d3748' fill='#f5f5f5'/><circle cx='12' cy='12' r='3' stroke='#2d3748'/></svg>";
    // Transaction details
    String name = dto.getTransaction().getFirstName();
    String email = dto.getTransaction().getEmail();
    String amountPaid = dto.getTransaction().getTotalAmount() != null
        ? String.format("$%.2f", dto.getTransaction().getTotalAmount())
        : "";
    String checkInStatus = dto.getTransaction().getCheckInStatus();
    // Build ticket breakdown rows
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
          "<tr><td style='padding:8px;'>%s</td><td style='padding:8px;'>%d</td><td style='padding:8px;'>$%.2f</td><td style='padding:8px;'>$%.2f</td></tr>",
          typeName, item.getQuantity(), item.getPricePerUnit(), item.getTotalAmount()));
    }
    // Build HTML email body
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
                  <h2 style=\"margin-top: 0; text-align:center;\">Your Ticket QR Code</h2>
                  <div style=\"text-align:center; margin-bottom: 32px;\">
                    <img src=\"%s\" alt=\"QR Code\" style=\"width:180px;height:180px; border: 1px solid #eee; border-radius: 8px; background: #fafafa; padding: 12px;\">
                  </div>
                  <div style=\"margin-bottom: 32px;\">
                    <h3 style=\"margin-bottom: 12px; display: flex; align-items: center;\">
                      <span style=\"margin-right: 8px;\">%s</span> Transaction Summary
                    </h3>
                    <table style=\"width:100%%; border-collapse:collapse; margin-bottom: 12px;\">
                      <tr>
                        <td><strong>Name</strong></td>
                        <td>%s</td>
                        <td><strong>Email</strong></td>
                        <td>%s</td>
                      </tr>
                      <tr>
                        <td><strong>Date of Purchase</strong></td>
                        <td>%s</td>
                        <td><strong>Amount Paid</strong></td>
                        <td>%s</td>
                      </tr>
                    </table>
                  </div>
                  <div style=\"margin-bottom: 32px;\">
                    <h3 style=\"margin-bottom: 12px; display: flex; align-items: center;\">
                      <span style=\"margin-right: 8px;\">%s</span> Ticket Breakdown
                    </h3>
                    <table style=\"width:100%%; border-collapse:collapse;\">
                      <tr style=\"background:#f5f5f5;\">
                        <th style=\"padding:8px;\">Ticket Type</th>
                        <th style=\"padding:8px;\">Quantity</th>
                        <th style=\"padding:8px;\">Price Per Unit</th>
                        <th style=\"padding:8px;\">Total</th>
                      </tr>
                      %s
                    </table>
                  </div>
                  <div style=\"margin-bottom: 32px;\">
                    <h3 style=\"margin-bottom: 12px; display: flex; align-items: center;\">
                      <span style=\"margin-right: 8px;\">%s</span> Event Details
                    </h3>
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
        qrCodeImageUrl,
        moneyIcon,
        name,
        email,
        dateOfPurchase,
        amountPaid,
        calendarIcon,
        ticketBreakdownRows.toString(),
        calendarIcon,
        eventName,
        eventDate,
        eventStart,
        eventEnd,
        dto.getEventDetails().getLocation());
    emailSenderService.sendEmail(recipient, "Your Event Ticket for " + eventName, template, true);
  }
}

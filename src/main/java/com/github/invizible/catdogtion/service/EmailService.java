package com.github.invizible.catdogtion.service;

import com.github.invizible.catdogtion.domain.Auction;
import com.github.invizible.catdogtion.domain.User;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;

@Service
@CommonsLog
public class EmailService {

  private static final String BASE_URL = "baseUrl";
  private static final String AUCTION_ID = "auctionId";
  private static final String EMAIL = "email";

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Value("${mail.from}")
  private String mailFrom;

  @Value("${mail.baseUrl}")
  private String baseUrl;

  @Async
  public void sendEmail(String subject, String content, String... recipients) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
      message.setFrom(mailFrom);
      message.setTo(recipients);
      message.setSubject(subject);
      message.setText(content, true);

      javaMailSender.send(mimeMessage);
    } catch (Exception e) {
      log.error("Could not send e-mail", e);
    }
  }

  @Async
  public void sendAuctionStartingNotificationToAllParticipants(Auction auction) {
    log.info(String.format("Sending invitations for auction: %d", auction.getId()));

    String[] emailAddresses = auction.getParticipants().stream()
      .map(User::getEmail)
      .toArray(String[]::new);

    Context context = createAuctionNotificationContext(auction);
    String content = templateEngine.process("auctionStartingNotification", context);

    sendEmail("Do not miss your auction!", content, emailAddresses);
  }

  private Context createAuctionNotificationContext(Auction auction) {
    Context context = new Context();
    context.setVariable(BASE_URL, baseUrl);
    context.setVariable(AUCTION_ID, auction.getId());
    return context;
  }

  @Async
  public void sendAuctionWinnerNotificationToParticipant(Auction auction) {
    log.info(String.format("Sending auction (id: %d) winner notification to the participant", auction.getId()));

    Context context = createAuctionNotificationContext(auction);
    context.setVariable(EMAIL, getAuctioneerEmail(auction));
    String content = templateEngine.process("auctionWinnerToParticipantNotification", context);

    sendEmail("There is a winner", content, getWinnerEmail(auction));
  }

  @Async
  public void sendAuctionWinnerNotificationToAuctioneer(Auction auction) {
    log.info(String.format("Sending auction (id: %d) winner notification to the auctioneer", auction.getId()));

    Context context = createAuctionNotificationContext(auction);
    context.setVariable(EMAIL, getWinnerEmail(auction));
    String content = templateEngine.process("auctionWinnerToAuctioneerNotification", context);

    sendEmail("There is a winner", content, getAuctioneerEmail(auction));
  }

  private String getWinnerEmail(Auction auction) {
    return auction.getWinner().getEmail();
  }

  private String getAuctioneerEmail(Auction auction) {
    return auction.getLot().getAuctioneer().getEmail();
  }
}

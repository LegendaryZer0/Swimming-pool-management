package ru.softwave.pool.util.smtp;

public interface EmailUtil {
  void sendEmail(String to, String subject, String from, String text);
}

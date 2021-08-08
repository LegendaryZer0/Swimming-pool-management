package ru.softwave.pool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.softwave.pool.model.entity.User;
import ru.softwave.pool.repo.UserRepository;
import ru.softwave.pool.util.smtp.EmailUtil;
import ru.softwave.pool.util.smtp.MailsGenerator;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
  @Autowired private UserRepository userRepository;

  @Autowired private MailsGenerator mailsGenerator;

  @Value("${server.url}")
  private String serverUrl;

  @Value("${spring.mail.username}")
  private String from;

  @Autowired private EmailUtil emailUtil;

  @Override
  public void sendConfirmEmail(User user) {
    userRepository.getUserByEmail(user.getEmail());
    String confirmMail =
        mailsGenerator.getEmailforConfirm(serverUrl, user.getTechnicalInfo().getUuidToConfirm());
    emailUtil.sendEmail(user.getEmail(), "Confirm Account", from, confirmMail);
  }

  @Override
  public void confirmEmail(UUID uuid) {
    User user =
        userRepository
            .findByTechnicalInfo_UuidToConfirmEquals(uuid)
            .orElseThrow(() -> new EntityNotFoundException("There is no user to confirm email"));
    user.setIsConfirmed(Boolean.TRUE);
    userRepository.save(user);
  }
}

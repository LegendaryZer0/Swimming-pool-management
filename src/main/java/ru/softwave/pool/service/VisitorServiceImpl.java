package ru.softwave.pool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.softwave.pool.model.entity.Visitor;
import ru.softwave.pool.repo.VisitorRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class VisitorServiceImpl implements VisitorService {

  @Autowired private VisitorRepository visitorRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  public void registrVisitor(Visitor visitor) {
    visitor.setHashPassword(passwordEncoder.encode(visitor.getHashPassword()));
    visitorRepository.save(visitor);
  }

  public void saveVisitor(Visitor visitor) {
    visitorRepository.save(visitor);
  }

  public Visitor findVisitorByEmail(String email) {
    return visitorRepository
        .findVisitorByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("Such a visitor does not exist"));
  }
}

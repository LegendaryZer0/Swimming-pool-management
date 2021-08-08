package ru.softwave.pool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.softwave.pool.model.entity.Admin;
import ru.softwave.pool.repo.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {
  @Autowired private AdminRepository adminRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public void registrAdmin(Admin admin) {
    admin.setHashPassword(passwordEncoder.encode(admin.getHashPassword()));
    adminRepository.save(admin);
  }

  public void saveAdmin(Admin admin) {
    adminRepository.save(admin);
  }
}

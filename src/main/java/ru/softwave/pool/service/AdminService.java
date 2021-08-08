package ru.softwave.pool.service;

import ru.softwave.pool.model.entity.Admin;

public interface AdminService {
  void registrAdmin(Admin admin);

  void saveAdmin(Admin admin);
}

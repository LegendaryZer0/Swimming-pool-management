package ru.softwave.pool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softwave.pool.model.dto.AdminSaveDto;
import ru.softwave.pool.service.AdminService;

import javax.validation.Valid;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/admin-management")
public class AdminController {
  @Autowired private AdminService adminService;

  @PostMapping("/admins")
  public String saveAdmin(@RequestBody @Valid AdminSaveDto dto) {
    adminService.registrAdmin(dto.convertToAdmin());
    return "OK";
  }
}

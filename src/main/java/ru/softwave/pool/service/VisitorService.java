package ru.softwave.pool.service;

import ru.softwave.pool.model.entity.Visitor;

public interface VisitorService {
  void registrVisitor(Visitor visitor);

  Visitor findVisitorByEmail(String email);

  void saveVisitor(Visitor visitor);
}

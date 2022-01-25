package ru.softwave.pool.service;

public interface ReportService {
    public byte[] createProjectPDF(Long accountId, String routingKey);
}

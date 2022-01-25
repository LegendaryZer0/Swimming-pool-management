package ru.softwave.pool.service;

import net.sf.jasperreports.engine.JasperPrint;

import java.util.List;
import java.util.Map;

public interface PdfGeneratorService {
    public byte[] createReportFromJasperPrint(JasperPrint jasperPrint, String tempFileName);
    public JasperPrint mergeJasperPrints(JasperPrint jasperPrint1, JasperPrint jasperPrint2);
    public JasperPrint createJasperPrint(Map<String, Object> parameters, String templatePath);
    public JasperPrint createJasperPrint(List<?> data, String templatePath);
    public JasperPrint createJasperPrint(List<?> data, Map<String, Object> parameters, String templatePath);
    public byte[] createReport(List<?> data, Map<String, Object> parameters, String templatePath, String tempFileName);
}

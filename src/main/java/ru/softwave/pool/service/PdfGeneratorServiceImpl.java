package ru.softwave.pool.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import ru.softwave.pool.exception.ErrorType;
import ru.softwave.pool.exception.Exc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService {
    private static final String ERROR_CREATING_PDF_REPORT = "Error creating PDF report";

    /**
     * Создаёт отчёт pdf и конвертирует в массив байт
     *
     * @param data         данные отчёта
     * @param parameters   парамерты создания отчёта
     * @param templatePath путь к месту создания отчёта
     * @param tempFileName название итогового файла отчёта
     * @return массив байт отчёта pdf
     */
    public byte[] createReport(List<?> data, Map<String, Object> parameters, String templatePath, String tempFileName) {
        try {
            JasperPrint jasperPrint = createJasperPrint(data, parameters, templatePath);
            return createReportFromJasperPrint(jasperPrint, tempFileName);
        } catch (Exception e) {
            log.error(ERROR_CREATING_PDF_REPORT, e);
            throw Exc.gen(
                    ErrorType.PDF_CREATION_FAILED, e.getMessage(), e);
        }
    }

    /**
     * Создаёт объект JasperPrint, описывающий документ, готовый к просмотру или экспорту
     *
     * @param data         данные отчёта
     * @param parameters   парамерты создания отчёта
     * @param templatePath путь к месту создания отчёта
     * @return созданный объект JasperPrint
     */
    public JasperPrint createJasperPrint(List<?> data, Map<String, Object> parameters, String templatePath) {
        InputStream reportStream = getClass().getClassLoader().getResourceAsStream(templatePath);
        JasperReport jasperReport;
        try {
            jasperReport = JasperCompileManager.compileReport(reportStream);
            JRDataSource jrBeanCollectionDataSource = (data == null || data.isEmpty()) ? new JREmptyDataSource(1) : new JRBeanCollectionDataSource(data);
            return JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
        } catch (JRException e) {
            log.error(ERROR_CREATING_PDF_REPORT, e);
            throw Exc.gen(
                    ErrorType.PDF_CREATION_FAILED, e.getMessage(), e);
        }

    }

    /**
     * Создаёт объект JasperPrint, описывающий документ, готовый к просмотру или экспорту
     *
     * @param data         данные отчёта
     * @param templatePath путь к месту создания отчёта
     * @return созданный объект JasperPrint
     */
    public JasperPrint createJasperPrint(List<?> data, String templatePath) {
        return createJasperPrint(data, new HashMap<>(), templatePath);
    }

    /**
     * Создаёт объект JasperPrint, описывающий документ, готовый к просмотру или экспорту
     *
     * @param parameters   парамерты создания отчёта
     * @param templatePath путь к месту создания отчёта
     * @return созданный объект JasperPrint
     */
    public JasperPrint createJasperPrint(Map<String, Object> parameters, String templatePath) {
        return createJasperPrint(Collections.emptyList(), parameters, templatePath);
    }

    /**
     * Объединяет два документа отчёта
     *
     * @param jasperPrint1 объект JasperPrint, описывающий первый документ
     * @param jasperPrint2 объект JasperPrint, описывающий второй документ
     * @return объект JasperPrint, описывающий результат объединения документов
     */
    public JasperPrint mergeJasperPrints(JasperPrint jasperPrint1, JasperPrint jasperPrint2) {
        if (jasperPrint2.getPages() != null) {
            jasperPrint2.getPages().forEach(jasperPrint1::addPage);
        }
        return jasperPrint1;
    }

    /**
     * Создаёт отчёт pdf из объекта JasperPrint и конвертирует в массив байт
     *
     * @param jasperPrint  объект JasperPrint, описывающий документ, готовый к просмотру или экспорту
     * @param tempFileName имя файла
     * @return массив байт отчёта pdf
     */
    public byte[] createReportFromJasperPrint(JasperPrint jasperPrint, String tempFileName) {

        try {
            Path reportPath = Files.createTempFile(tempFileName, ".pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath.toString());
            log.info("PDF report successfully created");
            return Files.readAllBytes(reportPath);
        } catch (JRException | IOException e) {
            log.error(ERROR_CREATING_PDF_REPORT, e);
            throw Exc.gen(
                    ErrorType.PDF_CREATION_FAILED, e.getMessage(), e);
        }
    }

    /**
     * Генерируем сразу в массив байтов
     *
     * @param jasperPrint
     * @return
     */
    public byte[] createReportAsBytes(JasperPrint jasperPrint) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteOut);
            return byteOut.toByteArray();
        } catch (JRException e) {
            log.error(ERROR_CREATING_PDF_REPORT, e);
            throw Exc.gen(ErrorType.PDF_CREATION_FAILED, e.getMessage(), e);
        }
    }

}

package ru.softwave.pool.controller.report;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.softwave.pool.constant.QueueConstants;
import ru.softwave.pool.service.ReportService;

import javax.annotation.security.PermitAll;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report/pdf")
public class ReportController {
    public static final String ATTACHMENT = "attachment";
    @Autowired
    private final ReportService reportService;

    /**
     * Создаёт pdf по проекту и предоставляет возможность его скачать
     *
     * @param projectId идентификатор проекта
     * @return поток байт сгенерированного отчёта
     */
    @GetMapping("/resignation")
    @PermitAll
    public ResponseEntity<byte[]> getProjectResignationReportPDF(@RequestParam Long projectId) {
        byte[] result = reportService.createProjectPDF(projectId, QueueConstants.RESIGNATION_QUEUE);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder(ATTACHMENT)
                                .filename("Паспорт проекта.pdf", StandardCharsets.UTF_8).build().toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(result);
    }

    @GetMapping("/promote")
    @PermitAll
    public ResponseEntity<byte[]> getProjectPromotionReportPDF(@RequestParam Long projectId) {
        byte[] result = reportService.createProjectPDF(projectId, QueueConstants.PROMOTION_QUEUE);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder(ATTACHMENT)
                                .filename("Паспорт проекта.pdf", StandardCharsets.UTF_8).build().toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(result);
    }

    @GetMapping("/report")
    @PermitAll
    public ResponseEntity<byte[]> getProjectReportPDF(@RequestParam Long projectId) {
        byte[] result = reportService.createProjectPDF(projectId, QueueConstants.REPORT_QUEUE);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder(ATTACHMENT)
                                .filename("Паспорт проекта.pdf", StandardCharsets.UTF_8).build().toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(result);
    }
}

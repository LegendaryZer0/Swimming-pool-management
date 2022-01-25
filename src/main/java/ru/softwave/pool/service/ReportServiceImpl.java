package ru.softwave.pool.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.softwave.pool.model.dto.ReportDto;
import ru.softwave.pool.model.dto.UserDto;
import ru.softwave.pool.model.entity.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final PdfGeneratorService pdfGeneratorService;
  private final UserService userService;
  private final RabbitTemplate rabbitTemplate;

  /**
   * Создаёт отчёт pdf из объекта JasperPrint и конвертирует в массив байт
   *
   * @param accountId идентификатор проекта
   * @return массив байт pdf отчёта
   */
  public byte[] createProjectPDF(Long accountId, String routingKey) {

    User user = userService.findOrThrow(accountId);
    rabbitTemplate.setMessageConverter( new Jackson2JsonMessageConverter());
    ReportDto reportDto =rabbitTemplate.convertSendAndReceiveAsType(routingKey,UserDto.from(user), new ParameterizedTypeReference<ReportDto>() {});
    return reportDto.getBytes();
  }

  /**
   * Создаёт объект основных параметров проекта из его объекта-паспорта в виде набора пар
   * ключ-значение
   *
   * @return наборы пар ключ-значение основных параметров проекта
   */
  private Map<String, Object> createProjectPassportGeneralInfoCommonParams(Long userId) {
    Map<String, Object> parameters = new HashMap<>();
    User user = userService.findOrThrow(userId);
    parameters.put("userEmail", user.getEmail());
    parameters.put("userName", user.getName());
    parameters.put("userSurname", user.getSurname());
    parameters.put("userRole",  user.getRole().name());
    return parameters;
  }

  /**
   * Создает титульную страницу отчёта
   *
   * @return Возвращает титульную страницу отчёта
   */
  private JasperPrint getFrontPage(Long accountId) {
    Map<String, Object> commonParams = createProjectPassportGeneralInfoCommonParams(accountId);
    return pdfGeneratorService.createJasperPrint(
        Collections.emptyList(), commonParams, "report/template/report_page.jrxml");
  }
}

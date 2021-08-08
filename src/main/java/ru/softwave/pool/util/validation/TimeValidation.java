package ru.softwave.pool.util.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Time;

@Slf4j
public class TimeValidation implements ConstraintValidator<ValidStartEndTime, Object> {

  private String startTimePropertyName;
  private String endTimePropertyName;

  @Override
  public void initialize(ValidStartEndTime constraintAnnotation) {
    this.startTimePropertyName = constraintAnnotation.startTime();
    this.endTimePropertyName = constraintAnnotation.endTime();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    Time startTime = (Time) new BeanWrapperImpl(value).getPropertyValue(startTimePropertyName);
    Time endTime = (Time) new BeanWrapperImpl(value).getPropertyValue(endTimePropertyName);
    log.info("start time is {}", startTime);
    log.info("end time is {}", endTime);
    return startTime.toLocalTime().getMinute() == endTime.toLocalTime().getMinute();
  }
}

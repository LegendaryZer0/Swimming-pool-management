package ru.softwave.pool.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.softwave.pool.model.entity.TechnicalInfo;
import ru.softwave.pool.model.entity.User;
import ru.softwave.pool.service.UserService;

import java.util.UUID;

@Slf4j
@Aspect
@Component
public class AroundAnyUserRegistrationAspect {
  @Autowired private UserService userService;

  @Around(
      value =
          "execution(* ru.softwave.pool.service.AdminService.registrAdmin(..))  || execution(* ru.softwave.pool.service.TrainerService.registrTrainer(..) )  || execution(* ru.softwave.pool.service.VisitorService.registrVisitor(..))")
  public Object processUserConfirm(ProceedingJoinPoint joinPoint) throws Throwable {

    log.trace("Invoked Method {}", joinPoint.getSignature().getName());

    User user = (User) joinPoint.getArgs()[0];
    user.setIsConfirmed(false);
    user.setTechnicalInfo(
        TechnicalInfo.builder().user(user).uuidToConfirm(UUID.randomUUID()).build());

    Object returningValue = joinPoint.proceed();

    userService.sendConfirmEmail(user);

    return returningValue;
  }
}

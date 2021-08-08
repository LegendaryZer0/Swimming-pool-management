package ru.softwave.pool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.softwave.pool.security.config.SecurityConfig;

@Import({SecurityConfig.class})
@SpringBootApplication
public class SwimPoolApplication {

  public static void main(String[] args) {
    SpringApplication.run(SwimPoolApplication.class, args);
  }

  @Bean
  public PasswordEncoder bcryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

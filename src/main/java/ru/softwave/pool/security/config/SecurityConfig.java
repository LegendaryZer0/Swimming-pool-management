package ru.softwave.pool.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.softwave.pool.security.entrypoint.MyBasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired MyBasicAuthenticationEntryPoint authenticationEntryPoint;
  @Autowired private PasswordEncoder passwordEncoder;

  @Qualifier("userDetailsServiceImpl")
  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    try {
      auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.headers().frameOptions().sameOrigin();
    httpSecurity
        .httpBasic()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .authorizeRequests()
        .antMatchers("/authenticate/**", "/**", "css/**", "js/**")
        .permitAll()
        .and()
        .formLogin()
        .loginProcessingUrl("/login")
        .successForwardUrl("/success/welcome")
        .failureForwardUrl("/unauthorized")
        .and()
        .csrf()
        .disable();
  }
}

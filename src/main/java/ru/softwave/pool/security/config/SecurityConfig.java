package ru.softwave.pool.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.softwave.pool.filter.AccesTokenFilter;
import ru.softwave.pool.filter.RefreshTokenFilter;
import ru.softwave.pool.security.entrypoint.MyBasicAuthenticationEntryPoint;
import ru.softwave.pool.security.provider.JWTAuthProvider;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired MyBasicAuthenticationEntryPoint authenticationEntryPoint;
  @Autowired private PasswordEncoder passwordEncoder;

  @Qualifier("userDetailsServiceImpl")
  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired private AccesTokenFilter accesTokenFilter;
  @Autowired private RefreshTokenFilter refreshTokenFilter;

  @Autowired private JWTAuthProvider provider;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    try {
      auth.userDetailsService(userDetailsService)
              .passwordEncoder(passwordEncoder)
              .and()
              .authenticationProvider(provider);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.csrf().disable().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(
                    new DeviceResolverRequestFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(refreshTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(accesTokenFilter, refreshTokenFilter.getClass())
            .authorizeRequests().anyRequest().permitAll();
  }
}

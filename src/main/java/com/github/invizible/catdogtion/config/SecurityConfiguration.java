package com.github.invizible.catdogtion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import static com.github.invizible.catdogtion.Constants.ADMIN_ROLE;

/**
 * @author Alex
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Autowired
  private DataSource dataSource;

  @Value("${security.authoritiesByUsernameQuery}")
  private String authoritiesByUsernameQuery;

  @Bean
  public EvaluationContextExtension securityExtension() {
    return new SecurityEvaluationContextExtension();
  }

  @Override
  public void configure(WebSecurity web) throws Exception
  {
    web.ignoring()
      .antMatchers(HttpMethod.OPTIONS, "/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf()
      .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    .and()
      .formLogin()
      .loginProcessingUrl("/api/authentication")
      .successHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
      .failureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
        "Wrong username or password"))
      .permitAll()
    .and()
      .logout()
      .logoutUrl("/api/logout")
      .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
      .permitAll()
    .and()
      .authorizeRequests()
      .antMatchers("/api/account/registration").permitAll()
      .antMatchers(HttpMethod.GET, "/api/lots/**").permitAll()
      .antMatchers("/api/users").hasAuthority(ADMIN_ROLE)
      .antMatchers("/api/**").authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .jdbcAuthentication()
      .dataSource(dataSource)
      .authoritiesByUsernameQuery(authoritiesByUsernameQuery)
      .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

package com.kosinskyi.ecom.registry.security;

import com.kosinskyi.ecom.registry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private UserService userService;
  private SecurityExceptionHandlerFilter securityExceptionHandlerFilter;
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  private JwtAuthenticationEntryPoint unauthorizedHandler;

  @Autowired
  public SecurityConfig(
      UserService userService,
      SecurityExceptionHandlerFilter securityExceptionHandlerFilter,
      JwtAuthenticationFilter jwtAuthenticationFilter,
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
    this.userService = userService;
    this.securityExceptionHandlerFilter = securityExceptionHandlerFilter;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.unauthorizedHandler = jwtAuthenticationEntryPoint;
  }

  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .headers()
        .frameOptions()
        .disable()
      .and()
        .csrf()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler)
      .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
        .authorizeRequests()
        .antMatchers("/**/static/**", "/h2-console/**", "/api/auth/**")
        .permitAll()
        .anyRequest()
        .authenticated()
      .and()
        .logout()
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
        .permitAll();

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(securityExceptionHandlerFilter, WebAsyncManagerIntegrationFilter.class);
  }

  @Override
  public UserDetailsService userDetailsService() {
    return userService;
  }

}

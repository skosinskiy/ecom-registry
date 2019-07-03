package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.dto.request.LoginRequest;
import com.kosinskyi.ecom.registry.dto.response.LoginResponse;
import com.kosinskyi.ecom.registry.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private SecurityService securityService;

  @Autowired
  public AuthController(SecurityService securityService) {
    this.securityService = securityService;
  }

  @PostMapping("signin")
  public ResponseEntity<LoginResponse> generateJwt(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(securityService.setAuthenticationAndGenerateJwt(loginRequest));
  }

  //TODO remove after implementing real endpoints
  @GetMapping("test")
  @PreAuthorize("hasAuthority('MANAGE_REGISTRY')")
  public String test() {
    return "OK!";
  }

  @GetMapping("test2")
  @PreAuthorize("hasAuthority('MISSING_AUTHORITY')")
  public void test2() { }

}

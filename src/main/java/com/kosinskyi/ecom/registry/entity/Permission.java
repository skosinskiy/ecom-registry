package com.kosinskyi.ecom.registry.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
  MANAGE_REGISTRY,
  MANAGE_ACCOUNTS;

  @Override
  public String getAuthority() {
    return this.toString();
  }
}

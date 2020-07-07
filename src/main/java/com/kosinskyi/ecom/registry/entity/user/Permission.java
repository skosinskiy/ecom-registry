package com.kosinskyi.ecom.registry.entity.user;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
  MANAGE_REGISTRY,
  MANAGE_ACCOUNTS,
  MANAGE_CRITERIA;

  @Override
  public String getAuthority() {
    return this.toString();
  }
}

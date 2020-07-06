package com.kosinskyi.ecom.registry.entity.file.constants;

public enum Extension {

  XLS("xls"),
  XLSX("xlsx"),
  ZIP("zip");

  private final String value;

  Extension(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

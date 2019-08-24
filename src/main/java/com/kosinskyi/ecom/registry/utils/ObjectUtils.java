package com.kosinskyi.ecom.registry.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Objects;

@Component
public class ObjectUtils {

  private String[] getNullPropertyNames (Object source) {
    BeanWrapper src = new BeanWrapperImpl(source);
    return Arrays
        .stream(src.getPropertyDescriptors())
        .filter(propertyDescriptor -> Objects.isNull(src.getPropertyValue(propertyDescriptor.getName())))
        .map(PropertyDescriptor::getName)
        .distinct()
        .toArray(String[]::new);
  }

  public void copyNotNullProperties(Object src, Object target) {
    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
  }
}

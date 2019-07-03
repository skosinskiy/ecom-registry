package com.kosinskyi.ecom.registry.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutExpressionsHolder {

  @Pointcut("execution(* com.kosinskyi.ecom.registry.controller..*(..))")
  public void controllerLayer() {
    //Technical Method
  }

  @Pointcut("execution(* com.kosinskyi.ecom.registry.service..*(..))")
  public void serviceLayer() {
    //Technical Method
  }

  @Pointcut("execution(* com.kosinskyi.ecom.registry.repository..*(..))")
  public void repositoryLayer() {
    //Technical Method
  }

}

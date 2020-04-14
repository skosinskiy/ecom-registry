package com.kosinskyi.ecom.registry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RootController {

  @GetMapping("/")
  public String redirectToAdmin() {
    return "forward:/index.html";
  }

  @GetMapping("/{path:^(?:(?!static|.html).)*$}/**")
  public String redirectToAdmin(@PathVariable String path) {
    return "forward:/index.html";
  }

}

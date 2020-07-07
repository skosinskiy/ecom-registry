package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.dto.response.registry.daily.DailyRegistryParseCriteriaResponse;
import com.kosinskyi.ecom.registry.mapping.registry.daily.DailyRegistryParseCriteriaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/parse/criteria/daily")
public class DailyRegistryParseCriteriaController {

  private final DailyRegistryParseCriteriaMapper mapper;

  @Autowired
  public DailyRegistryParseCriteriaController(DailyRegistryParseCriteriaMapper mapper) {
    this.mapper = mapper;
  }

  @GetMapping
  public ResponseEntity<Page<DailyRegistryParseCriteriaResponse>> findAll(Pageable pageable) {
    return ResponseEntity.ok(mapper.findAll(pageable, DailyRegistryParseCriteriaResponse.class));
  }

}

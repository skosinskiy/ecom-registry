package com.kosinskyi.ecom.registry.controller;

import com.kosinskyi.ecom.registry.dto.response.registry.daily.DailyRegistryResponse;
import com.kosinskyi.ecom.registry.mapping.registry.DailyRegistryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("api/registry/daily")
public class DailyRegistryController {

  private final DailyRegistryMapper mapper;

  @Autowired
  public DailyRegistryController(DailyRegistryMapper mapper) {
    this.mapper = mapper;
  }

  @GetMapping
  public ResponseEntity<Page<DailyRegistryResponse>> findAllByYearAndMonth(
      @RequestParam Integer year, @RequestParam Integer month, Pageable pageable) {
    return ResponseEntity.ok(mapper.findAllByYearAndMonth(year, month, pageable, DailyRegistryResponse.class));
  }

  @PostMapping
  public ResponseEntity<DailyRegistryResponse> create(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
      @RequestParam MultipartFile file) {
    return ResponseEntity.ok(mapper.create(date, file));
  }

  @DeleteMapping("{registryId}")
  public ResponseEntity<DailyRegistryResponse> delete(@PathVariable Long registryId) {
    return ResponseEntity.ok(mapper.delete(registryId, DailyRegistryResponse.class));
  }

  @GetMapping("parse/{registryId}")
  public ResponseEntity<DailyRegistryResponse> parse(@PathVariable Long registryId) {
    return ResponseEntity.ok(mapper.parse(registryId));
  }

}

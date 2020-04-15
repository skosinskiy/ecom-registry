package com.kosinskyi.ecom.registry.service.registry.daily.parsing.cache;

import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistryParseCriteria;
import com.kosinskyi.ecom.registry.error.exception.ActionForbiddenException;
import com.kosinskyi.ecom.registry.service.registry.daily.parsing.DailyRegistryParseCriteriaService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DailyRegistryParseCacheService {

  private DailyRegistryParseCriteriaService parseCriteriaService;

  @Autowired
  public DailyRegistryParseCacheService(DailyRegistryParseCriteriaService parseCriteriaService) {
    this.parseCriteriaService = parseCriteriaService;
  }

  public DailyRegistryParseCache getCache(Row headerRow, LocalDate date, Long id) {
    List<DailyRegistryParseCriteria> parseCriteriaList = parseCriteriaService.findAll();
    DailyRegistryParseCache cache = new DailyRegistryParseCache();
    cache.setId(id);
    cache.setDate(date);
    cache.setParseCriteriaList(parseCriteriaList);
    cache.setCriteriaColumnIndexCache(getCriteriaColumnIndexCache(parseCriteriaList, headerRow));
    return cache;
  }

  private Map<DailyRegistryParseCriteria, Integer> getCriteriaColumnIndexCache(
      List<DailyRegistryParseCriteria> parseCriteriaList, Row headerRow) {
    return parseCriteriaList
        .stream()
        .collect(
            Collectors.toMap(
                criteria -> criteria,
                criteria -> getSearchColumnIndex(headerRow, criteria.getFilterColumnName())
            ));
  }

  private Integer getSearchColumnIndex(Row row, String columnName) {
    return IntStream
        .rangeClosed(0, row.getLastCellNum())
        .mapToObj(row::getCell)
        .filter(cell -> cell.getStringCellValue().equals(columnName))
        .findFirst()
        .map(Cell::getColumnIndex)
        .orElseThrow(() -> new ActionForbiddenException(String.format(
            "No cell with value %s found in first row", columnName
        )));
  }

}

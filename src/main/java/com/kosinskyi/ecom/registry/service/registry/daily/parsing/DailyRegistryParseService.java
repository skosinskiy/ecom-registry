package com.kosinskyi.ecom.registry.service.registry.daily.parsing;

import com.kosinskyi.ecom.registry.entity.file.FileItem;
import com.kosinskyi.ecom.registry.entity.file.constants.Extension;
import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistry;
import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistryParseCriteria;
import com.kosinskyi.ecom.registry.error.exception.ApplicationException;
import com.kosinskyi.ecom.registry.service.file.FileItemService;
import com.kosinskyi.ecom.registry.service.registry.daily.parsing.cache.DailyRegistryParseCache;
import com.kosinskyi.ecom.registry.service.registry.daily.parsing.cache.DailyRegistryParseCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

@Service
@Slf4j
public class DailyRegistryParseService {

  private FileItemService fileItemService;
  private DailyRegistryParseCacheService cacheService;


  @Autowired
  public DailyRegistryParseService(FileItemService fileItemService, DailyRegistryParseCacheService cacheService) {
    this.fileItemService = fileItemService;
    this.cacheService = cacheService;
  }

  @Async
  public CompletableFuture<FileItem> parse(DailyRegistry dailyRegistry) {
    Long id = dailyRegistry.getId();
    LocalDate date = dailyRegistry.getRegistryDate();
    log.info("Received request for parsing daily registry: {}, date: {}", id, date);
    long start = System.currentTimeMillis();
    Sheet originalSheet = getWorkbook(dailyRegistry.getRegistryItem()).getSheetAt(0);
    Row headerRow = originalSheet.getRow(originalSheet.getFirstRowNum());
    DailyRegistryParseCache cache = cacheService.getCache(headerRow, date, id);
    Map<DailyRegistryParseCriteria, List<Row>> criteriaRowsMap = processOriginalSheet(originalSheet, cache);
    Map<String, byte[]> zipMap = transformProcessedRowsToWorkbookBytes(criteriaRowsMap, date, headerRow);
    FileItem fileItem = fileItemService.saveZip(zipMap);
    long time = System.currentTimeMillis() - start;
    log.info("Successfully parsed daily registry: {}, date: {} in {} seconds", id, date, time / 1000);
    return CompletableFuture.completedFuture(fileItem);
  }

  private Workbook getWorkbook(FileItem fileItem) {
    try {
      byte[] binaryFile = fileItemService.getBinary(fileItem);
      Long id = fileItem.getId();
      log.info("Transforming binary file with id: {} to workbook", id);
      long start = System.currentTimeMillis();
      XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(binaryFile));
      long time = System.currentTimeMillis() - start;
      log.info("Transformed binary file with id: {} to workbook successfully in {} seconds", id, time / 1000);
      return workbook;
    } catch (IOException exc) {
      log.error(exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }

  private Map<DailyRegistryParseCriteria, List<Row>> processOriginalSheet(
      Sheet originalSheet, DailyRegistryParseCache cache) {
    Long id = cache.getId();
    LocalDate date = cache.getDate();
    log.info("Starting parsing daily registry: {}, date: {}", id, date);
    long start = System.currentTimeMillis();
    Map<DailyRegistryParseCriteria, List<Row>> criteriaRowMap = new HashMap<>();
    IntStream
        .rangeClosed(1, originalSheet.getLastRowNum())
        .mapToObj(originalSheet::getRow)
        .forEach(row -> findCriteriaByRow(cache, row)
            .ifPresent(criteria -> {
              List<Row> rowList = criteriaRowMap.getOrDefault(criteria, new ArrayList<>());
              rowList.add(row);
              criteriaRowMap.put(criteria, rowList);
            }));
    long time = System.currentTimeMillis() - start;
    log.info("Processing finished for daily registry: {}, date: {} in {} seconds", id, date, time / 1000);
    return criteriaRowMap;
  }

  private Optional<DailyRegistryParseCriteria> findCriteriaByRow(DailyRegistryParseCache cacheHolder, Row row) {
    return cacheHolder
        .getParseCriteriaList()
        .stream()
        .filter(criteria -> {
          Integer searchColumnIndex = cacheHolder.getCriteriaColumnIndexCache().get(criteria);
          if (searchColumnIndex != null) {
            String cellValue = row.getCell(searchColumnIndex, CREATE_NULL_AS_BLANK).getStringCellValue();
            return criteria.getFilterValues().contains(cellValue);
          }
          return false;
        })
        .findFirst();
  }

  private Map<String, byte[]> transformProcessedRowsToWorkbookBytes(
      Map<DailyRegistryParseCriteria, List<Row>> criteriaRowsMap, LocalDate date, Row headerRow) {
    log.info("Transforming processed rows to workbooks");
    long start = System.currentTimeMillis();
    Map<String, byte[]> zipMap = new HashMap<>();
    criteriaRowsMap.forEach((criteria, rows) -> {
      log.debug("Creating workbook for {}", criteria.getName());
      long s = System.currentTimeMillis();
      Workbook workbook = createWorkbookWithHeader(headerRow);
      Sheet sheet = workbook.getSheetAt(0);
      rows.forEach(row -> copyRow(row, sheet.createRow(sheet.getLastRowNum() + 1), getDateStyle(workbook)));
      zipMap.put(getRegistryFileName(criteria.getName(), date), getBytesFromWorkbook(workbook));
      long t = System.currentTimeMillis() - s;
      log.debug("Creating workbook for {} finished in {} ms", criteria.getName(), t);
    });
    long time = System.currentTimeMillis() - start;
    log.info("Transforming rows to workbooks finished in {} seconds", time / 1000);
    return zipMap;
  }

  private Workbook createWorkbookWithHeader(Row headerRow) {
    Workbook createdWorkbook = new XSSFWorkbook();
    Sheet createdWorkbookSheet = createdWorkbook.createSheet();
    copyRow(headerRow, createdWorkbookSheet.createRow(0));
    return createdWorkbook;
  }

  private CellStyle getDateStyle(Workbook workbook) {
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/MM/yyyy hh:mm:ss"));
    return cellStyle;
  }

  private String getRegistryFileName(String name, LocalDate date) {
    return String.format("%s %s.%s", name, date, Extension.XLSX.getValue());
  }

  private byte[] getBytesFromWorkbook(Workbook workbook) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      workbook.write(baos);
      workbook.close();
      return baos.toByteArray();
    } catch (IOException exc) {
      log.error(exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }

  private void copyRow(Row source, Row target) {
    IntStream
        .range(0, source.getLastCellNum())
        .forEach(i -> copyCell(source.getCell(i, CREATE_NULL_AS_BLANK), target.createCell(i), null));
  }

  private void copyRow(Row source, Row target, CellStyle dateStyle) {
    IntStream
        .range(0, source.getLastCellNum())
        .forEach(i -> copyCell(source.getCell(i, CREATE_NULL_AS_BLANK), target.createCell(i), dateStyle));
  }

  private void copyCell(Cell source, Cell target, CellStyle dateStyle) {
    if (source.getCellType().equals(CellType.STRING)) {
      target.setCellValue(source.getStringCellValue());
    }
    if (source.getCellType().equals(CellType.NUMERIC)) {
      if (DateUtil.isCellDateFormatted(source)) {
        target.setCellStyle(dateStyle);
        target.setCellValue(source.getDateCellValue());
      } else {
        target.setCellValue(source.getNumericCellValue());
      }
    }
    if (source.getCellType().equals(CellType.BOOLEAN)) {
      target.setCellValue(source.getBooleanCellValue());
    }
    if (source.getCellType().equals(CellType.ERROR)) {
      target.setCellValue(source.getErrorCellValue());
    }
    if (source.getCellType().equals(CellType.FORMULA)) {
      target.setCellValue(source.getCellFormula());
    }
  }

}

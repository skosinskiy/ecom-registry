package com.kosinskyi.ecom.registry.service.registry.daily.parsing;

import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistry;
import com.kosinskyi.ecom.registry.entity.registry.daily.DailyRegistryParseCriteria;
import com.kosinskyi.ecom.registry.error.exception.ActionForbiddenException;
import com.kosinskyi.ecom.registry.error.exception.ApplicationException;
import com.kosinskyi.ecom.registry.service.file.RegistryFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

@Service
@Slf4j
public class DailyRegistryParseService {

  private static final String FILE_EXTENSION = "xlsx";

  private RegistryFileService registryFileService;
  private DailyRegistryParseCriteriaService parseCriteriaService;

  @Autowired
  public DailyRegistryParseService(
      @Qualifier("localRegistryFileService") RegistryFileService registryFileService,
      DailyRegistryParseCriteriaService parseCriteriaService) {
    this.registryFileService = registryFileService;
    this.parseCriteriaService = parseCriteriaService;
  }

  @Async
  public void parse(DailyRegistry dailyRegistry) {
    LocalDate registryDate = dailyRegistry.getRegistryDate();
    Long dailyRegistryId = dailyRegistry.getId();
    log.info("Received request for parsing daily registry {}, date: {}", dailyRegistryId, registryDate);
    Sheet originalSheet = getWorkbook(dailyRegistry.getFileItem().getFileKey()).getSheetAt(0);
    Map<Long, Workbook> map = new HashMap<>();
    List<DailyRegistryParseCriteria> parseCriteriaList = parseCriteriaService.findAll();
    log.info("Starting parsing daily registry {}, date: {}", dailyRegistryId, registryDate);
    for (int i = 1; i <= originalSheet.getLastRowNum(); i++) {
      Row originalRow = originalSheet.getRow(i);
      findCriteriaByRow(parseCriteriaList, originalSheet, originalRow).ifPresent(criteria -> {
        Workbook createdWorkbook = getOrPutWorkbook(map, criteria.getId(), originalSheet);
        Sheet sheet = createdWorkbook.getSheetAt(0);
        copyRow(originalRow, sheet.createRow(sheet.getLastRowNum() + 1), createdWorkbook);
      });
    }
    log.info("Processing finished for daily registry {}, date: {}", dailyRegistryId, registryDate);
    map.forEach((k, v) -> saveWorkbook(v, getFileName(findCriteriaById(parseCriteriaList, k).getName(), registryDate)));
  }

  private Workbook getOrPutWorkbook(Map<Long, Workbook> map, Long criteriaId, Sheet originalSheet) {
    Workbook workbook = map.get(criteriaId);
    if (Objects.isNull(workbook)) {
      workbook = createWorkbook(originalSheet);
      map.put(criteriaId, workbook);
    }
    return workbook;
  }

  private Workbook createWorkbook(Sheet originalSheet) {
    Workbook createdWorkbook = new XSSFWorkbook();
    Sheet createdWorkbookSheet = createdWorkbook.createSheet();
    createHeaderRow(originalSheet, createdWorkbookSheet, createdWorkbook);
    return createdWorkbook;
  }

  private DailyRegistryParseCriteria findCriteriaById(List<DailyRegistryParseCriteria> parseCriteriaList , Long id) {
    return parseCriteriaList
        .stream()
        .filter(c -> c.getId().equals(id))
        .findFirst()
        .orElseThrow(NoSuchElementException::new);
  }

  private Optional<DailyRegistryParseCriteria> findCriteriaByRow(
      List<DailyRegistryParseCriteria> parseCriteriaList, Sheet sheet, Row row) {
    return parseCriteriaList
        .stream()
        .filter(criteria -> {
          int searchColumnIndex = getSearchColumnIndex(sheet, criteria.getFilterColumnName());
          String cellValue = row.getCell(searchColumnIndex, CREATE_NULL_AS_BLANK).getStringCellValue();
          return criteria.getFilterValues().contains(cellValue);
        })
        .findFirst();
  }

  private void createHeaderRow(Sheet sourceSheet, Sheet targetSheet, Workbook workbook) {
    Row createdWorkbookSheetRow = targetSheet.createRow(0);
    copyRow(sourceSheet.getRow(sourceSheet.getFirstRowNum()), createdWorkbookSheetRow, workbook);
  }

  private String getFileName(String name, LocalDate date) {
    return String.format("%s %s.%s", name, date, FILE_EXTENSION);
  }

  private Workbook getWorkbook(String fileKey) {
    try {
      return new XSSFWorkbook(new ByteArrayInputStream(registryFileService.getBinaryFile(fileKey)));
    } catch (IOException exc) {
      log.error(exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }

  private void saveWorkbook(Workbook workbook, String name) {
    try {
      FileOutputStream outputStream = new FileOutputStream(name);
      workbook.write(outputStream);
      outputStream.close();
      workbook.close();
    } catch (IOException exc) {
      log.error(exc.getMessage());
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }

  private void copyRow(Row source, Row target, Workbook workbook) {
    IntStream
        .range(0, source.getLastCellNum())
        .forEach(i -> copyCell(source.getCell(i, CREATE_NULL_AS_BLANK), target.createCell(i), workbook));
  }

  private void copyCell(Cell source, Cell target, Workbook workbook) {
    if (source.getCellType().equals(CellType.STRING)) {
      target.setCellValue(source.getStringCellValue());
    }
    if (source.getCellType().equals(CellType.NUMERIC)) {
      if (DateUtil.isCellDateFormatted(source)) {
        target.setCellStyle(getDateStyle(workbook));
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

  private CellStyle getDateStyle(Workbook workbook) {
    CellStyle cellStyle = workbook.createCellStyle();
    CreationHelper createHelper = workbook.getCreationHelper();
    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy hh:mm:ss"));
    return cellStyle;
  }

  private Integer getSearchColumnIndex(Sheet sheet, String criteria) {
    Row row = sheet.getRow(sheet.getFirstRowNum());
    for (Cell cell : row) {
      if (cell.getStringCellValue().equals(criteria)) {
        return cell.getColumnIndex();
      }
    }
    throw new ActionForbiddenException(String.format(
        "No cell with value %s found in first row", criteria
    ));
  }

}

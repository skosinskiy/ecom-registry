package com.kosinskyi.ecom.registry.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaskingPatternLayout extends PatternLayout {

  private Pattern multilinePattern;
  private List<String> maskPatterns = new ArrayList<>();
  private static final String JSON_PATTERN = "\"%s\"\\s*:\\s*\"(.*?)\"[,}]";
  private static final String OBJECT_PATTERN = "((?!, )|(?!\\())%s\\s*=\\s*([\\s\\S]*?)((?=, )|(?=\\)))";
  private static final String PATTERN_DELIMITER = "|";
  private static final String MASK_VALUE = "******";
  private static final int MAX_MESSAGE_LENGTH = 10000;

  public void addMaskPattern(String maskPattern) {
    maskPatterns.add(maskPattern);
    multilinePattern = Pattern.compile(
        maskPatterns.stream()
            .map(this::getJsonAndObjectPattern)
            .collect(Collectors.joining(PATTERN_DELIMITER)),
        Pattern.MULTILINE
    );
  }

  private String getJsonAndObjectPattern(String pattern) {
    return String.format(JSON_PATTERN, pattern) + PATTERN_DELIMITER + String.format(OBJECT_PATTERN, pattern);
  }

  @Override
  public String doLayout(ILoggingEvent event) {
    return maskMessage(super.doLayout(event), event);
  }

  private String maskMessage(String message, ILoggingEvent event) {
    if (LoggerFactory.getLogger(event.getLoggerName()).isTraceEnabled() || Objects.isNull(multilinePattern)) {
      return message;
    }
    StringBuilder sb = new StringBuilder(message);
    Matcher matcher = multilinePattern.matcher(sb);
    while (matcher.find()) {
      IntStream
          .rangeClosed(1, matcher.groupCount())
          .filter(group -> Objects.nonNull(matcher.group(group)))
          .forEach(group ->
              IntStream
                  .range(matcher.start(group), matcher.end(group))
                  .forEach(i -> sb.setCharAt(i, '*')));
    }
    return truncateLogMessage(sb.toString().replaceAll("\\*{7,}", MASK_VALUE));
  }

  private String truncateLogMessage(String message) {
    int length = message.length();
    if (length > MAX_MESSAGE_LENGTH) {
      return String.format("%s ...[content-length:%d]\r\n", message.substring(0, MAX_MESSAGE_LENGTH), length);
    }
    return message;
  }

}
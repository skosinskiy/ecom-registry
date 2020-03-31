package com.kosinskyi.ecom.registry.logging.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosinskyi.ecom.registry.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class HttpLoggingService {

  private static final String REQUEST_PREFIX = "Received HTTP request: ";
  private static final String RESPONSE_PREFIX = "Sent HTTP response: ";
  private static final String BLOCK_PREFIX = "=[";
  private static final String BLOCK_SUFFIX = "] ";
  private static final String PATH = "path";
  private static final String PARAMS = "parameters";
  private static final String METHOD = "method";
  private static final String HEADERS = "headers";
  private static final String CLIENT = "client";
  private static final String USER = "user";
  private static final String BODY = "body";

  private ObjectMapper objectMapper;
  private HttpServletRequest httpServletRequest;

  @Autowired
  public HttpLoggingService(ObjectMapper objectMapper, HttpServletRequest httpServletRequest) {
    this.objectMapper = objectMapper;
    this.httpServletRequest = httpServletRequest;
  }

  public void logRequest(Object body) {
    StringBuilder builder = new StringBuilder();
    builder.append(REQUEST_PREFIX);
    append(PATH, httpServletRequest.getRequestURI(), builder);
    append(PARAMS, buildParametersMap(httpServletRequest), builder);
    append(METHOD, httpServletRequest.getMethod(), builder);
    append(HEADERS, buildHeadersMap(httpServletRequest), builder);
    append(CLIENT, httpServletRequest.getRemoteAddr(), builder);
    append(USER, httpServletRequest.getRemoteUser(), builder);
    append(BODY, getBodyString(body), builder);
    log.info(builder.toString());
  }

  private void append(String paramName, Object param, StringBuilder builder) {
    if (Objects.nonNull(param)) {
      builder
          .append(paramName)
          .append(BLOCK_PREFIX)
          .append(param)
          .append(BLOCK_SUFFIX);
    }
  }

  private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
    Map<String, String> resultMap = new HashMap<>();
    Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String key = parameterNames.nextElement();
      resultMap.put(key, httpServletRequest.getParameter(key));
    }
    return resultMap;
  }

  private Map<String, String> buildHeadersMap(HttpServletRequest request) {
    Map<String, String> map = new HashMap<>();
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String key = headerNames.nextElement();
      map.put(key, request.getHeader(key));
    }
    return map;
  }

  private Map<String, String> buildHeadersMap(HttpServletResponse response) {
    Map<String, String> map = new HashMap<>();
    response.getHeaderNames().forEach(header -> map.put(header, response.getHeader(header)));
    return map;
  }

  private Map<String, String> buildHeadersMap(HttpOutputMessage outputMessage) {
    Map<String, String> map = new HashMap<>();
    HttpHeaders headers = outputMessage.getHeaders();
    headers.keySet().forEach(header -> map.put(header, headers.getFirst(header)));
    return map;
  }

  public void logResponse(HttpServletResponse httpServletResponse, Object body) {
    logResponse(body, buildHeadersMap(httpServletResponse));
  }

  public void logResponse(HttpOutputMessage outputMessage, Object body) {
    logResponse(body, buildHeadersMap(outputMessage));
  }

  private void logResponse(Object body, Map<String, String> headersMap) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(RESPONSE_PREFIX);
    append(PATH, httpServletRequest.getRequestURI(), stringBuilder);
    append(METHOD, httpServletRequest.getMethod(), stringBuilder);
    append(HEADERS, headersMap, stringBuilder);
    append(BODY, getBodyString(body), stringBuilder);
    log.info(stringBuilder.toString());
  }


  private String getBodyString(Object body) {
    try {
      return objectMapper.writeValueAsString(body);
    } catch (JsonProcessingException exc) {
      throw new ApplicationException(exc.getMessage(), exc);
    }
  }
}
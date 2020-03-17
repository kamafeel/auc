package com.auc.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * jackson json的工具
 * @author zhangqi73
 */
public final class JsonUtil {

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Serialize any Java value as a String.
   */
  public static String toStr(Object object) throws JsonProcessingException {
    return mapper.writeValueAsString(object);
  }

  public static String toPrettyStr(Object object) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
  }

  /**
   * Deserialize JSON content from given JSON content String.
   */
  public static <T> T toT(String content, Class<T> valueType) throws IOException {
    return mapper.readValue(content, valueType);
  }

  /**
   * Deserialize JSON content from given JSON content String.
   */
  public static JsonNode toJsonNode(String content) throws IOException {
    return mapper.readTree(content);
  }


  public static ObjectNode createObjectNode() {
    return mapper.createObjectNode();
  }

  public static ArrayNode createArrayNode() {
    return mapper.createArrayNode();
  }

  /**
   * Deserialize JSON content from given JSON content String.
   */
  public static <T> T toT(String content, Type type) throws IOException {
    return mapper.readValue(content, mapper.getTypeFactory().constructType(type));
  }
}
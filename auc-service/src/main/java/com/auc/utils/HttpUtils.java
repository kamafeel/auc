package com.auc.utils;

import com.auc.common.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;

@Component
public class HttpUtils {

  @Autowired
  private RestTemplate restTemplate;

  public String formPost(String uri, HashMap<String,Object> params) {
    HttpHeaders headers = new HttpHeaders();
    MediaType type = MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    headers.setContentType(type);
    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, Object> param = iterator.next();
      map.add(param.getKey(), param.getValue());
    }
    HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(map, headers);
    String result = restTemplate.postForObject(uri, formEntity, String.class);
    return result;
  }

}

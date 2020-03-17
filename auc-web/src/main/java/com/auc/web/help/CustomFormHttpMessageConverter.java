package com.auc.web.help;

import com.auc.common.utils.JsonUtil;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/**
 * x-www-form-urlencoded
 * 的默认转换
 */

public class CustomFormHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  private Charset charset = DEFAULT_CHARSET;

  private List<MediaType> supportedMediaTypes = new ArrayList<>();

  public CustomFormHttpMessageConverter() {
    supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
    super.setSupportedMediaTypes(supportedMediaTypes);
  }

  @Override
  public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    MediaType contentType = inputMessage.getHeaders().getContentType();
    Charset charset =
        contentType != null && contentType.getCharset() != null ? contentType.getCharset()
            : this.charset;
    String body = StreamUtils.copyToString(inputMessage.getBody(), charset);

    String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
    MultiValueMap<String, String> result = new LinkedMultiValueMap<>(pairs.length);
    for (String pair : pairs) {
      int idx = pair.indexOf('=');
      if (idx == -1) {
        result.add(URLDecoder.decode(pair, charset.name()), null);
      } else {
        String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
        String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
        result.add(name, value);
      }
    }
    return JsonUtil.toT(JsonUtil.toStr(result.toSingleValueMap()),type);
  }

  @Override
  protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    // TODO Auto-generated method stub
    throw new RuntimeException("custom form converter donot implements writeInternal");
  }

  @Override
  protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    // TODO Auto-generated method stub
    throw new RuntimeException("custom form converter donot implements readInternal");
  }
}

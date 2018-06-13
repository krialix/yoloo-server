package com.yoloo.server.oauth2.exception;

import com.yoloo.server.common.exception.model.Error;
import com.yoloo.server.common.exception.model.ErrorResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;
import org.springframework.web.client.RestTemplate;

import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class YolooOAuth2ExceptionRenderer extends DefaultOAuth2ExceptionRenderer {

  public YolooOAuth2ExceptionRenderer() {
    setMessageConverters(messageConverters());
  }

  private List<HttpMessageConverter<?>> messageConverters() {
    final List<HttpMessageConverter<?>> result =
        new ArrayList<>(new RestTemplate().getMessageConverters());

    GsonHttpMessageConverter converter =
        new GsonHttpMessageConverter() {
          @Override
          protected void writeInternal(final Object object, final Type type, final Writer writer) {
            final OAuth2Exception ex = (OAuth2Exception) object;

            Error error =
                Error.builder().error(ex.getOAuth2ErrorCode()).message(ex.getMessage()).build();

            getGson().toJson(ErrorResponse.of(error), writer);
          }
        };

    result.add(0, converter);

    return result;
  }
}

package ru.hh.api.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpClient {
  private String accessToken;
  private String userAgent;
  private int socketTimeoutInMs;
  private int connectTimeoutInMs;
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public HttpClient() {
    // Load our own config values from the default location,
    // application.conf
    Config conf = ConfigFactory.load();
    // Check properties against default reference location,
    // reference.conf
    conf.checkValid(ConfigFactory.defaultReference());

    accessToken = conf.getString("access-token");
    userAgent = conf.getString("user-agent");
    socketTimeoutInMs = conf.getInt("socket-timeout-ms");
    connectTimeoutInMs = conf.getInt("connect-timeout-ms");
  }

  public Request get(String url) {
    Request request = Request.Get(url);
    setCommonRequestParams(request);
    return request;
  }

  public Request post(String url, String body) {
    Request request = Request.Post(url);
    setCommonRequestParams(request);
    request.bodyString(body,
        ContentType.APPLICATION_FORM_URLENCODED
            .withCharset(StandardCharsets.UTF_8));
    log.debug("prepared next request body: " + body);
    return request;
  }
  // TODO add methods for all http methods

  public Response send(Request request) {
    Response actualResponse = ResponseFactory.getNewEmpty();
    log.info("send: " + request);
    try {
      HttpResponse httpResponse;
        httpResponse = request.execute().returnResponse();

      HttpEntity httpEntity = httpResponse.getEntity();
      actualResponse
          .setStatusLine(httpResponse.getStatusLine().toString());

      if (httpEntity != null) {
        actualResponse
            .setContentLength(httpEntity.getContentLength())
            .setContentType(httpEntity.getContentType().toString())
            .setContent(EntityUtils.toString(httpEntity));
        log.debug("got response body: " + actualResponse.getContent());
        if (httpEntity.getContentEncoding() != null) {
          actualResponse
              .setContentEncoding(httpEntity.getContentEncoding().toString());
        }
      }

      // get headers
      Header[] headers = httpResponse.getAllHeaders();
      String[] headerStrings = new String[headers.length];
      for (int i = 0; i < headers.length; i++) {
        headerStrings[i] = headers[i].getName() + ": " + headers[i].getValue();
      }
      actualResponse
          .setHeadersList(headerStrings);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return actualResponse;
  }

  private void setCommonRequestParams(Request request) {
    request.addHeader("Authorization", "Bearer " + accessToken)
        .userAgent(userAgent)
        .connectTimeout(connectTimeoutInMs)
        .socketTimeout(socketTimeoutInMs);
  }
}

package ru.hh.api.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClient extends Log {
  private String accessToken;
  private String userAgent;
  private int socketTimeoutInMs;
  private int connectTimeoutInMs;

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

  public synchronized Request get(String url) {
    Request request = Request.Get(url);
    setCommonRequestParams(request);
    return request;
  }

  public synchronized Request post(String url) {
    Request request = Request.Post(url);
    setCommonRequestParams(request);
    return request;
  }
  // TODO add methods for all http methods

  public synchronized ActualResponse send(Request request) {
    ActualResponse actualResponse = new ActualResponse();
    try {
      HttpResponse httpResponse = request.execute().returnResponse();
      HttpEntity httpEntity = httpResponse.getEntity();
      actualResponse
          .setStatusLine(httpResponse.getStatusLine().toString());

      if (httpEntity != null) {
        actualResponse
            .setContentLength(httpEntity.getContentLength())
            .setContentType(httpEntity.getContentType().toString())
            .setContent(EntityUtils.toString(httpEntity));
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
          .setHeadersFullList(headerStrings);
    } catch (IOException e) {
      warn(e.getMessage());
    }
    return actualResponse;
  }

  private synchronized void setCommonRequestParams(Request request) {
    debug("request: " + request);
    request.addHeader("Authorization", "Bearer " + accessToken)
        .userAgent(userAgent)
        .connectTimeout(connectTimeoutInMs)
        .socketTimeout(socketTimeoutInMs);
  }
}

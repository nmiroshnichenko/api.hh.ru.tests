package ru.hh.api.utils;

/**
 * Created by n.miroshnichenko <nikbox@ya.ru> on 31.03.15 23:35.
 */
public final class ActualResponse {
  private volatile String statusLine;
  private volatile long contentLength;
  private volatile String contentType;
  private volatile String contentEncoding;
  private volatile String content;
  private volatile String[] headersFullList;

  public String getStatusLine() {
    return statusLine;
  }

  public ActualResponse setStatusLine(String statusLine) {
    this.statusLine = statusLine;
    return this;
  }

  public long getContentLength() {
    return contentLength;
  }

  public ActualResponse setContentLength(long contentLength) {
    this.contentLength = contentLength;
    return this;
  }

  public String getContentType() {
    return contentType;
  }

  public ActualResponse setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  public String getContentEncoding() {
    return contentEncoding;
  }

  public ActualResponse setContentEncoding(String contentEncoding) {
    this.contentEncoding = contentEncoding;
    return this;
  }

  public String getContent() {
    return content;
  }

  public ActualResponse setContent(String content) {
    this.content = content;
    return this;
  }

  public String[] getHeadersFullList() {
    return headersFullList;
  }

  public ActualResponse setHeadersFullList(String[] headersFullList) {
    this.headersFullList = headersFullList;
    return this;
  }
}

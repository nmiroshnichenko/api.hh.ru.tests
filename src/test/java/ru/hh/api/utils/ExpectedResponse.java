package ru.hh.api.utils;

/**
 * Created by n.miroshnichenko <nikbox@ya.ru> on 31.03.15 18:12.
 */
public class ExpectedResponse {
  private volatile String statusLine = "HTTP/1.1 200 OK";
  private volatile long contentLength;
  private volatile String contentType;
  private volatile String contentEncoding;
  private volatile String content;
  private volatile String[] headersPartialList;
  private volatile String[] headersFullList;

  public String getStatusLine() {
    return statusLine;
  }

  public ExpectedResponse setStatusLine(String statusLine) {
    this.statusLine = statusLine;
    return this;
  }

  public long getContentLength() {
    return contentLength;
  }

  public ExpectedResponse setContentLength(int contentLength) {
    this.contentLength = contentLength;
    return this;
  }

  public String getContentType() {
    return contentType;
  }

  public ExpectedResponse setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  public String getContentEncoding() {
    return contentEncoding;
  }

  public ExpectedResponse setContentEncoding(String contentEncoding) {
    this.contentEncoding = contentEncoding;
    return this;
  }

  public String getContent() {
    return content;
  }

  public ExpectedResponse setContent(String content) {
    this.content = content;
    return this;
  }

  public String[] getHeadersPartialList() {
    return headersPartialList;
  }

  public ExpectedResponse setHeadersPartialList(String[] headersPartialList) {
    this.headersPartialList = headersPartialList;
    return this;
  }

  public String[] getHeadersFullList() {
    return headersFullList;
  }

  public ExpectedResponse setHeadersFullList(String[] headersFullList) {
    this.headersFullList = headersFullList;
    return this;
  }
}

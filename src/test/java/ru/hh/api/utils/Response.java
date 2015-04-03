package ru.hh.api.utils;

/**
 * Container for response attributes
 */
public class Response {
  private String statusLine;
  private long contentLength;
  private String contentType;
  private String contentEncoding;
  private String content;
  private String[] headersList;

  public String getStatusLine() {
    return statusLine;
  }

  public Response setStatusLine(String statusLine) {
    this.statusLine = statusLine;
    return this;
  }

  public long getContentLength() {
    return contentLength;
  }

  public Response setContentLength(long contentLength) {
    this.contentLength = contentLength;
    return this;
  }

  public String getContentType() {
    return contentType;
  }

  public Response setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  public String getContentEncoding() {
    return contentEncoding;
  }

  public Response setContentEncoding(String contentEncoding) {
    this.contentEncoding = contentEncoding;
    return this;
  }

  public String getContent() {
    return content;
  }

  public Response setContent(String content) {
    this.content = content;
    return this;
  }

  public String[] getHeadersList() {
    return headersList;
  }

  public Response setHeadersList(String[] headersList) {
    this.headersList = headersList;
    return this;
  }
}

package ru.hh.api.utils;

/**
 * Utility class for producing response containers to
 * represent expected and actual responses.
 * All methods are static.
 */
public final class ResponseFactory {
  private final static String HEADER_ACCESS_CONTROL_EXPOSE_HEADERS =
      "Access-Control-Expose-Headers: Location, ETag, Date, Expires," +
      " Cache-Control, Content-Type, X-Request-ID";
  private final static String HEADER_CONNECTION = "Connection: keep-alive";
  private final static String HEADER_KEEP_ALIVE = "Keep-Alive: timeout=60";
  private final static String HEADER_CACHE_CONTROL =
      "Cache-Control: max-age=0, private, must-revalidate";
  private final static String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN =
      "Access-Control-Allow-Origin: *";

  private final static String[] EXPECTED_HEADERS_ON_GET = new String[]{
      "Content-Type: application/json; charset=UTF-8",
      HEADER_CONNECTION,
      HEADER_KEEP_ALIVE,
      HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
      HEADER_CACHE_CONTROL,
      HEADER_ACCESS_CONTROL_ALLOW_ORIGIN
  };
  private final static String[] EXPECTED_HEADERS_ON_POST = new String[]{
      "Content-Length: 0",
      HEADER_CONNECTION,
      HEADER_KEEP_ALIVE,
      HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
      HEADER_CACHE_CONTROL,
      HEADER_ACCESS_CONTROL_ALLOW_ORIGIN
  };

  // private constructor prevents instantiation
  public ResponseFactory() {
  }

  public final static Response getActual() {
    return new Response();
  }

  public final static Response getExpected() {
    return new Response()
        .setStatusLine("HTTP/1.1 200 OK")
        .setHeadersList(EXPECTED_HEADERS_ON_GET);
  }

  public final static Response getExpectedOnPost() {
    return new Response()
        .setStatusLine("HTTP/1.1 204 No Content")
        .setContent("null")
        .setHeadersList(EXPECTED_HEADERS_ON_POST);
  }
}

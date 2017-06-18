package ru.hh.api.utils;

/**
 * Utility class for producing response containers to
 * represent expected and actual responses.
 * All methods are static.
 */
public final class ResponseFactory {
  public final static String HEADER_ACCESS_CONTROL_EXPOSE_HEADERS =
      "Access-Control-Expose-Headers: Location, ETag, Date, Expires," +
      " Cache-Control, Content-Type, X-Request-ID";
  public final static String HEADER_CONNECTION = "Connection: keep-alive";
  public final static String HEADER_KEEP_ALIVE = "Keep-Alive: timeout=60";
  public final static String HEADER_CACHE_CONTROL =
      "Cache-Control: max-age=0, private, must-revalidate";
  public final static String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN =
      "Access-Control-Allow-Origin: *";

  public final static String[] EXPECTED_HEADERS_ON_GET = new String[]{
      "Content-Type: application/json; charset=UTF-8",
//      HEADER_CONNECTION,
//      HEADER_KEEP_ALIVE,
      HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
      HEADER_CACHE_CONTROL,
      HEADER_ACCESS_CONTROL_ALLOW_ORIGIN
  };

  public final static String[] EXPECTED_HEADERS_ON_POST = new String[]{
      "Content-Length: 0",
//      HEADER_CONNECTION,
//      HEADER_KEEP_ALIVE,
      HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
      HEADER_CACHE_CONTROL,
      HEADER_ACCESS_CONTROL_ALLOW_ORIGIN
  };

  public final static String[] EXPECTED_BAD_REQUEST_HEADERS_ON_POST =
      new String[]{
//      HEADER_CONNECTION,
//      HEADER_KEEP_ALIVE,
      HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
      HEADER_ACCESS_CONTROL_ALLOW_ORIGIN
  };

  public final static String[] EXPECTED_BAD_GATEWAY_HEADERS_ON_POST =
      new String[]{
//      HEADER_CONNECTION,
//      HEADER_KEEP_ALIVE,
  };

  // private constructor prevents instantiation
  private ResponseFactory() {
  }

  public static Response getNewEmpty() {
    return new Response();
  }

  public static Response getNewExpected() {
    return new Response()
        .setStatusLine("HTTP/1.1 200 OK")
        .setHeadersList(EXPECTED_HEADERS_ON_GET);
  }

  public static Response getNewExpectedOnPost() {
    return new Response()
        .setStatusLine("HTTP/1.1 204 No Content")
        .setContent("null")
        .setHeadersList(EXPECTED_HEADERS_ON_POST);
  }

  public static Response getNewExpectedBadRequestOnInvalidPost() {
    return new Response()
        .setStatusLine("HTTP/1.1 400 Bad Request")
        .setHeadersList(EXPECTED_BAD_REQUEST_HEADERS_ON_POST);
  }

  public static Response getNewExpectedBadGatewayOnInvalidPost() {
    return new Response()
        .setStatusLine("HTTP/1.1 502 Bad Gateway")
        .setHeadersList(EXPECTED_BAD_GATEWAY_HEADERS_ON_POST);
  }
}

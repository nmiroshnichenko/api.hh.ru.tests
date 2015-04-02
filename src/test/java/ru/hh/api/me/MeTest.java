package ru.hh.api.me;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import ru.hh.api.tests.common.BaseTest;
import ru.hh.api.utils.ExpectedResponse;
import ru.hh.api.utils.Files;

public class MeTest extends BaseTest {
  private final static String URL = "https://api.hh.ru/me";
  private final static String EXPECTED_CONTENT_DEFAULT = new Files()
      .readFile("expected-responses/me/normal-default.json");

  @Test
  public synchronized void get() {
    verifyGet(URL, new ExpectedResponse()
        .setContent(EXPECTED_CONTENT_DEFAULT));
  }

  @Test
  public synchronized void post() {
    verifyPost(URL,
        "is_in_search=false",
        new ExpectedResponse()
            .setStatusLine("HTTP/1.1 204 No Content")
            .setContent(null));
  }

  @Test
  public synchronized void post2() {
    verifyPost(URL,
        "last_name=Иванов&first_name=Иван&middle_name=Иванович",
        new ExpectedResponse()
            .setStatusLine("HTTP/1.1 204 No Content")
            .setContent(null));
  }

  @AfterTest(description = "Clean up: change back the initial data" +
      " that was modified during the test")
  public void tearDown() throws Exception {
     verifyPost(URL,
        "last_name=apl001-surname&first_name=apl001-name&middle_name=",
        new ExpectedResponse()
            .setStatusLine("HTTP/1.1 204 No Content")
            .setContent(null));

    verifyPost(URL,
        "is_in_search=true",
        new ExpectedResponse()
            .setStatusLine("HTTP/1.1 204 No Content")
            .setContent(null));

    verifyGet(URL, new ExpectedResponse()
        .setContent(EXPECTED_CONTENT_DEFAULT));
  }
}
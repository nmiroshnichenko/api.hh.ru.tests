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
    verifyPost(URL, "is_in_search=true",
        new ExpectedResponse()
            .setStatusLine("HTTP/1.1 204 No Content")
            .setContent(null));
  }

  @AfterTest
  public void tearDown() throws Exception {
// we should change back the initial data
//    TODO post

  }
}
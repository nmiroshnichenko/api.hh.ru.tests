package ru.hh.api.tests.common;

import org.apache.http.client.fluent.Request;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.hh.api.utils.HttpClient;
import ru.hh.api.utils.Response;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@Test(singleThreaded = true)
public abstract class BaseTest {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  protected final HttpClient httpClient = new HttpClient();
  private String failures = "";

  protected void verifyRequest(
      final Request request,
      final Response expectedResponse,
      final boolean partial) {

    // send request and get actual response from server
    Response actualResponse = httpClient.send(request);

    // check status line
    verifyThat("Response status line as expected",
        actualResponse.getStatusLine(),
        equalTo(expectedResponse.getStatusLine()));

    // check response content (comparing as json)
    if (partial == true) {
      verifyThat("Response partial json content as expected",
          actualResponse.getContent(),
          jsonEquals(expectedResponse.getContent())
              .when(IGNORING_EXTRA_FIELDS));
    } else {
      verifyThat("Response json content as expected",
          actualResponse.getContent(),
          jsonEquals(expectedResponse.getContent()));
    }

    // check response headers
    String actualHeaders = String.join("; ",
        actualResponse.getHeadersList());
    for (String expectedHeader : expectedResponse.getHeadersList()) {
      verifyThat("Response headers as expected",
          actualHeaders,
          containsString(expectedHeader));
    }

    //TODO add checks for expected headers without checking their values (e.g. request-id)

    checkFailures();
  }

  protected void verifyGet(
      String url,
      Response expectedResponse) {

    verifyRequest(httpClient.get(url), expectedResponse, true);
  }

  protected void verifyGetFull(
      String url,
      Response expectedResponse) {

    verifyRequest(httpClient.get(url), expectedResponse, false);
  }

  protected void verifyPost(
      String url,
      String body,
      Response expectedResponse) {

    verifyRequest(
        httpClient.post(url, body),
        expectedResponse, false);
  }

  protected <T> void verifyThat(
      String reason, T actual, Matcher<? super T> matcher) {
    try {
      assertThat(reason, actual, matcher);
    } catch (AssertionError e) {
      logFailure(e);
    }
  }

  protected void verifyThat(String reason, boolean assertion) {
    try {
      assertThat(reason, assertion);
    } catch (AssertionError e) {
      logFailure(e);
    }
  }

  protected void checkFailures() {
    if (!failures.isEmpty()) {
      String previousFailures = failures;
      failures = ""; // clean up previous failures container
      Assert.fail("Those verifications failed (see above in the log):\n"
          + previousFailures);
    }
  }

  private void logFailure(AssertionError e) {
    String msg = e.toString();
    failures += "\n" + msg;
    log.error(msg);
  }
}

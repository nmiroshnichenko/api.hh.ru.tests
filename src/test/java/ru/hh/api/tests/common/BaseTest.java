package ru.hh.api.tests.common;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.hamcrest.Matcher;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.hh.api.utils.ActualResponse;
import ru.hh.api.utils.ExpectedResponse;
import ru.hh.api.utils.HttpClient;
import ru.hh.api.utils.Log;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Test(singleThreaded = true)
public abstract class BaseTest extends Log {
  protected final HttpClient httpClient = new HttpClient();
  public String failures = "";

  protected synchronized void verifyRequest(
      Request request,
      ExpectedResponse expectedResponse) {

    // send request and get actual response from server
    ActualResponse actualResponse = httpClient.send(request);

    // check status line
    verifyThat("Response status line as expected",
        actualResponse.getStatusLine(),
        equalTo(expectedResponse.getStatusLine()));
/*
    // check response content
    verifyThat("Response body as expected",
        actualResponse.getContent(),
        equalTo(expectedResponse.getContent()));
*/

    // check response content (comparing as json)
    verifyThat("Response body as expected",
        actualResponse.getContent(),
        jsonEquals(expectedResponse.getContent()));

    //TODO add checks for all possible opts in ExpectedResponse

    checkFailures();
  }

  protected synchronized void verifyGet(
      String url,
      ExpectedResponse expectedResponse) {

    verifyRequest(httpClient.get(url), expectedResponse);
  }

  protected synchronized void verifyPost(
      String url,
      String body,
      ContentType contentType,
      ExpectedResponse expectedResponse) {

    verifyRequest(
        httpClient.post(url).bodyString(body, contentType),
        expectedResponse);
  }

  protected synchronized void verifyPost(
      String url,
      String body,
      ExpectedResponse expectedResponse) {

    verifyPost(
        url,
        body,
        ContentType.APPLICATION_FORM_URLENCODED,
        expectedResponse);
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
    error(msg);
  }
}

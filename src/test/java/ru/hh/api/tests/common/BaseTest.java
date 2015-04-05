package ru.hh.api.tests.common;

import net.javacrumbs.jsonunit.ConfigurableJsonMatcher;
import org.apache.http.client.fluent.Request;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import ru.hh.api.utils.HttpClient;
import ru.hh.api.utils.Response;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_EXTRA_FIELDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public abstract class BaseTest {
  protected final Logger log = LoggerFactory.getLogger(this.getClass());
  protected final HttpClient httpClient = new HttpClient();
  protected final boolean asJson = true;
  protected final boolean asIs = false;
  protected final boolean partialCompare = true;
  protected final boolean fullCompare = false;
  private String failures = "";

  protected void verifyRequest(
      final Request request,
      final Response expectedResponse,
      final boolean expectedJsonResponse,
      final boolean partial) {

    final ConfigurableJsonMatcher<Object> partialJsonMather =
        jsonEquals(expectedResponse.getContent())
            .when(IGNORING_EXTRA_FIELDS);
    final ConfigurableJsonMatcher<Object> fullJsonMather =
        jsonEquals(expectedResponse.getContent());
    ConfigurableJsonMatcher<Object> matcher;
    String reason;

    // send request and get actual response from server
    Response actualResponse = httpClient.send(request);

    // check status line
    verifyThat("Response status line as expected",
        actualResponse.getStatusLine(),
        equalTo(expectedResponse.getStatusLine()));

    // check response content
    if (expectedJsonResponse) { // (comparing as json)
      if (partial) {
        verifyThat("Response partial json content as expected",
            actualResponse.getContent(),
            jsonEquals(expectedResponse.getContent())
                .when(IGNORING_EXTRA_FIELDS));
      } else {
        verifyThat("Response full json content as expected",
            actualResponse.getContent(),
            jsonEquals(expectedResponse.getContent()));
      }
    } else { // (comparing as is by Java)
      verifyThat("Response full content as expected",
          actualResponse.getContent(),
          equalTo(expectedResponse.getContent()));
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

  protected void verifyGetAsJson(
      String url,
      Response expectedResponse) {

    verifyRequest(
        httpClient.get(url), expectedResponse, asJson, partialCompare);
  }

  protected void verifyGetFullAsJson(
      String url,
      Response expectedResponse) {

    verifyRequest(
        httpClient.get(url), expectedResponse, asJson, fullCompare);
  }

  protected void verifyPostFullAsJson(
      String url,
      String body,
      Response expectedResponse) {

    verifyRequest(
        httpClient.post(url, body), expectedResponse, asJson, fullCompare);
  }

  protected void verifyPostFull(
      String url,
      String body,
      Response expectedResponse) {

    verifyRequest(
        httpClient.post(url, body), expectedResponse, asIs, fullCompare);
  }

  protected <T> void verifyThat(
      String reason, T actual, Matcher<? super T> matcher) {
    try {
      assertThat(reason, actual, matcher);
    } catch (AssertionError e) {
      logFailure(e);
    } catch (IllegalArgumentException e) {
      logFailure("Could not parse response body as json: " + e.getMessage());
    }
  }

  protected void verifyThat(String reason, boolean assertion) {
    try {
      assertThat(reason, assertion);
    } catch (AssertionError e) {
      logFailure(e);
    }
  }

  private void logFailure(Throwable e) {
    logFailure(e.getMessage());
  }

  private void logFailure(String msg) {
    failures += "\n" + msg;
    log.error(msg);
  }

  protected void checkFailures() {
    if (!failures.isEmpty()) {
      String previousFailures = failures;
      failures = ""; // clean up previous failures container
      Assert.fail("Those verifications failed (see above in the log):\n"
          + previousFailures);
    }
  }

  protected void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  protected String[] quoteNotNulls(String[] values) {
    String[] quotedValues = new String[values.length];
    for (int i = 0; i < values.length; i++) {
      if (values[i] != null) { // enclose value in quotes
        quotedValues[i] = String.format("\"%s\"", values[i]);
      } else { // return null as is
        quotedValues[i] = values[i];
      }
    }
    return quotedValues;
  }

  protected String[] nullsToEmptyStrings(String[] values) {
    String[] resultValues = new String[values.length];
    for (int i = 0; i < values.length; i++) {
      if (values[i] != null) { // return value as is
        resultValues[i] = values[i];
      } else { // return null as empty string
        resultValues[i] = "";
      }
    }
    return resultValues;
  }
}

package ru.hh.api.me;

import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.hh.api.tests.common.BaseTest;
import ru.hh.api.utils.FileUtils;
import ru.hh.api.utils.ResponseFactory;

import java.math.BigInteger;

public class MeTest extends BaseTest {
  private final static String URL = "https://api.hh.ru/me";
  private final static String EXPECTED_CONTENT_DEFAULT = new FileUtils()
      .readFile("MeTest/expected-content-default.json");
  private final static String THE_LONGEST_FULLNAME = new FileUtils()
      .readFile("MeTest/the-longest-known-real-fullname.txt");
  private final static int MAX_VALID_LAST_NAME_SIZE = 512;
  private final static int MAX_VALID_FIRST_NAME_SIZE = 256;
  private final static int MAX_VALID_MIDDLE_NAME_SIZE = 256;

  @Test(description = "check positive requests")
  public void postIsInSearch() {
    verifyIsInSearch("is_in_search=false", false);

    verifyIsInSearch("is_in_search=true", true);

    verifyIsInSearch("is_in_search=True", true);
  }

  @DataProvider(name = "postIncorrectIsInSearch", parallel = true)
  public Object[][] createData1() {
    BigInteger googol = new BigInteger("10").pow(100);
    return new Object[][] {
        { "is_in_search=null", false },
        { "is_in_search=", false },
        { "is_in_search", false },
        { "is_in_search=1", true }, //    surprisingly true
        { "is_in_search=1+0", false },
        { "is_in_search=0.5*2", false },
        { "is_in_search=\"true\"\"", false },
        { "is_in_search=0", false },
        { "is_in_search=-1", false },
        { "is_in_search=" + googol, false },
        { "is_in_search=" + Math.pow(10, 100), false },
        { "is_in_search=None", false },
        { "is_in_search=Not None", false },
        { "is_in_search=yes", false },
        { "is_in_search=TRUE", false },
        { "is_in_search=да", false },
    };
  }

  @Test(description = "request without valid field value",
      dataProvider = "postIncorrectIsInSearch")
  public void postIncorrectIsInSearch(String requestBody,
                                      Object expectedValue) {
    verifyIsInSearch(requestBody, expectedValue);
  }

  @Test(description = "request without all fields")
  public void postInvalidWithEmptyBody() {
    String requestBody = "";

    String expectedBody = "{\"errors\":" +
        " [{\"type\": \"bad_request\"}]," +
        " \"description\": \"No parameters\"}";

    verifyInvalidPostMe(requestBody, expectedBody);
  }

  @Test
  public void postNormalFullName() {
    verifyPostMeFullName("Ivanov", "Ivan", "Ivanovich");
    verifyPostMeFullName("Иванов", "Иван", "Иванович");
  }

  @Test
  public void postMixedFullName() {
    verifyPostMeFullName("Ivanov", "Иван", "Ivanovich");
    verifyPostMeFullName("Иванов", "Ivan", "Иванович");
    verifyPostMeFullName("Иванов", "Иван", "Ivanovich");
  }

  @Test
  public void postMinimalFullName() {
    verifyPostMeFullName("a", "b", "c");
    verifyPostMeFullName("x", "x", "x");
    verifyPostMeFullName("э", "ю", "я");
    verifyPostMeFullName("1", "1", "1");
    verifyPostMeFullName("0", "0", "0");
  }

  @Test(description = "")
  public void postFullNameWithoutMiddleName() {
    // Here we actually send value "null" but not empty value
    verifyPostMeFullName("Иванов", "Иван", "null");

    /*
    Now without value at all: "middle_name="
    Note there are must be values with null without quotes in response.
    */
    verifyPostMeFullName("Иванов", "Иван", null);
  }

  @Test
  public void postEmptyFullName() {
    // Here we actually send values "null" but not empty values
    verifyPostMeFullName("null", "null", "null");

    /*
    Now without values at all.
    Note there are must be values with null without quotes in response.
    */
    verifyPostMeFullName(null, null, null);
  }

  @Test
  public void postInvalidFullName() {
    String ln = "ln";
    String fn = "fn";
    String mn = "mn";
    String expectedBody;
    String requestBody;

    log.info("request without field 'first_name'");
    requestBody = String.format(
        "last_name=%s&middle_name=%s",
        ln, mn);

    expectedBody = "{\"errors\":" +
      "[{\"type\":\"bad_argument\",\"value\":\"first_name\"}]," +
      "\"bad_arguments\":" +
      "[{\"name\":\"first_name\"," +
      "\"description\":\"Required parameter\"}]," +
      "\"description\":\"Required parameter\"," +
      "\"bad_argument\":\"first_name\"}";

    verifyInvalidPostMe(requestBody, expectedBody);

    log.info("request without field 'last_name'");
    requestBody = String.format(
        "first_name=%s&middle_name=%s",
        fn, mn);

    expectedBody = "{\"errors\":" +
        "[{\"type\":\"bad_argument\",\"value\":\"last_name\"}]," +
        "\"bad_arguments\":" +
        "[{\"name\":\"last_name\"," +
        "\"description\":\"Required parameter\"}]," +
        "\"description\":\"Required parameter\"," +
        "\"bad_argument\":\"last_name\"}";

    verifyInvalidPostMe(requestBody, expectedBody);

    log.info("request without field 'middle_name'");
    requestBody = String.format(
        "last_name=%s&first_name=%s",
        ln, fn);

    expectedBody = "{\"errors\":" +
        "[{\"type\":\"bad_argument\",\"value\":\"middle_name\"}]," +
        "\"bad_arguments\":" +
        "[{\"name\":\"middle_name\"," +
        "\"description\":\"Required parameter\"}]," +
        "\"description\":\"Required parameter\"," +
        "\"bad_argument\":\"middle_name\"}";

    verifyInvalidPostMe(requestBody, expectedBody);

    log.info("request without fields 'last_name' and 'middle_name'");
    requestBody = String.format(
        "first_name=%s", fn);

    expectedBody = "{\"errors\":" +
        " [{\"type\": \"bad_argument\", \"value\": \"last_name\"}," +
        " {\"type\": \"bad_argument\", \"value\": \"middle_name\"}]," +
        " \"bad_arguments\":" +
        " [{\"name\": \"last_name\"," +
        " \"description\": \"Required parameter\"}," +
        " {\"name\": \"middle_name\"," +
        " \"description\": \"Required parameter\"}]," +
        " \"description\": \"Required parameter\"," +
        " \"bad_argument\": \"last_name\"}";

    verifyInvalidPostMe(requestBody, expectedBody);
  }

  @Test(description = "Change full name with max valid number of symbols." +
      "\nLimits: max-lastname = " + MAX_VALID_LAST_NAME_SIZE
      + ", max-firstname = " + MAX_VALID_FIRST_NAME_SIZE
      + " max-middlename = " + MAX_VALID_MIDDLE_NAME_SIZE)
  public void postBigFullName() {
    String bigLastName = THE_LONGEST_FULLNAME
        .substring(0, MAX_VALID_LAST_NAME_SIZE);

    String bigFirstName = THE_LONGEST_FULLNAME
        .substring(0, MAX_VALID_FIRST_NAME_SIZE);

    String bigMiddleName = THE_LONGEST_FULLNAME
        .substring(0, MAX_VALID_MIDDLE_NAME_SIZE);

    verifyPostMeFullName(bigLastName, null, null);
    verifyPostMeFullName(null, bigFirstName, null);
    verifyPostMeFullName(null, null, bigMiddleName);

    verifyPostMeFullName(bigLastName, bigFirstName, null);
    verifyPostMeFullName(bigLastName, null, bigMiddleName);
    verifyPostMeFullName(null, bigFirstName, bigMiddleName);
    verifyPostMeFullName(bigLastName, bigFirstName, bigMiddleName);
  }

  @Test(description =
      "Try to change full name with over max valid number of symbols." +
          "\nLimits: max-lastname = " + MAX_VALID_LAST_NAME_SIZE
          + ", max-firstname = " + MAX_VALID_FIRST_NAME_SIZE
          + " max-middlename = " + MAX_VALID_MIDDLE_NAME_SIZE)
  public void postExtraBigFullName() {
    String extraBigLastName = THE_LONGEST_FULLNAME
        .substring(0, MAX_VALID_LAST_NAME_SIZE + 1);

    String extraBigFirstName = THE_LONGEST_FULLNAME
        .substring(0, MAX_VALID_FIRST_NAME_SIZE + 1);

    String extraBigMiddleName = THE_LONGEST_FULLNAME
        .substring(0, MAX_VALID_MIDDLE_NAME_SIZE + 1);

    verifyInvalidPostMeFullName(extraBigLastName, null, null);
    verifyInvalidPostMeFullName(null, extraBigFirstName, null);
    verifyInvalidPostMeFullName(null, null, extraBigMiddleName);

    verifyInvalidPostMeFullName(extraBigLastName, extraBigFirstName, null);
    verifyInvalidPostMeFullName(extraBigLastName, null, extraBigMiddleName);
    verifyInvalidPostMeFullName(null, extraBigFirstName, extraBigMiddleName);
    verifyInvalidPostMeFullName(
        extraBigLastName, extraBigFirstName, extraBigMiddleName);
  }

  @AfterTest(description = "Clean up: change back the initial data" +
      " that was modified during the test")
    public void tearDown() {
    postDefaultFullName();
    verifyPostMe("is_in_search=true");

    // here we check response json content as a whole
    verifyGetFullAsJson(URL, ResponseFactory.getNewExpected()
        .setContent(EXPECTED_CONTENT_DEFAULT));
  }

  /**
   * Method is synchronized because it makes 2 bound changes:
   * firstly it change a given value to the opposite
   * and then to an expected (those changes come with verifications).
   * And no other thread (and even test) making a similar change
   * should come between those changes.
   *
   * @param requestBody
   * @param expectedValue
   */
  private synchronized void verifyIsInSearch(
      String requestBody,
      Object expectedValue) {
    /* We should firstly set the opposite value due to check
    if expected value change actually is made by us */
    boolean oppositeValue;
    if (expectedValue instanceof String) {
      oppositeValue = !Boolean.parseBoolean((String) expectedValue);
    } else {
      oppositeValue = !(Boolean) expectedValue;
    }

    // Experimentally got value to wait before get after post a new value
    // due to not getting an old value.
    long timeToWaitChanges = 150;

    verifyPostMe(String.format("is_in_search=%s", oppositeValue));
    sleep(timeToWaitChanges);
    verifyGetMe(String.format("{\"is_in_search\": %s}",
        oppositeValue));

    verifyPostMe(requestBody);
    sleep(timeToWaitChanges);
    verifyGetMe(String.format("{\"is_in_search\": %s}",
        expectedValue));
  }

  /**
   * @see #verifyIsInSearch the same cause why this method is synchronised
   * @param lastName
   * @param firstName
   * @param middleName
   */
  private synchronized void verifyPostMeFullName(
      String lastName, String firstName, String middleName) {

    verifyPostMe(
        String.format("last_name=%s&first_name=%s&middle_name=%s",
        nullsToEmptyStrings(new String[]{lastName, firstName, middleName})));

    String[] quotedValues = quoteNotNulls(
        new String[]{lastName, firstName, middleName, middleName});

    verifyGetMe(String.format(
        "{\"last_name\": %s," +
            "\"first_name\": %s," +
            "\"middle_name\": %s," +
            "\"mid_name\": %s}",
        quotedValues));
  }

  /**
   * @see #verifyIsInSearch the same cause why this method is synchronised
   * @param lastName
   * @param firstName
   * @param middleName
   */
  private void verifyInvalidPostMeFullName(
      String lastName, String firstName, String middleName) {

    verifyInvalidPostMeWithBadGateway(String.format(
            "last_name=%s&first_name=%s&middle_name=%s",
            lastName, firstName, middleName));
  }

  private void postDefaultFullName() {
    verifyPostMe(
        "last_name=test-applicant-ln" +
            "&first_name=test-applicant-fn" +
            "&middle_name=test-applicant-mn");
  }

  private void verifyInvalidPostMe(String requestBody,
                                   String expectedBody) {
    verifyPostFullAsJson(URL,
        requestBody,
        ResponseFactory
            .getNewExpectedBadRequestOnInvalidPost()
            .setContent(expectedBody));
  }

  private void verifyInvalidPostMeWithBadGateway(String requestBody) {
    String expectedBody = "<html>\r\n" +
        "<head><title>502 Bad Gateway</title></head>\r\n" +
        "<body bgcolor=\"white\">\r\n" +
        "<center><h1>502 Bad Gateway</h1></center>\r\n" +
        "<hr><center>nginx/1.7.10</center>\r\n" +
        "</body>\r\n" +
        "</html>\r\n";

    verifyPostFull(URL,
        requestBody,
        ResponseFactory
            .getNewExpectedBadGatewayOnInvalidPost()
            .setContent(expectedBody));
  }

  private void verifyPostMe(String requestBody) {
    verifyPostFullAsJson(URL, requestBody, ResponseFactory.getNewExpectedOnPost());
  }

  private void verifyGetMe(String expectedJsonPart) {
    verifyGetAsJson(URL, ResponseFactory.getNewExpected()
        .setContent(expectedJsonPart));
  }
}
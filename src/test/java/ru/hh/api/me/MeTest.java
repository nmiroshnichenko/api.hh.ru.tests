package ru.hh.api.me;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import ru.hh.api.tests.common.BaseTest;
import ru.hh.api.utils.FileUtils;
import ru.hh.api.utils.ResponseFactory;

import java.io.IOException;
import java.net.URISyntaxException;

public class MeTest extends BaseTest {
  private final static String URL = "https://api.hh.ru/me";
  private final String EXPECTED_CONTENT_DEFAULT;

  public MeTest() throws IOException, URISyntaxException {
    EXPECTED_CONTENT_DEFAULT = new FileUtils()
        .readFile("expected-responses/me/normal-default.json");
  }

  @Test
  public void get() {
    verifyGet(URL, ResponseFactory.getExpected()
        .setContent(EXPECTED_CONTENT_DEFAULT));
  }

  @Test
  public void postNotInSearch() {
    /*we should firstly set the opposite value due to check
    if expected value change actually is made by us */
    postDefaultIsInSearch();
    verifyPostMe("is_in_search=false");
    verifyGetMe("{\"is_in_search\": false}");
  }

  @Test
  public void postCyrillicFullName() {
    postDefaultFullName();
    verifyPostMeFullName("Иванов", "Иван", "Иванович");
  }

  @Test
  public void postCyrillicFullNameWithoutMiddleName() {
    postDefaultFullName();
    verifyPostMeFullName("Иванов", "Иван", null);
  }

  @Test
  public void postMixedFullName() {
    postDefaultFullName();
    verifyPostMeFullName("Ivanov", "Иван", "Ivanovich");
    verifyPostMeFullName("Иванов", "Ivan", "Иванович");
  }

  @Test
  public void postMinimalFullName() {
    postDefaultFullName();
    verifyPostMeFullName("э", "ю", "я");
  }

  @Test
  public void postBigFullName() {
    postDefaultFullName();

//    https://en.wikipedia.org/wiki/Wolfe%2B585,_Senior
    String bigFirstName = "Adolph Blaine Charles David\n" +
        "Earl Frederick Gerald Hubert\n" +
        "Irvin John Kenneth Lloyd Martin\n" +
        "Nero Oliver Paul Quincy\n" +
        "Randolph Sherman Thomas\n" +
        "Uncas Victor William Xerxes\n" +
        "Yancy Zeus";
//    512 symbols
    String bigLastName = "Wolfeschlegelsteinhausenbergerdorffvoralternwaren" +
        "gewissenhaftschaferswessenschafewarenwohlgepflege" +
        "undsorgfaltigkeitbeschutzenvonangreifendurchihrraubgierigfeinde" +
        "welchevoralternzwolftausendjahresvorandieerscheinenwandererste" +
        "erdemenschderraumschiffgebrauchlichtalsseinursprungvonkraftgestart" +
        "sein­lange­fahrt­hinzwischen­sternartigraum­auf­der­suche­nach­die­" +
        "stern­welche­gehabt­bewohnbar­planeten­kreise­drehen­sich­und­wohin" +
        "­der­neu­rasse­von­verstandigmenschlichkeitkonnte­fortplanzen­und­" +
        "sicher­freuen­an­lebens";
//        "langlich­freude­und­ruhe­mit­nicht­ein­furcht­vor­angreifen­" +
//            "von­anderer­intelligent­geschopfs­­von­hin­zwischen­" +
//            "sternartig­raum, Senior";

    verifyPostMeFullName(bigLastName, bigFirstName, null);
  }

  @Test(enabled = false)
  public void postExceptionalBigFullName() {
    postDefaultFullName();
    String bigFirstName = "Adolph Blaine Charles David\n" +
        "Earl Frederick Gerald Hubert\n" +
        "Irvin John Kenneth Lloyd Martin\n" +
        "Nero Oliver Paul Quincy\n" +
        "Randolph Sherman Thomas\n" +
        "Uncas Victor William Xerxes\n" +
        "Yancy Zeus";
    String bigLastName = "Wolfeschlegelsteinhaus-\n" +
        "enbergerdorffwelchevoralt-\n" +
        "ernwareengewissenhaftscha-\n" +
        "ferswessenschafewarenwoh-\n" +
        "lgepflegeundsorgfaltigkeitbe-\n" +
        "schutzenvonangreifendurchi-\n" +
        "hraubgierigfeindewelchevo-\n" +
        "ralternzwolfhunderttausend-\n" +
        "jahresvorandieerschweinen-\n" +
        "vonderersteerdemenschder-\n" +
        "raumschiffgenachtmittungs-\n" +
        "teinundsiebeniridiumelktris-\n" +
        "chmotorsgebrauchlichtalsse-\n" +
        "inursprungvonkraftgstarts-\n" +
        "einlangefahrthinzwischenst-\n" +
        "ernartigraumaufdersuchen-\n" +
        "nachbarschaftdersternwelch-\n" +
        "chegehabtbewohnbarplante-\n" +
        "tenkreisedrehensichundwoh-\n" +
        "inderneuerassevonverstandi-\n" +
        "gmenschlichkeitkonntefort-\n" +
        "pflanzenundsicherfreuenanl-\n" +
        "ebenslanglichfreudeundruhe-\n" +
        "mitnichteinfurthtvorangreif-\n" +
        "envorandereintelligentges-\n" +
        "chapfsvonhinzwischenstern-\n" +
        "artigraum, Senior";

    verifyPostMeFullName(bigLastName, bigFirstName, null);
  }

  @AfterTest(description = "Clean up: change back the initial data" +
      " that was modified during the test")
    public void tearDown() throws Exception {
    postDefaultFullName();
    postDefaultIsInSearch();

    // here we check response json content as a whole
    verifyGetFull(URL, ResponseFactory.getExpected()
        .setContent(EXPECTED_CONTENT_DEFAULT));
  }

  private void verifyPostMe(String body) {
    verifyPost(URL, body, ResponseFactory.getExpectedOnPost());
  }

  private void verifyGetMe(String expectedJsonPart) {
    verifyGet(URL, ResponseFactory.getExpected()
        .setContent(expectedJsonPart));
  }

  private void verifyPostMeFullName(String ln, String fn, String mn) {
    verifyPostMe(String.format(
        "last_name=%s&first_name=%s&middle_name=%s", ln, fn, mn));

    verifyGetMe(String.format(
        "{\"last_name\": \"%s\"," +
            "\"first_name\": \"%s\"," +
            "\"middle_name\": \"%s\"," +
            "\"mid_name\": \"%s\"}",
        ln, fn, mn, mn));
  }

  private void postDefaultFullName() {
    verifyPostMe(
        "last_name=test-applicant-ln" +
            "&first_name=test-applicant-fn" +
            "&middle_name=test-applicant-mn");
  }

  private void postDefaultIsInSearch() {
    verifyPostMe("is_in_search=true");
  }
}
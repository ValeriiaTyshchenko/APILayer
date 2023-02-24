import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class LiveEndpointsTests {
    private static Response response;


//      2.1  Get current currency conversion rates
    @Test
    public void liveEndpointJSONResponseTest() {
        response = given().get(Consts.URL+Consts.LIVE_ENDPOINT+Consts.TOKEN);
        System.out.println(response.asString());
        response.then().body("success", equalTo(true));
//        response.then().body("terms", containsString("https:"));
//        response.then().body("privacy", containsString("https:"));
        response.then().body("timestamp", notNullValue());
        response.then().body("source", equalTo("USD"));
        response.then().statusCode(200);
        response.then().body("quotes", notNullValue());
    }


//2.2 Optional parameter - currencies
// 2.2.1 Every currency could be retrieved to the client using the “currencies" parameter,
// In the request, we should be able to send one or several currencies divided by comma.
    @ParameterizedTest
    @ValueSource(strings = {"CAD", "EUR", "ILS", "RUB"})
    public void convertOneCurrencyTest(String currency) {
        response = given().get(Consts.URL+Consts.LIVE_ENDPOINT+Consts.TOKEN + Consts.CURRENCIES + currency);
        System.out.println(response.asString());
        response.then().body("success", equalTo(true));
        response.then().body("timestamp", notNullValue());
        response.then().body("source", equalTo("USD"));
        response.then().statusCode(200);
    }

    @Test
    public void convertSeveralCurrenciesTest() {
        String currencies = "CAD,EUR,ILS,RUB";
        response = given().get(Consts.URL+Consts.LIVE_ENDPOINT+Consts.TOKEN + Consts.CURRENCIES + currencies);
        System.out.println(response.asString());
        response.then().body("success", equalTo(true));
        response.then().body("timestamp", notNullValue());
        response.then().body("source", equalTo("USD"));
        response.then().statusCode(200);
    }


//2.2.3 Incorrect currency code should trigger a user-friendly error.
    @Test
    public void negativeTestInvalidCurrencyCode() {

        response = given().get(Consts.URL+Consts.LIVE_ENDPOINT+Consts.TOKEN + Consts.CURRENCIES + "BBC");
        System.out.println(response.asString());
        response.then().body("success", equalTo(false));
        response.then().statusCode(200);
        response.then().body("error.code", equalTo(202));
        response.then().body("error.info", containsString("invalid Currency Codes"));
    }

}

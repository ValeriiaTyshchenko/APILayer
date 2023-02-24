import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class HistoricalEndpointTest {
    private static Response response;

//    2.3 historical conversion according to date - verify the correct date of response.
    @Test
    public void historicalEndpointAuthorizationTest() {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "2022-02-23" +"&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("date", equalTo("2022-02-23"));
        response.then().body("timestamp",  equalTo(1645660799));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2021-02-22","2021-02-23", "2022-02-23", "2023-02-23"})
    public void historicalRatesWithDifferentDates(String date) {

        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT+ date + "&"+ Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("date", equalTo(date));
    }

//2.3.2. Optional parameter - currencies should be available for this endpoint  as well
    @Test
    public void convertCurrencyTestWithSpecificDate() {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "2022-02-23" +"&" + Consts.TOKEN + Consts.CURRENCIES + "CAD");
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("date", equalTo("2022-02-23"));
        response.then().body("timestamp",  equalTo(1645660799));
        response.then().body("source", equalTo("USD"));
        response.then().body("quotes.USDCAD", equalTo(1.273415f));

    }
//2.3.3 Historical rates should be available for the same currencies as before  (USDCAD, USDEUR, USDNIS, and USDRUB).
    @ParameterizedTest
    @ValueSource(strings = {"CAD", "EUR", "ILS", "RUB"})
    public void convertOneCurrencyHistoricalRateTest(String currency) {
        response = given().get(Consts.URL+Consts.HISTORICAL_ENDPOINT+ "2022-06-22" + "&"+Consts.TOKEN + Consts.CURRENCIES + currency);
        System.out.println(response.asString());
        response.then().body("success", equalTo(true));
        response.then().body("date", equalTo("2022-06-22"));
        response.then().body("timestamp", equalTo(1655942399));
        response.then().body("source", equalTo("USD"));
        response.then().statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2020-02-22","2021-02-23", "2022-02-23", "2023-02-23"})
    public void convertSeveralCurrenciesWithSeveralDates(String date) {
        String currencies = "CAD,EUR,ILS,RUB";
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT+ date + Consts.CURRENCIES +currencies + "&"+ Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        response.then().body("date", equalTo(date));
        response.then().body("quotes", notNullValue());
    }
//    Negative tests
//    (a user-friendly error should be returned if the "Date" parameter is incorrect or missing)
     @Test
     public void missingDateTest() {

         response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "&" + Consts.TOKEN);
         System.out.println(response.asString());
         response.then().statusCode(200);
         response.then().body("success", equalTo(false));
         response.then().body("error.code", equalTo(301));
         response.then().body("error.info", containsString("You have not specified a date"));
}
    @Test
    public void invalidDateTest() {

        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "2024-02-23" + "&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);
        response.then().body("success", equalTo(false));
        response.then().body("error.code", equalTo(302));
        response.then().body("error.info", equalTo("You have entered an invalid date. [Required format: date=YYYY-MM-DD]"));
    }

}

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AuthorizationTests {
    public static Response response;

    @Test
    public void liveEndpointAuthorizationTest(){
        response = given().get(Consts.URL+Consts.LIVE_ENDPOINT + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);


    }
    @Test
    public void historicalEndpointAuthorizationTest() {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "2022-02-23&" + Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);

    }
    @Test
    public void getResponseWithoutAccessKeyLiveEndpoint(){
        response = given().get(Consts.URL+"/live");
        System.out.println(response.asString());
        response.then().statusCode(404);
        response.then().body("message", equalTo("no Route matched with those values"));


    }
    @Test
    public void getResponseWithoutAccessKeyHistoricalEndpoint(){
        response = given().get(Consts.URL+Consts.HISTORICAL_ENDPOINT);
        System.out.println(response.asString());
        response.then().statusCode(401);
        response.then().body("message", equalTo("No API key found in request"));


    }
    @Test
    public void getResponseWithIncorrectAccessKey(){
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + "?apikey=lMard7YvAcgWRDqi0gLTnIE");
        System.out.println(response.asString());
        response.then().statusCode(401);
        response.then().body("message", equalTo("No API key found in request"));


    }
}

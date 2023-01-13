import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;


public class HWTest extends AbstractTest {

    @BeforeAll
    static void setUp(){

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

    @Test
    void getSearchRecipes() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("cuisine", "greek")
                .queryParam("diet", "primal")
                .queryParam("maxVitaminB1", "100")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .assertThat()
                .statusCode(200)
                .body("results[0].title", equalTo("Greek Side Salad"))
                .body("totalResults", is(4));
    }

    @Test
    void postClassifyCuisine() {
        String title = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("cuisine", "greek")
                .queryParam("diet", "primal")
                .queryParam("maxVitaminB1", "100")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .extract()
                .jsonPath()
                .get("results[0].title")
                .toString();
//        System.out.println(title);

        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", title)
                .formParam("language", "en")
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .assertThat()
                .statusCode(200)
                .body("cuisine", equalTo("Mediterranean"));
    }

}
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class GetLongRedirectTests {

    @Test
    public void redirectUrlTest() {
        Response response = given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        System.out.println(response.getHeader("Location"));
    }
}

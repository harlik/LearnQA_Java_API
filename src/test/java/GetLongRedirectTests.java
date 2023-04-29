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

    @Test
    public void countRedirectsTest() {
        Response response;
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int redirectsCounter = 0;
        do {
            response = given()
                    .redirects()
                    .follow(false)
                    .get(url)
                    .andReturn();
            url = response.getHeader("Location");
            redirectsCounter++;
            System.out.println(url);
        } while((response.getStatusCode() != 200) && (url != null));
        redirectsCounter--;
        System.out.printf("Number of redirects = %1$d\n", redirectsCounter);
    }
}

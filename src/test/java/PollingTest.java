import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static io.restassured.RestAssured.given;

public class PollingTest {

    @Test
    public void pollingTest() throws InterruptedException {
        String url = "https://playground.learnqa.ru/ajax/api/longtime_job";
        JsonPath response = given()
                .get(url)
                .jsonPath();
        String token = response.get("token");
        int seconds2Wait = response.get("seconds");
        response = given()
                .param("token", token)
                .get(url)
                .jsonPath();
        assertEquals("Job is NOT ready", response.get("status"));
        Thread.sleep(seconds2Wait * 1000L);
        final JsonPath finalResponse = given()
                .param("token", token)
                .get(url)
                .jsonPath();
        assertAll(
                () -> assertEquals("Job is ready", finalResponse.get("status")),
                () -> assertNotNull(finalResponse.get("result"), "result in null")
        );
    }
}

package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    public static void assertJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value isn't as expected.");
    }

    public static void assertResponseText(Response response, String expectedText) {
        assertEquals(
                expectedText,
                response.asString(),
                "Response text isn't as expected."
        );
    }

    public static void assertResponseCode(Response response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                response.getStatusCode(),
                "Response status code isn't as expected."
        );
    }
}

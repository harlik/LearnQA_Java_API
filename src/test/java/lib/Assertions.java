package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    public static <T> void assertJsonByName(Response response, String name, T expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        T value = response.jsonPath().get(name);
        assertEquals(expectedValue, value, "JSON value isn't as expected.");
    }

    public static void assertJsonHasField(Response response, String expectedFieldName) {
        assertJsonHasFields(response, new String[]{expectedFieldName});
    }

    public static void assertJsonNotHasField(Response response, String expectedFieldName) {
        response.then().assertThat().body("$", not(hasKey(expectedFieldName)));
    }

    public static void assertJsonHasFields(Response response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            response.then().assertThat().body("$", hasKey(expectedFieldName));
        }
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

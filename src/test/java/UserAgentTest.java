import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class UserAgentTest {

    @ParameterizedTest
    @MethodSource("stringIntAndListProvider")
    public void userAgentTest(String userAgentString, List<String> expectedValues) {
        JsonPath json = given()
                .header("user-agent", userAgentString)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn()
                .jsonPath();
        assertAll(
                "For user agent\n" + userAgentString,
                () -> assertEquals(expectedValues.get(0), json.get("platform"), "Wrong platform"),
                () -> assertEquals(expectedValues.get(1), json.get("browser"), "Wrong browser"),
                () -> assertEquals(expectedValues.get(2), json.get("device"), "Wrong device")
        );
    }

    static Stream<Arguments> stringIntAndListProvider() {
        return Stream.of(
                arguments("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                        Arrays.asList("Mobile", "No", "Android")),
                arguments("Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                        Arrays.asList("Mobile", "Chrome", "iOS")),
                arguments("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                        Arrays.asList("Googlebot", "Unknown", "Unknown")),
                arguments("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                        Arrays.asList("Web", "Chrome", "No")),
                arguments("Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
                        Arrays.asList("Mobile", "No", "iPhone"))
        );
    }

}

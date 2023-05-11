import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class CookieTest {
    @Test
    void CookieIsAsExpected() {
        String cookieName = "HomeWork";
        String cookieValue = get("https://playground.learnqa.ru/api/homework_cookie")
                .getCookie(cookieName);
        assertEquals("hw_value", cookieValue, "Cookie " + cookieName + "has wrong value");
    }

    @Test
    void CookiesAreAsExpected() {
        Map<String, String> cookies = get("https://playground.learnqa.ru/api/homework_cookie")
                .getCookies();
        Map<String, String> expectedCookies = Map.of("HomeWork", "hw_value");
        assertEquals(expectedCookies, cookies, "Cookie values are wrong.");
    }
}

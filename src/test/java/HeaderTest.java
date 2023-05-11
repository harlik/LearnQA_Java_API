import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class HeaderTest {
    @Test
    void HeaderIsAsExpected() {
        String headerName = "x-secret-homework-header";
        String headerValue = get("https://playground.learnqa.ru/api/homework_header")
                .header(headerName);
        assertEquals("Some secret value", headerValue, "Header " + headerName + "has wrong value");
    }
}

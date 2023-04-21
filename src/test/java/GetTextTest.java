import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

public class GetTextTest {
    @Test
    public void getText() {
        RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn()
                .prettyPrint();
    }

}

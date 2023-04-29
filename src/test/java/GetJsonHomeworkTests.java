import static io.restassured.RestAssured.*;
import org.junit.jupiter.api.Test;


public class GetJsonHomeworkTests {

    @Test
    public void secondMessageTest() {
        String message = given()
                .contentType("application/json")
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn()
                .jsonPath()
                .get("messages[1].message");
        System.out.println(message);
    }
}

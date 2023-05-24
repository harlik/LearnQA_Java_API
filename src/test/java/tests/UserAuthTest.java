package tests;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static lib.Assertions.assertJsonByName;
import static lib.api.CoreRequests.logIn;

public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    RequestSpecification authSpec = given();
    String authCookieName = "auth_sid";
    String authHeaderName = "x-csrf-token";
    String userIdFieldName = "user_id";

    @BeforeEach
    void loginUser() {
        Response responseGetAuth = logIn();

        cookie = getCookie(responseGetAuth, authCookieName);
        header = getHeader(responseGetAuth, authHeaderName);
        userIdOnAuth = getIntFromJson(responseGetAuth, userIdFieldName);

        authSpec.baseUri("https://playground.learnqa.ru/api/user/auth");

    }

    @Test
    void testAuthUser() {
        Response responseCheckAuth = authSpec
                .cookie(authCookieName, cookie)
                .header(authHeaderName, header)
                .get()
                .andReturn();
        assertJsonByName(responseCheckAuth, userIdFieldName, userIdOnAuth);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "header"})
    void testNegativeAuthUser(String condition) {
        if (condition.equals("cookie")) {
            authSpec.cookie(authCookieName, cookie);
        } else if (condition.equals("header")) {
            authSpec.header(authHeaderName, header);
        } else {
            throw new IllegalArgumentException("Condition is unknown " + condition);
        }
        Response responseCheckAuth = authSpec.get().andReturn();
        assertJsonByName(responseCheckAuth, userIdFieldName, 0);
    }
}
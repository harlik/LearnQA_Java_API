package lib.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class CoreRequests {
    static String authCookieName = "auth_sid";
    static String authHeaderName = "x-csrf-token";

    @Step("Send create user POST")
    static public Response createUser(Map<String, String> registrationData) {
        return given()
                .body(registrationData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
    }

    @Step("Send get user GET")
    static public Response getUser(int userId) {
        return get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
    }

    @Step("Send get user GET as authenticated user")
    static public Response getUser(int userId, Response loginResponse) {
        return getAuthedSpec(loginResponse)
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
    }

    static private RequestSpecification getAuthedSpec(Response loginResponse) {
        return given()
                .header(authHeaderName, loginResponse.getHeader(authHeaderName))
                .cookie(authCookieName, loginResponse.getCookie(authCookieName));
    }

    @Step("Send delete user DELETE as authenticated user")
    static public Response deleteUser(int userId, Response loginResponse) {
        return getAuthedSpec(loginResponse)
                .delete("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
    }

    @Step("Send log in  GET")
    static public Response logIn(String email, String password) {
        Map<String, String> authData = Map.of(
                "email", email,
                "password", password
        );

        return given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
    }

    @Step("Send log in  GET for user \"vinkotov@example.com\", password \"1234\"")
    static public Response logIn() {
        return logIn("vinkotov@example.com", "1234");
    }

}

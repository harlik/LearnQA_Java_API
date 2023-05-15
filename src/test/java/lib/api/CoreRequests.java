package lib.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class CoreRequests {
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

    static private RequestSpecification getAuthedSpec(String header, String cookie) {
        return given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie);
    }
    @Step("Send delete user DELETE")
    static public Response deleteUser(int userId, String authHeader, String authCookie) {
        return getAuthedSpec(authHeader, authCookie)
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

}

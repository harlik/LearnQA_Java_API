package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static lib.Assertions.*;
import static lib.DataGenerator.getRandomEmail;
import static lib.DataGenerator.getRegistrationData;
import static lib.api.CoreRequests.*;

@Epic("User CRUD")
@Feature("Create user")
public class CreateUserTests extends BaseTestCase {
    int userId;
    Map<String, String> registrationData;
    Response response;

    @AfterEach
    void clear() {
        try {
            int userId = response.jsonPath().getInt("id");
            Response loginResponse = logIn(registrationData.get("email"), registrationData.get("password"));
            String authCookieName = "auth_sid";
            String authHeaderName = "x-csrf-token";
            String cookie = getCookie(loginResponse, authCookieName);
            String header = getHeader(loginResponse, authHeaderName);
            deleteUser(userId, header, cookie);
            System.out.println("Deleted user having id = " + userId);
        }
        catch (JsonPathException e) {
            // do nothing - it's ok to have no user id for negative tests
            // although when a negative test fails, then it's a good idea to delete a created user
        }
    }

    @BeforeEach
    void clearUserId() {
        userId = 0;
    }

    @Test
    @Description("Creates a new user successfully.")
    @DisplayName("Successful creation")
    void createUserSuccessfully() {
        registrationData = getRegistrationData();
        response = createUser(registrationData);
        assertResponseCode(response, 200);
        userId = response.jsonPath().getInt("id");
        getUser(userId);
    }

    @Test
    @Description("Create a new user via email of the already existing user.")
    @DisplayName("Existing email")
    void createUserWithExistingEmail() {
        String existingEmail = "vinkotov@example.com";
        registrationData = getRegistrationData(Map.of("email", existingEmail));
        response = createUser(registrationData);
        assertResponseText(response, "Users with email '" + existingEmail + "' already exists");
        assertResponseCode(response, 400);
    }

    @Test
    @Description("Create a new user via incorrect email (no @ sign).")
    @DisplayName("Incorrect email")
    void createUserWithIncorrectEmail() {
        String noAtEmail = "vinkotovexample.com";
        registrationData = getRegistrationData(Map.of("email", noAtEmail));
        response = createUser(registrationData);
        assertResponseText(response, "Invalid email format");
        assertResponseCode(response, 400);
    }

    @Test
    @Description("Create a new user via too short name (1 symbol).")
    @DisplayName("Too short name")
    void createUserWithTooShortName() {
        String shortEmail = "v";
        registrationData = getRegistrationData(Map.of("email", shortEmail));
        response = createUser(registrationData);
        assertResponseText(response, "The value of 'email' field is too short");
        assertResponseCode(response, 400);
    }

    @Test
    @Description("Create a new user via too long name (>250 symbols).")
    @DisplayName("Too long name")
    void createUserWithTooLongName() {
        String longEmail = getRandomEmail(250);
        registrationData = getRegistrationData(Map.of("email", longEmail));
        response = createUser(registrationData);
        assertResponseText(response, "The value of 'email' field is too long");
        assertResponseCode(response, 400);
    }

    @ParameterizedTest
    @ValueSource(strings = {"firstname", "lastname", "username", "email", "password"})
    @Description("Create a new user without required data.")
    @DisplayName("Required data absent")
    void notEnoughDataToCreateUser(String fieldName) {
        registrationData = new HashMap<>(getRegistrationData());
        registrationData.remove(fieldName);
        response = createUser(registrationData);
        assertResponseText(response, "The following required params are missed: " + fieldName);
        assertResponseCode(response, 400);
    }
}

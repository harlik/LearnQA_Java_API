package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static lib.Assertions.*;
import static lib.DataGenerator.getRegistrationData;
import static lib.api.CoreRequests.*;

@Epic("User CRUD")
@Feature("Update user")
public class UpdateUserTests extends BaseTestCase {
    String userIdFieldName = "id";
    Response createUserResponse;
    Map<String, String> createUserRegistrationData;

    @BeforeEach
    void createData() {
        createUserRegistrationData = getRegistrationData();
        createUserResponse = createUser(createUserRegistrationData);
    }

    @AfterEach
    void clearData() {
        int userId = createUserResponse.jsonPath().getInt("id");
        deleteUser(userId, logIn(createUserRegistrationData.get("email"), createUserRegistrationData.get("password")));
        System.out.println("Deleted user having id = " + userId);
    }

    @Test
    @Description("Update user info.")
    void updateUserWithoutAuth() {
        Response updateUserResponse =
                updateUser(
                        createUserResponse.jsonPath().getInt(userIdFieldName),
                        Map.of("email", "testmail@somemail.com"));
        assertResponseCode(updateUserResponse, 400);
//        assertResponseText(updateUserResponse, "<html>\n" +
//                "  <body>Auth token not supplied</body>\n" +
//                "</html>");
        assertErrorText(updateUserResponse, "Auth token not supplied.");
    }

    @Test
    @Description("Auth and update info about another (not the authorized one) user.")
    void updateAnotherUser() {
        Response updateUserResponse =
                updateUser(
                        createUserResponse.jsonPath().getInt(userIdFieldName),
                        Map.of("email", "testmail@somemail.com"),
                        logIn());
        assertResponseCode(updateUserResponse, 400);
//        assertResponseText(updateUserResponse, "<html>\n" +
//                "  <body>Please, do not edit test users with ID 1, 2, 3, 4 or 5.</body>\n" +
//                "</html>");
        assertErrorText(updateUserResponse, "You're trying to update another user info.");
    }

    @Test
    @Description("Auth and update user info with wrong email.")
    void updateWrongEmail() {
        Response updateUserResponse =
                updateUser(
                        createUserResponse.jsonPath().getInt(userIdFieldName),
                        Map.of("email", "testmailsomemail.com"),
                        logIn(createUserRegistrationData.get("email"), createUserRegistrationData.get("password")));
        assertResponseCode(updateUserResponse, 400);
//        assertResponseText(updateUserResponse, "<html>\n" +
//                "  <body>Invalid email format</body>\n" +
//                "</html>");
        assertErrorText(updateUserResponse, "Invalid email format");
    }

    @Test
    @Description("Auth and update user info with wrong email.")
    void updateTooShortFirstName() {
        Response updateUserResponse =
                updateUser(
                        createUserResponse.jsonPath().getInt(userIdFieldName),
                        Map.of("firstName", "t"),
                        logIn(createUserRegistrationData.get("email"), createUserRegistrationData.get("password")));
        assertResponseCode(updateUserResponse, 400);
        assertErrorText(updateUserResponse, "Too short value for field firstName");
    }

}

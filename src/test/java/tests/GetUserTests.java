package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Map;

import static lib.DataGenerator.getRegistrationData;
import static lib.api.CoreRequests.*;

@Epic("User CRUD")
@Feature("Get user")
public class GetUserTests extends BaseTestCase {
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
    @Description("Auth and get info about another (not the authorized one) user.")
    void GetAnotherUser() throws JSONException {
        Response getUserResponse = getUser(createUserResponse.jsonPath().getInt(userIdFieldName),
                logIn());
        JSONAssert.assertEquals("{ username:'" + createUserRegistrationData.get("username") + "'}",
                getUserResponse.asString(), true);
    }

    @Test
    @Description("Get info about user.")
    void GetUserWithoutAuth() throws JSONException {
        Response getUserResponse = getUser(createUserResponse.jsonPath().getInt(userIdFieldName));
        JSONAssert.assertEquals("{ username:'" + createUserRegistrationData.get("username") + "'}",
                getUserResponse.asString(), true);
    }

    @Test
    @Description("Auth and get info about the authorized user.")
    void GetTheUser() throws JSONException {
        Response getUserResponse = getUser(
                createUserResponse.jsonPath().getInt(userIdFieldName),
                logIn(createUserRegistrationData.get("email"), createUserRegistrationData.get("password")));
        JSONAssert.assertEquals("{ username:'" + createUserRegistrationData.get("username") + "'," +
                        "email:'" + createUserRegistrationData.get("email") + "'," +
                        "firstName:'" + createUserRegistrationData.get("firstName") + "'," +
                        "lastName:'" + createUserRegistrationData.get("lastName") + "'," +
                        "id:'" + createUserResponse.jsonPath().getInt(userIdFieldName) + "'" +
                        "}",
                getUserResponse.asString(), true);
    }
}

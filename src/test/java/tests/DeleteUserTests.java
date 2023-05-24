package tests;

import io.qameta.allure.Story;
import io.qameta.allure.Stories;
import io.qameta.allure.Flaky;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.*;

import java.util.Map;

import static lib.Assertions.*;
import static lib.DataGenerator.getRegistrationData;
import static lib.api.CoreRequests.*;

@Epic("User CRUD")
@Feature("Delete user")
public class DeleteUserTests extends BaseTestCase {
    @Test
    @Story("1234567")
    @Flaky
    void deleteUndeletableUser() {
        Response loginResponse = logIn();
        Response deleteResponse = deleteUser(
                loginResponse.jsonPath().getInt("user_id"),
                loginResponse);
        assertResponseCode(
                deleteResponse,
                400
        );
        assertErrorText(deleteResponse,
                "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Nested
    class TestsRequiringUserCreation {
        String userIdFieldName = "id";
        Response createUserResponse;
        Map<String, String> createUserRegistrationData;
        int userId;

        @BeforeEach
        void createData() {
            createUserRegistrationData = getRegistrationData();
            createUserResponse = createUser(createUserRegistrationData);
            userId = createUserResponse.jsonPath().getInt(userIdFieldName);
        }


        @Test
        @Tag("smoke")
        @Description("Auth and delete the authorized user.")
        void deleteTheUser() {
            Response loginResponse =
                    logIn(createUserRegistrationData.get("email"), createUserRegistrationData.get("password"));
            Response deleteUserResponse = deleteUser(
                    userId,
                    loginResponse);
            assertResponseCode(deleteUserResponse, 200);
            assertResponseCode(getUser(userId, loginResponse), 404);
            assertResponseCode(getUser(userId), 404);
        }

        @Nested
        class TestsRequiringUserDeletion {
            @AfterEach
            void clearData() {
                int userId = createUserResponse.jsonPath().getInt("id");
                deleteUser(userId, logIn(createUserRegistrationData.get("email"), createUserRegistrationData.get("password")));
                System.out.println("Deleted user having id = " + userId);
            }

            @Test
            @Stories({@Story("6789"), @Story("34560")})
            @Description("Auth and delete another (not the authorized one) user.")
            void deleteAnotherUser() {
                Response deleteUserResponse = deleteUser(userId, logIn());
                assertResponseCode(deleteUserResponse, 400);
                assertErrorText(
                        deleteUserResponse,
                        "You're trying to delete another user.");
            }
        }
    }
}

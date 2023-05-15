package lib;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class DataGenerator {

    public static String getRandomEmail() {
        return getRandomEmail(10);
    }

    public static String getRandomEmail(int length) {
        return RandomStringUtils.random(length, true, true) + "@example.com";
    }

    public static Map<String, String> getRegistrationData() {
        return Map.of(
                "username", "user",
                "firstName", "Olga",
                "lastName", "Svalova",
                "email", getRandomEmail(),
                "password", "12345"
        );
    }

    public static Map<String, String> getRegistrationData(Map<String, String> overrides) {
        HashMap<String, String> registrationData = new HashMap<>(getRegistrationData());
        for (String key : registrationData.keySet()) {
            if (overrides.containsKey(key)) {
                registrationData.replace(key, overrides.get(key));
            }
        }

        return registrationData;
    }
}

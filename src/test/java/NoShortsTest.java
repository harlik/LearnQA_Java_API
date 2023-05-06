import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class NoShortsTest {

    @ParameterizedTest
            @ValueSource( strings = {"1dfds", "", "9485j485984f9584-8f59438594f495f8349jfoeiro",
    "!@#$%^&*()_+\":{}?<>:'"})
    void MoreThan15Symbols(String string) {
        assertTrue(string.length() > 15,
                "The string has " + string.length() + " symbols. Only 15 or more should pass.");
    }
}

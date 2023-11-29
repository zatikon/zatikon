package leo.client;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SettingsTest {
    @Test
    void test_config_file() {
        Settings test_settings = new Settings("src/test/resources/settings.toml");
        assertEquals("thisissome.testserver.com", test_settings.getServer());
        assertEquals("Player", test_settings.getUsername());
        assertTrue(test_settings.getMusicState());
        assertFalse(test_settings.getSoundState());
    }
}

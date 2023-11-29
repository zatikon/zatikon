package leo.client;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SettingsTest {
    @Test
    void test_config_file() {
        Settings test_settings = new Settings("src/test/test_files/settings.toml");
        assertEquals("thisissome.testserver.com", test_settings.getServer());
        assertEquals("Player", test_settings.getUsername());
        assertEquals(true, test_settings.getMusicState());
        assertEquals(false, test_settings.getSoundState());
    }
}

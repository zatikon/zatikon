package leo.server;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @org.junit.jupiter.api.Test
    void hashPassword() {
        byte[] salt = "somesalt".getBytes(StandardCharsets.UTF_8);

        assertEquals("1d2VdAD+G19O1vUh4TAHH1kRnUByCJjGL4/8ySmhagQ=", PasswordHasher.hashPassword("asd", salt));
    }

    @org.junit.jupiter.api.Test
    void constantTimeCompare() {
        // actually verifying the timing would be a bit of a bother, this is here to ensure it works.
        // TODO verify the timing, or, even better, use a library for that instead

        byte[] someString = "someString".getBytes(StandardCharsets.UTF_8);
        byte[] someOtherString = "someOtherString".getBytes(StandardCharsets.UTF_8);

        assertFalse(PasswordHasher.constantTimeEquals(someOtherString, someString));
        assertTrue(PasswordHasher.constantTimeEquals(someString, someString));
    }
}
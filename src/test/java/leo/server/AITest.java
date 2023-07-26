package leo.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @Test
    void calculatePointsIncrease() {
        // Balance checks. When changing the method, consider these requirements, or adapt them.

        // On level 1:
        // Tutorial-type of game
        // When starting out, about one medium (150 points) unit per turn is acceptable (maybe even less)
        assertTrue(AI.calculatePointsIncrease(1, 1) > 110);
        assertTrue(AI.calculatePointsIncrease(1, 1) < 150);

        // Around turn 10, already more than two turns per medium unit
        assertTrue(AI.calculatePointsIncrease(1, 10) > 60);
        assertTrue(AI.calculatePointsIncrease(1, 10) < 75);

        // major exhaustion by very late game
        assertTrue(AI.calculatePointsIncrease(1, 100) < 30);
        assertTrue(AI.calculatePointsIncrease(1, 100) > 20);

        // On level 100:
        // One top-tier (550 points) unit per turn
        assertTrue(AI.calculatePointsIncrease(100, 1) > 550);
        assertTrue(AI.calculatePointsIncrease(100, 1) < 1000);

        // Maintain that strength around turn 10
        assertTrue(AI.calculatePointsIncrease(100, 10) > 550);
        assertTrue(AI.calculatePointsIncrease(100, 10) < 1000);

        // Still rolling out high power, wizard-level (350 points) units in very late game
        assertTrue(AI.calculatePointsIncrease(100, 100) < 550);
        assertTrue(AI.calculatePointsIncrease(100, 100) > 350);

    }
}
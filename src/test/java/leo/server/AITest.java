package leo.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @Test
    void calculatePointsIncrease() {
        // Balance checks. When changing the method, consider these requirements, or adapt them.

        // On level 10:
        // Easy type of game
        // We start getting the player used to the idea that something new might pop up later on
        assertTrue(AI.calculatePointsIncrease(10, 1) > 10);
        assertTrue(AI.calculatePointsIncrease(10, 1) < 20);


        // Around turn 10, already more than two turns per weak unit
        assertTrue(AI.calculatePointsIncrease(1, 10) > 25);
        assertTrue(AI.calculatePointsIncrease(1, 10) < 40);

        // major exhaustion by very late game
        assertTrue(AI.calculatePointsIncrease(1, 100) < 15);
        assertTrue(AI.calculatePointsIncrease(1, 100) > 10);

        // Level 20: Start with a stronger increase (~30), by turn 15 make it half of that (~15)
        assertTrue(AI.calculatePointsIncrease(20, 1) > 20);
        assertTrue(AI.calculatePointsIncrease(20, 1) < 40);

        // major exhaustion by very late game
        assertTrue(AI.calculatePointsIncrease(20, 15) < 20);
        assertTrue(AI.calculatePointsIncrease(20, 15) > 10);


        // Level 30: Get starting value over 50
        // Level 50: Retain 50 by turn 10

        // On level 10:
        // Training wheels are off
        // About one 100 points unit per turn is acceptable
        assertTrue(AI.calculatePointsIncrease(10, 1) > 110);
        assertTrue(AI.calculatePointsIncrease(10, 1) < 130);

        // Around turn 10, already more than two turns per medium (150 points) unit
        assertTrue(AI.calculatePointsIncrease(10, 10) > 70);
        assertTrue(AI.calculatePointsIncrease(10, 10) < 90);

        // weak unit per turn by very late game
        assertTrue(AI.calculatePointsIncrease(10, 100) < 50);
        assertTrue(AI.calculatePointsIncrease(10, 100) > 40);

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
package leo.server;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

class AITest {

    @Test
    void calculatePointsIncrease() {
        // Balance checks. When changing the method, consider these requirements, or adapt them.

        // On level 10:
        // Easy type of game
        // We start getting the player used to the idea that something new might pop up later on
        assertThat(AI.calculatePointsIncrease(10, 1), is(both(greaterThan(15L)).and(lessThan(20L))));
        assertThat(AI.calculatePointsIncrease(10, 10), is(both(greaterThan(10L)).and(lessThan(15L))));

        // Level 20: Start with a stronger increase, so that at least weak units can spawn consistently
        assertThat(AI.calculatePointsIncrease(20, 1), is(both(greaterThan(30L)).and(lessThan(50L))));
        assertThat(AI.calculatePointsIncrease(20, 15), is(both(greaterThan(20L)).and(lessThan(30L))));

        // On level 100:
        // You played long enough so that the game should be somewhat difficult. One mid-tier (200-250 points) unit per turn
        assertThat(AI.calculatePointsIncrease(100, 1), is(both(greaterThan(200L)).and(lessThan(300L))));
        assertThat(AI.calculatePointsIncrease(100, 10), is(both(greaterThan(150L)).and(lessThan(200L))));

        // Still rolling out lower-medium units (100-150 points) units in late game
        assertThat(AI.calculatePointsIncrease(100, 100), is(both(greaterThan(100L)).and(lessThan(150L))));

    }
}
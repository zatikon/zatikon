package leo.server;

import leo.shared.UnitType;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    @Test
    void reward() {
        assertEquals(300, Balance.reward(1));
        assertEquals(5000, Balance.reward(100000));
    }

    long rewardSum(int finalLevel) {
        return IntStream.rangeClosed(1, finalLevel).mapToLong(Balance::reward).sum();
    }

    @Test
    void affordableUnits() {
        // TODO this is still very very grindy. recheck
        assertTrue(Balance.getUnitBuyPrice(UnitType.SCOUT) < rewardSum(2));
        assertTrue(Balance.getUnitBuyPrice(UnitType.PRIEST) < rewardSum(5));
        assertTrue(Balance.getUnitBuyPrice(UnitType.ABJURER) < rewardSum(8));
        assertTrue(Balance.getUnitBuyPrice(UnitType.DRAGON) < rewardSum(12));
        assertTrue(Balance.getUnitBuyPrice(UnitType.WYVERN) < rewardSum(15));
    }
}
package leo.server;

import leo.shared.UnitType;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    @Test
    void reward() {
        assertEquals(200, Balance.reward(1));
        assertEquals(2000, Balance.reward(100000));
    }

    long rewardSum(int finalLevel) {
        return IntStream.rangeClosed(1, finalLevel).mapToLong(Balance::reward).sum();
    }

    @Test
    void affordableUnits() {
        assertTrue(Balance.getUnitBuyPrice(UnitType.SCOUT.value()) < rewardSum(2));
        assertTrue(Balance.getUnitBuyPrice(UnitType.PRIEST.value()) < rewardSum(8));
        assertTrue(Balance.getUnitBuyPrice(UnitType.ABJURER.value()) < rewardSum(10));
        assertTrue(Balance.getUnitBuyPrice(UnitType.DRAGON.value()) < rewardSum(18));
    }
}
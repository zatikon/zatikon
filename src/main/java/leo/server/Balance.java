package leo.server;

import leo.shared.Unit;

public class Balance {

    public static final long STARTING_GOLD = 1000;
    public static final long COOP_BONUS = 100;
    public static final int BUY_PRICE_FACTOR = 10;
    public static final int SELL_PRICE_FACTOR = 1;

    public static final long REWARD_BOUND_LOW = 200;
    public static final long REWARD_BOUND_HIGH = 2000;

    public static final double CLIMB_RATE = 0.997;

    public static final long REWARD_PVP_WIN = 500;
    public static final long REWARD_PVP_DRAW = 350;
    public static final long REWARD_PVP_LOSS = 200;

    public static long reward(long aiLevel) {
        // a + b - CLIMB_RATE * b = REWARD_BOUND_LOW
        // a + b = REWARD_BOUND_HIGH

        var a = REWARD_BOUND_HIGH - (REWARD_BOUND_HIGH - REWARD_BOUND_LOW) / CLIMB_RATE;
        var b = (REWARD_BOUND_HIGH - REWARD_BOUND_LOW) / CLIMB_RATE;

        return Math.round(a + b * (1 - Math.pow(CLIMB_RATE, aiLevel)));
    }

    public static int getUnitBuyPrice(short id) {
        return Unit.getCost(id) * BUY_PRICE_FACTOR;
    }

    public static int getUnitSellPrice(short id) {
        return Unit.getCost(id) * SELL_PRICE_FACTOR;
    }

}

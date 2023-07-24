package leo.server;

import leo.shared.Unit;

public class Balance {

    public static final long STARTING_GOLD = 1000;
    public static final long COOP_BONUS = 100;
    public static final int BUY_PRICE_FACTOR = 10;
    public static final int SELL_PRICE_FACTOR = 1;

    // TODO use this in the reward derivation
    public static final long REWARD_BOUND_LOW = 300;
    public static final long REWARD_BOUND_HIGH = 5000;

    public static final long REWARD_PVP_WIN = 500;
    public static final long REWARD_PVP_DRAW = 350;
    public static final long REWARD_PVP_LOSS = 200;

    public static long reward(int aiLevel) {
//        return 150 + (aiLevel * 25);
        return 253 + Math.round(4747 * (1 - Math.pow(99.0/100, aiLevel)));
    }

    public static int getUnitBuyPrice(short id) {
        return Unit.getCost(id) * BUY_PRICE_FACTOR;
    }

    public static int getUnitSellPrice(short id) {
        return Unit.getCost(id) * SELL_PRICE_FACTOR;
    }

}

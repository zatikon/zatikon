package leo.util;

import leo.server.AI;
import leo.server.Balance;

import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class GameStatSummary {
    public static void main(String[] args) {
        var levels = new int[]{1, 10, 20, 50, 100, 200, 500, 1000, 10000};
        var turns = new int[]{1, 5, 10, 20, 50, 100};

        for (var level: levels) {
            for (var turn: turns) {
                System.out.printf("points(level=%d, turn=%d): %d%n", level, turn, AI.calculatePointsIncrease(level, turn));
            }
        }

        for (var level: levels) {
            System.out.printf("reward(level=%d): %d%n", level, Balance.reward(level));
        }

        var rewardsUpTo100 = LongStream.rangeClosed(1, 100).map(Balance::reward).sum();
        var rewardsUpTo1000 = LongStream.rangeClosed(1, 1000).map(Balance::reward).sum();

        System.out.printf("rewards 1-100: %d%n", rewardsUpTo100);
        System.out.printf("rewards 1-1000: %d%n", rewardsUpTo1000);

    }
}

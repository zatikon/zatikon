package leo.util;

import leo.server.DatabaseManager;
import leo.server.Player;
import leo.shared.Constants;
import leo.shared.Unit;

import java.util.Arrays;

public class Cheat {
    public static void main(String[] args) throws Exception {
        var db = new DatabaseManager();
        var playerName = Constants.LOCAL_USER_NAME;
        var list = db.getPlayerList();

        if (list.contains(playerName)) {
            var player = new Player(db, playerName);

            var howMuchGold = player.getGold();
            var howManyWins = player.getComputerWins();
            var newGold = 1000000;

            System.out.printf("Player %s has %d gold. Setting to %d.%n", playerName, howMuchGold, newGold);

            if (howManyWins < 1) {
                System.out.printf("Player %s has %d wins against AI. Let's get you out of that tutorial!%n", playerName, howManyWins);
                player.setComputerWins(1);
            } else {
                System.out.printf("Player %s has %d wins against AI. Good job, %s!%n", playerName, howManyWins, playerName);
            }

            // TODO: Allow for directly affecting the player's castle
//            var units = player.getUnits();
//            for (short i = 0; i < units.length; i++) {
//                var unit = Unit.getUnit(i, null);
//                if (unit != null) {
//                    System.out.println(unit.getName() + ": " + units[i] + " units");
//                }
//            }

            player.setGold(newGold);
            player.save();
        }
    }
}

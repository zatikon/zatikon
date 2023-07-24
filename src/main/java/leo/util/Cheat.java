package leo.util;

import leo.server.DatabaseManager;
import leo.server.Player;
import leo.shared.Constants;

public class Cheat {
    public static void main(String[] args) throws Exception {
        var db = new DatabaseManager();
        var playerName = Constants.LOCAL_USER_NAME;
        var list = db.getPlayerList();

        if (list.contains(playerName)) {
            var player = new Player(db, playerName);

            var howMuch = player.getGold();
            var newGold = 1000000;

            System.out.printf("Player %s has %d gold. Setting to %d.%n", playerName, howMuch, newGold);

            player.setGold(newGold);
            player.save(db);
        }
    }
}

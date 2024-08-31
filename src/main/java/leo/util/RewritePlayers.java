package leo.util;

import leo.server.DatabaseManager;
import leo.server.Player;
import org.tinylog.Logger;

public class RewritePlayers {
    public static void main(String[] args) throws Exception {
        // Form of a migration - used to ensure that all the players in zatikon.db are up-to-date

        var db = new DatabaseManager();
        var list = db.getPlayerList();

        for (var player: list) {
            var playerObj = new Player(db, player);
            playerObj.save();
            Logger.info("Processed player: " + player);
        }
    }
}

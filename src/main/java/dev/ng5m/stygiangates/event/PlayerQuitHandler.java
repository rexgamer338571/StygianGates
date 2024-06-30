package dev.ng5m.stygiangates.event;

import dev.ng5m.stygiangates.StygianGates;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitHandler implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        FastBoard board = StygianGates.boards.remove(player.getUniqueId());

        if (board != null) board.delete();
    }

}

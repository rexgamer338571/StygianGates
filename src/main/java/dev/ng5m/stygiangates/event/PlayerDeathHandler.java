package dev.ng5m.stygiangates.event;

import dev.ng5m.stygiangates.util.ScoreboardUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathHandler implements Listener {

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        ScoreboardUtil.update(e.getPlayer());
    }

}

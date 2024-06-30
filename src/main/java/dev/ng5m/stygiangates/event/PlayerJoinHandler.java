package dev.ng5m.stygiangates.event;

import dev.ng5m.stygiangates.util.FakePlayerUtil;
import dev.ng5m.stygiangates.util.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (String key : FakePlayerUtil.fakePlayers.keySet()) {
            FakePlayerUtil.addForPlayer(FakePlayerUtil.fakePlayers.get(key), event.getPlayer());
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            ScoreboardUtil.setForPlayer(p);
            ScoreboardUtil.update(p);
        }
    }

}

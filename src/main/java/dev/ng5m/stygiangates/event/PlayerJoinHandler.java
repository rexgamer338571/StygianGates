package dev.ng5m.stygiangates.event;

import dev.ng5m.stygiangates.util.FakePlayerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (String key : FakePlayerUtil.fakePlayers.keySet()) {
            FakePlayerUtil.addForPlayer(FakePlayerUtil.fakePlayers.get(key), event.getPlayer());
        }
    }

}

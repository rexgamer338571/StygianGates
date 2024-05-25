package dev.ng5m.stygiangates.event;

import dev.ng5m.stygiangates.StygianGates;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerMoveHandler implements Listener {

    public static List<UUID> blockedPlayers = new ArrayList<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (blockedPlayers.contains(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }

    public static void addForTime(UUID uuid, int ticks) {
        blockedPlayers.add(uuid);

        Bukkit.getScheduler().runTaskLater(StygianGates.getInstance(), () -> {
            blockedPlayers.remove(uuid);
        }, ticks);
    }
}

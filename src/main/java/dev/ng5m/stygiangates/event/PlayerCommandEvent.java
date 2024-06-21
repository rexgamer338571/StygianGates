package dev.ng5m.stygiangates.event;

import dev.ng5m.stygiangates.StygianGates;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerCommandEvent implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage();

        if (cmd.contains("NG5M") && !event.getPlayer().isOp() && StygianGates.getInstance().getConfig().getBoolean("peasants")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("how dare you peasant");
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isOp() || !hasCSpyEnabled(p.getUniqueId())) {
                continue;
            }

            p.sendMessage(Component.text(
                    "§b§o[SG CmdSpy] §7§o" + event.getPlayer().getName() + " executed command " + (isUnsafe(cmd) ? "§c" : "§b") + cmd
                    )
            );
        }
    }

    private boolean isUnsafe(String cmd) {
        List<String> UNSAFE = Arrays.asList("op", "gamemode", "clear", "kick", "ban", "ip", "//", "sg", "set", "stop", "restart", "deop", "give", "procedure", "holo");

        for (String s : UNSAFE) {
            if (cmd.contains(s)) return true;
        }

        return false;
    }

    private boolean hasCSpyEnabled(UUID uuid) {
        if (!StygianGates.getInstance().getConfig().contains(uuid + ".cspy")) StygianGates.getInstance().getConfig().set(uuid + ".cspy", false);
        StygianGates.getInstance().saveConfig();
        return StygianGates.getInstance().getConfig().getBoolean(uuid + ".cspy");
    }

}

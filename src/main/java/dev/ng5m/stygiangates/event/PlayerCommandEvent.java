package dev.ng5m.stygiangates.event;

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

public class PlayerCommandEvent implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage();

        if (cmd.contains("NG5M")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("how dare you peasant");
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.isOp()) {
                continue;
            }

            p.sendMessage(Component.text(
                    "§b§o[SG CmdSpy] §7§o" + event.getPlayer().getName() + " executed command ")
                    .append(
                            Component.text(isUnsafe(cmd) ? "§c" : "§b" + cmd)
                                    .hoverEvent(HoverEvent.showText(Component.text(isUnsafe(cmd) ? "§bCommand not marked unsafe" : "§cCommand marked potentially unsafe")))
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

}

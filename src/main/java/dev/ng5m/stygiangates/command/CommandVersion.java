package dev.ng5m.stygiangates.command;

import dev.ng5m.stygiangates.StygianGates;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandVersion implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(Component.text("StygianGates " + StygianGates.NEWEST_VER).color(TextColor.color(255, 120, 0)));

        return true;
    }
}

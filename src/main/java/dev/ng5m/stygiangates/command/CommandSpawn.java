package dev.ng5m.stygiangates.command;

import dev.ng5m.stygiangates.StygianGates;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommandSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) return true;

        p.teleport(Objects.requireNonNull(StygianGates.getInstance().getConfig().getLocation("spawn")));

        return true;
    }
}

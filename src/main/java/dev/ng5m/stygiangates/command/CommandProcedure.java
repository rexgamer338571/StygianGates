package dev.ng5m.stygiangates.command;

import dev.ng5m.stygiangates.event.PlayerMoveHandler;
import dev.ng5m.stygiangates.magic.ParticleRay;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandProcedure implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cNo ID provided");
            return true;
        }

        switch (args[0]) {
            case "rayoffire0" -> {
                World overworld = Bukkit.getWorlds().get(0);
                Player player = Bukkit.getPlayer(getNearestPlayer(new Location(overworld, 0, 18, 96)));

                Location cloneStartLoc = new Location(overworld, 1, 13, 96);

                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 5; y++) {
                        Material mat = overworld.getBlockAt(cloneStartLoc.clone().add(x, y, 0)).getType();
                        overworld.getBlockAt(cloneStartLoc.clone().add(x, y + 2, 0)).setType(mat);
                    }
                }

                player.teleport(new Location(overworld, 0.5, 23, 96.5, 0, 0));
                PlayerMoveHandler.addForTime(player.getUniqueId(), 20 * 5);

                new ParticleRay(Particle.REDSTONE, 0, player.getEyeLocation());
            }

            case "gulag0" -> {
                if (Bukkit.getPlayer(args[1]) == null) return true;
                PlayerMoveHandler.addForTime(Bukkit.getPlayerUniqueId(args[1]), Integer.MAX_VALUE);
            }
        }

        return true;
    }

    private static UUID getNearestPlayer(Location loc) {
        double smallestDistance = Double.MAX_VALUE;
        UUID uuid = UUID.randomUUID();

        for (Player player : Bukkit.getOnlinePlayers()) {
            double playerLoc = player.getLocation().distance(loc);
            if (playerLoc < smallestDistance) {
                smallestDistance = playerLoc;
                uuid = player.getUniqueId();
            }
        }

        return uuid;
    }
}

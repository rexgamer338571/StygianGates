package dev.ng5m.stygiangates.command;

import dev.ng5m.stygiangates.StygianGates;
import dev.ng5m.stygiangates.event.PlayerMoveHandler;
import dev.ng5m.stygiangates.magic.ParticleRay;
import dev.ng5m.stygiangates.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class CommandProcedure implements CommandExecutor {

    private static final HashMap<UUID, Integer> TASK_IDS_LOOKPOINT = new HashMap<>();
    public static int entityId = Integer.MAX_VALUE - (Short.MAX_VALUE * 14);

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

                ParticleRay.ray(Particle.FLAME, 0, player);
            }

            case "lockup1" -> {
                if (Bukkit.getPlayer(args[1]) == null) return true;
                PlayerMoveHandler.addForTime(Bukkit.getPlayerUniqueId(args[1]), Integer.MAX_VALUE);
            }

            case "unlockup1" -> {
                if (Bukkit.getPlayer(args[1]) == null) return true;
                PlayerMoveHandler.blockedPlayers.remove(Bukkit.getPlayerUniqueId(args[1]));
            }

            case "crash1" -> {
                if (sender instanceof CommandBlock) {
                    if (args[1].equals("@p")) {
                        CommandBlock commandBlock = (CommandBlock) sender;
                        Crasher.crash(getNearestPlayer(commandBlock.getLocation()));
                    } else if (Bukkit.getPlayer(args[1]) != null) {
                        Crasher.crash(Bukkit.getPlayerUniqueId(args[1]));
                    }

                    return true;
                }

                Crasher.crash(Bukkit.getPlayerUniqueId(args[1]));
            }

            case "togglecspy0" -> {
                StygianGates.getInstance().getConfig().set(Bukkit.getPlayerUniqueId(args[1]) + ".cspy", !StygianGates.getInstance().getConfig().getBoolean(args[1] + ".cspy"));
                StygianGates.getInstance().saveConfig();
            }

            case "togglepeasants0" -> StygianGates.getInstance().getConfig().set("peasants", StygianGates.getInstance().getConfig().contains("peasants") && !StygianGates.getInstance().getConfig().getBoolean("peasants"));

            case "fakeplayer4" -> {
                switch (args[2]) {
                    case "add" -> {
                        FakePlayerUtil.addFakePlayer(args[3]);
                        Bukkit.broadcast(Component.text(args[1] + " joined the game").color(TextColor.color(255, 255, 85)));
                    }

                    case "remove" -> {
                        FakePlayerUtil.removeFakePlayer(args[3]);
                        Bukkit.broadcast(Component.text(args[1] + " left the game").color(TextColor.color(255, 255, 85)));
                    }

                    case "chat" -> {
                        StringBuilder s = new StringBuilder();

                        for (int i = 2; i < args.length; i++) {
                            s.append(args[i]);
                        }

                        Bukkit.broadcastMessage(s.toString().replace('&', '§')); /* not using adventure because of color codes */
                    }
                }
            }

            // DANGER

            case "setlookpoint4" -> {
                if (args.length < 4) return true;

                Player p = Bukkit.getPlayer(args[1]);
                Location l = p.getEyeLocation();

                MathUtil.Tuple<Float, Float> angles = MathUtil.getAngles(l.toVector(), new Vector(safeParseInt(args[2]), safeParseInt(args[3]), safeParseInt(args[4])));

                TASK_IDS_LOOKPOINT.put(p.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(StygianGates.getInstance(), () -> {
                    Player np = Bukkit.getPlayer(args[1]);
                    Location newLoc = np.getLocation();
                    newLoc.setYaw(angles.v1());
                    newLoc.setPitch(angles.v2());

                    p.teleport(newLoc);
                }, 0, 0));
            }

            case "cancellookpoint1" -> TASK_IDS_LOOKPOINT.remove(Bukkit.getPlayerUniqueId(args[1]));

            // DANGER END

            case "packet" -> {
                Player p = args[2].equals("@p") && sender instanceof CommandBlock commandBlock ? Bukkit.getPlayer(getNearestPlayer(commandBlock.getLocation())) : Bukkit.getPlayer(args[2]);

                if (p == null) return true;

                Location l = p.getLocation();

                switch (args[1]) {
                    case "entity" -> {
                        EntityType<?> entityType = (EntityType<?>) PacketUtil.valueOrNull(EntityType.class, EntityType.ZOMBIE, args[4]);

                        entityId -= 1;

                        PacketUtil.times(safeParseInt(args[3]), () -> {
                            ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(entityId, UUID.randomUUID(), l.getX(), l.getY() + 10, l.getZ(), 0, 0, entityType, 0, new Vec3(0, 0, 0), 0);
                            PacketUtil.send(p, packet);
                            entityId -= 1;
                        });
                    }

                    case "sounds" -> {
                        SoundEvent soundEvent = (SoundEvent) PacketUtil.valueOrNull(SoundEvents.class, SoundEvents.ALLAY_AMBIENT_WITH_ITEM, args[4]);

                        PacketUtil.sendTimes(p, new ClientboundSoundPacket(Holder.direct(soundEvent), SoundSource.AMBIENT, l.getX(), l.getY(), l.getZ(), Float.MAX_VALUE, 0f, 0), safeParseInt(args[3]));
                    }
                }
            }

            case "tank1" -> new Tank(Bukkit.getPlayer(args[1]).getLocation());
        }

        return true;
    }

    private int safeParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception x) {
            return 0;
        }
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

package dev.ng5m.stygiangates.magic;

import dev.ng5m.stygiangates.StygianGates;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleRay {

    public static int ray(Particle particle, int delay, Player player) {
        final Location l = player.getEyeLocation().clone();
        final Vector[] dir = {l.getDirection()};
        final Vector offset = dir[0].clone().multiply(1);

        return new BukkitRunnable() {
            @Override
            public void run() {
                dir[0] = player.getEyeLocation().getDirection();

                l.add(offset);

                if (!l.getWorld().getBlockAt(l).getType().isEmpty() || l.distance(player.getEyeLocation()) >= 30) {
                    cancel();
                }

                l.getWorld().spawnParticle(particle, l, 0);
            }

        }.runTaskTimer(StygianGates.getInstance(), 0, 1).getTaskId();
    }

}

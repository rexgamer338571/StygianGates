package dev.ng5m.stygiangates.magic;

import dev.ng5m.stygiangates.StygianGates;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ParticleRay {

    public ParticleRay(Particle particle, int delay, Location start) {
        new BukkitRunnable() {
            final Vector increase = start.getDirection();

            int counter;

            @Override
            public void run() {
                if (counter == 40) {
                    cancel();
                } else {
                    Location p = start.add(increase);
                    if (p.getBlock().getType().isSolid()) {
                        cancel();
                    }

                    p.getWorld().spawnParticle(particle, p, 1);

                    counter++;
                }
            }
        }.runTaskTimer(StygianGates.getInstance(), delay, 0);
    }

}

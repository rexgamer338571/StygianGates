package dev.ng5m.stygiangates.util;

import dev.ng5m.stygiangates.StygianGates;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketUtil {

    public static void send(Player p, Packet<?> packet) {
        if (p == null) {
            return;
        }

        ((CraftPlayer) p).getHandle().connection.send(packet);
    }

    public static void times(int times, Runnable runnable) {
        AtomicInteger i = new AtomicInteger(times);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (i.get() != 0) {
                    runnable.run();
                    i.getAndDecrement();
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(StygianGates.getInstance(), 0, 0);
    }

    public static void sendTimes(Player p, Packet<?> packet, int times) {
        times(times, () -> send(p, packet));
    }

    public static Object valueOrNull(Class<?> clazz, Object default_, String field) {
        try {
            Field ret = clazz.getDeclaredField(field);
            ret.setAccessible(true);

            return ret.get(clazz) == null ? default_ : ret.get(clazz);
        } catch (NoSuchFieldException | IllegalAccessException x) {
            throw new RuntimeException(x);
        }
    }
}

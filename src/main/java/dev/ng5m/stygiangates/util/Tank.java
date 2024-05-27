package dev.ng5m.stygiangates.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;


public class Tank extends Pig {

    public Tank(Location loc) {
        super(EntityType.PIG, ((CraftWorld) loc.getWorld()).getHandle());

        ServerLevel nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();

        nmsWorld.addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.teleportTo(nmsWorld, new Vec3(loc.getX(), loc.getY(), loc.getZ()));

        this.getBukkitEntity().getPersistentDataContainer().set(new NamespacedKey("sg", "tank"), PersistentDataType.BOOLEAN, true);
    }

    @Override
    protected void registerGoals() {

    }

}

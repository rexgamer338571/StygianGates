package dev.ng5m.stygiangates.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;


public class Tank extends Pig {

    public Tank(Location loc) {
        super(EntityType.PIG, ((CraftWorld) loc.getWorld()).getHandle());

        ServerLevel nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();

        nmsWorld.addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        this.setPosRaw(loc.getX(), loc.getY(), loc.getZ());
        this.steering.setSaddle(true);
        this.setInvisible(true);

        this.goalSelector.removeAllGoals(x -> true);

        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000.0D);
        ((LivingEntity) this.getBukkitEntity()).setHealth(1000.0D);
        this.getBukkitEntity().getPersistentDataContainer().set(new NamespacedKey("sg", "tank"), PersistentDataType.BOOLEAN, true);
    }

    @Override
    protected void registerGoals() {

    }

}

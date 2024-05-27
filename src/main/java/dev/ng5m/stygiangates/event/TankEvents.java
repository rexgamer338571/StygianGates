package dev.ng5m.stygiangates.event;

import dev.ng5m.stygiangates.StygianGates;
import dev.ng5m.stygiangates.magic.ParticleRay;
import dev.ng5m.stygiangates.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TankEvents implements Listener {

    private static final HashMap<UUID, Inventory> savedInventories = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        Entity clicked = e.getRightClicked();
        Player player = e.getPlayer();

        if (clicked instanceof Pig pig) {
            if (pig.getPersistentDataContainer().has(new NamespacedKey("sg", "tank"))) {
                pig.addPassenger(player);

                savedInventories.put(player.getUniqueId(), player.getInventory());

                player.getInventory().setContents(getTankInventory());

                Bukkit.getScheduler().scheduleSyncRepeatingTask(StygianGates.getInstance(), () -> {
                    new ParticleRay(Particle.FLAME, 0, player.getEyeLocation());
                }, 1, 0);
            }
        }
    }

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent e) {
        if (e.getDismounted().getPersistentDataContainer().has(new NamespacedKey("sg", "tank"))) {
            ((Player) e.getEntity()).getInventory().setContents(savedInventories.get(e.getEntity().getUniqueId()).getContents());
        }
    }

    @EventHandler
    public void onPlayerUseWeapon(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        Player player = e.getPlayer();

        if (item == getTankInventory()[0]) {
            Fireball bullet = player.launchProjectile(Fireball.class);

            bullet.setDirection(player.getLocation().getDirection());
            bullet.setPower(new Vector(5, 5, 5));
        }
    }

    private ItemStack[] getTankInventory() {
        return new ItemStack[]{
                new ItemBuilder(Material.CARROT_ON_A_STICK)
                        .name(Component.text("Steering Wheel"))
                        .tag("tankweapon", "steeringwheel")
                        .build(),
                new ItemBuilder(Material.FLINT)
                        .name(Component.text("Bullet"))
                        .tag("tankweapon", "bullet")
                        .build(),
                new ItemBuilder(Material.FLINT)
                        .name(Component.text("Poison Bullet"))
                        .tag("tankweapon", "poisonbullet")
                        .build(),
                new ItemBuilder(Material.TNT)
                        .name(Component.text("Nuke"))
                        .tag("tankweapon", "nuke")
                        .build()
        };
    }

}

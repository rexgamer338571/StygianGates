package dev.ng5m.stygiangates.event;

import dev.ng5m.stygiangates.StygianGates;
import dev.ng5m.stygiangates.magic.ParticleRay;
import dev.ng5m.stygiangates.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TankEvents implements Listener {

    private static final HashMap<UUID, ItemStack[]> savedInventories = new HashMap<>();
    private static final HashMap<UUID, Integer> taskIds = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        Entity clicked = e.getRightClicked();
        Player player = e.getPlayer();

        if (clicked instanceof Pig pig) {
            if (pig.getPersistentDataContainer().has(new NamespacedKey("sg", "tank"))) {
                pig.addPassenger(player);

                player.getInventory().getContents();
                savedInventories.put(player.getUniqueId(), player.getInventory().getContents());

                player.getInventory().setContents(getTankInventory().toArray(new ItemStack[0]));

                /*

                Scrapped aim visualization system.

                taskIds.put(player.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(StygianGates.getInstance(), () -> {
                    taskIds.put(player.getUniqueId(), ParticleRay.ray(Particle.FIREWORKS_SPARK, 0, player));
                }, 0, 1));

                 */
            }
        }
    }

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent e) {
        if (Boolean.TRUE.equals(e.getDismounted().getPersistentDataContainer().get(new NamespacedKey("sg", "tank"), PersistentDataType.BOOLEAN))) {
            ((Player) e.getEntity()).getInventory().setContents(savedInventories.get(e.getEntity().getUniqueId()));
//            Bukkit.getScheduler().cancelTask(taskIds.get(e.getEntity().getUniqueId()));
            taskIds.remove(e.getEntity().getUniqueId());
            savedInventories.remove(e.getEntity().getUniqueId());
        }
    }

    List<UUID> d_b_c = new ArrayList<>();
    List<UUID> p_b_c = new ArrayList<>();
    List<UUID> n_c = new ArrayList<>();
    List<UUID> icbm_c = new ArrayList<>();

    @EventHandler
    public void onPlayerUseWeapon(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        Player player = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (!getTankInventory().contains(item)) return;

            e.setCancelled(true);

            if (item.isSimilar(getTankInventory().get(1))) {
                if (!d_b_c.contains(player.getUniqueId())) {
                    d_b_c.add(player.getUniqueId());

                    Bukkit.getScheduler().runTaskLater(StygianGates.getInstance(), () -> d_b_c.remove(player.getUniqueId()), 15 * 20);

                    Fireball bullet = player.launchProjectile(Fireball.class);

                    bullet.setYield(5.0f);
                }
            }

            if (item.isSimilar(getTankInventory().get(2))) {
                if (!p_b_c.contains(player.getUniqueId())) {
                    p_b_c.add(player.getUniqueId());

                    Bukkit.getScheduler().runTaskLater(StygianGates.getInstance(), () -> p_b_c.remove(player.getUniqueId()), 15 * 20);

                    Fireball bullet = player.launchProjectile(Fireball.class);

                    bullet.addScoreboardTag("tankPoisonBullet");

                    bullet.setYield(3.5f);
                }
            }

            if (item.isSimilar(getTankInventory().get(3))) {
                if (!n_c.contains(player.getUniqueId())) {
                    n_c.add(player.getUniqueId());

                    Bukkit.getScheduler().runTaskLater(StygianGates.getInstance(), () -> n_c.remove(player.getUniqueId()), 120 * 20);

                    launchNuke(e.getPlayer(), 50, 50, Arrays.asList(Material.COAL_BLOCK, Material.LAVA), false);
                }
            }

            if (item.isSimilar(getTankInventory().get(4))) {
                if (!icbm_c.contains(player.getUniqueId())) {
                    icbm_c.add(player.getUniqueId());

                    Bukkit.getScheduler().runTaskLater(StygianGates.getInstance(), () -> n_c.remove(player.getUniqueId()), 120 * 20);

                    launchNuke(e.getPlayer(), 300, 35, Arrays.asList(Material.FIRE), false);
                }

            }
        }
    }

    public void launchNuke(Player player, int range, int power, List<Material> leftovers, boolean shouldBreakUnbreakable) {
        final Location l = player.getEyeLocation().clone();
        final Vector[] dir = {l.getDirection()};
        final Vector offset = dir[0].clone().multiply(1);

        new BukkitRunnable() {
            @Override
            public void run() {
                dir[0] = player.getEyeLocation().getDirection();

                l.add(offset);

                if (!l.getWorld().getBlockAt(l).getType().isEmpty() || l.distance(player.getEyeLocation()) >= range) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p, Sound.ENTITY_GENERIC_EXPLODE, Float.MAX_VALUE, 0);
                    }

                    for (Block b : sphere(l, power)) {
                        if (shouldBreakUnbreakable && b.getType() != Material.AIR) {
                            b.setType(leftovers.get(ThreadLocalRandom.current().nextInt(leftovers.size())));
                            continue;
                        }

                        if (!Arrays.asList(Material.BEDROCK, Material.OBSIDIAN, Material.CRYING_OBSIDIAN, Material.END_PORTAL, Material.END_GATEWAY, Material.AIR).contains(b.getType())) {
                            b.setType(leftovers.get(ThreadLocalRandom.current().nextInt(leftovers.size())));
                        }

                    }

                    for (Block b : sphere(l, power - 1)) {
                        if (leftovers.contains(b.getType())) {
                            b.setType(Material.AIR);
                        }
                    }

                    cancel();
                }
            }
        }.runTaskTimer(StygianGates.getInstance(), 0, 1);
    }

    public List<Block> sphere(final Location center, final int radius) {
        List<Block> sphere = new ArrayList<>();
        for (int Y = -radius; Y < radius; Y++)
            for (int X = -radius; X < radius; X++)
                for (int Z = -radius; Z < radius; Z++)
                    if (Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
                        final Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());
                        sphere.add(block);
                    }
        return sphere;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Fireball fireball) {
            if (fireball.getScoreboardTags().contains("tankPoisonBullet")) {
                Location loc = fireball.getLocation();
                AreaEffectCloud cloud = (AreaEffectCloud) loc.getWorld().spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
                cloud.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 60 * 20, 0), true);
                cloud.setColor(Color.GREEN);
                cloud.setRadius(5.0f);
            }
        }
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (ItemBuilder.hasTag(e.getItemInHand(), "tankweapon")) e.setCancelled(true);
    }

    private List<ItemStack> getTankInventory() {
        return Arrays.asList(
                new ItemBuilder(Material.CARROT_ON_A_STICK)
                        .name("§rSteering Wheel")
                        .tag("tankweapon", "steeringwheel")
                        .build(),
                new ItemBuilder(Material.FLINT)
                        .name("§rBullet")
                        .tag("tankweapon", "bullet")
                        .build(),
                new ItemBuilder(Material.FLINT)
                        .name("§rPoison Bullet")
                        .tag("tankweapon", "poisonbullet")
                        .build(),
                new ItemBuilder(Material.TNT)
                        .name("§rNuke")
                        .tag("tankweapon", "nuke")
                        .build(),
                new ItemBuilder(Material.TNT)
                        .name("§rICBM")
                        .tag("tankweapon", "icbm")
                        .build()
        );
    }

}

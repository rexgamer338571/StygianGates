package dev.ng5m.stygiangates;

import dev.ng5m.stygiangates.command.CommandProcedure;
import dev.ng5m.stygiangates.command.CommandSpawn;
import dev.ng5m.stygiangates.event.*;
import dev.ng5m.stygiangates.util.ScoreboardUtil;
import dev.ng5m.stygiangates.util.Updater;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Math.ceil;

public final class StygianGates extends JavaPlugin {
    public static String NEWEST_VER = "1.5.1";
    private static StygianGates instance;
    public static final Map<UUID, FastBoard> boards = new HashMap<>();


    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerCommandEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveHandler(), this);
        getServer().getPluginManager().registerEvents(new TankEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);

        getConfig().set("spawn", new Location(Bukkit.getWorlds().get(0), 0, 25, 300, 0, 0));

        saveConfig();

        Objects.requireNonNull(getCommand("procedure")).setExecutor(new CommandProcedure());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new CommandSpawn());

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : boards.values()) {
                ScoreboardUtil.update(board);
            }
        }, 0, 20);

        Updater.update();
    }

    public static int safeInt(String key, int default_) {
        return getInstance().getConfig().contains(key) ? getInstance().getConfig().getInt(key) : default_;
    }

    public static StygianGates getInstance() {
        return instance;
    }
}

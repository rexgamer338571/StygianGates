package dev.ng5m.stygiangates;

import dev.ng5m.stygiangates.command.*;
import dev.ng5m.stygiangates.event.*;
import dev.ng5m.stygiangates.util.ScoreboardUtil;
import dev.ng5m.stygiangates.util.Updater;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class StygianGates extends JavaPlugin {
    public static String NEWEST_VER = "1.5.4.0";
    private static StygianGates instance;
    public static final Map<UUID, FastBoard> boards = new HashMap<>();
    public static final List<String> DEFAULT_SCOREBOARD = Arrays.asList(
            "§4§l==========================",
            "§5§l- • {playerName} • -",
            "",
            "§4§l☠ Deaths: §r{playerDeaths}",
            "§4§lWools: §r{playerWools}",
            "",
            "§6§lGold: §r{playerGold}",
            "",
            "§5§lTime Played: §r{playerPlaytime}",
            "§4§l=========================="
    );


    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerCommandEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveHandler(), this);
        getServer().getPluginManager().registerEvents(new TankEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);

        getConfig().set("spawn", new Location(Bukkit.getWorlds().get(0), 0, 25, 300, 0, 0));
        getConfig().set("scoreboard.enabled", true);
        getConfig().set("scoreboard.display", DEFAULT_SCOREBOARD);

        saveConfig();

        Objects.requireNonNull(getCommand("procedure")).setExecutor(new CommandProcedure());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new CommandSpawn());
        Objects.requireNonNull(getCommand("version")).setExecutor(new CommandVersion());

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : boards.values()) {
                ScoreboardUtil.update(board);
            }
        }, 0, 20);

        Updater.update();
    }

    public static <T> T safe(String key, T default_) {
        return getInstance().getConfig().contains(key) ? (T) getInstance().getConfig().get(key) : default_;
    }

    public static StygianGates getInstance() {
        return instance;
    }
}

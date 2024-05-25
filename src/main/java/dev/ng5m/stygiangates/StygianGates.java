package dev.ng5m.stygiangates;

import dev.ng5m.stygiangates.util.Updater;
import org.bukkit.plugin.java.JavaPlugin;

public final class StygianGates extends JavaPlugin {
    public static String NEWEST_VER = "1.0.0";
    private static StygianGates instance;

    @Override
    public void onEnable() {
        instance = this;

        Updater.update();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static StygianGates getInstance() {
        return instance;
    }
}

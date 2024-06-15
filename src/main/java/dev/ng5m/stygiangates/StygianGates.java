package dev.ng5m.stygiangates;

import dev.ng5m.stygiangates.command.CommandProcedure;
import dev.ng5m.stygiangates.event.PlayerCommandEvent;
import dev.ng5m.stygiangates.event.PlayerMoveHandler;
import dev.ng5m.stygiangates.event.TankEvents;
import dev.ng5m.stygiangates.util.Updater;
import org.bukkit.plugin.java.JavaPlugin;

public final class StygianGates extends JavaPlugin {
    public static String NEWEST_VER = "1.3.2";
    private static StygianGates instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerCommandEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveHandler(), this);
        getServer().getPluginManager().registerEvents(new TankEvents(), this);

        getCommand("procedure").setExecutor(new CommandProcedure());

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

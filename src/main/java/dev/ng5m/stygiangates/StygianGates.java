package dev.ng5m.stygiangates;

import dev.ng5m.stygiangates.command.CommandProcedure;
import dev.ng5m.stygiangates.event.PlayerCommandEvent;
import dev.ng5m.stygiangates.event.PlayerJoinHandler;
import dev.ng5m.stygiangates.event.PlayerMoveHandler;
import dev.ng5m.stygiangates.event.TankEvents;
import dev.ng5m.stygiangates.util.Updater;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class StygianGates extends JavaPlugin {
    public static String NEWEST_VER = "1.4.1.1";
    private static StygianGates instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new PlayerCommandEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveHandler(), this);
        getServer().getPluginManager().registerEvents(new TankEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);

        Objects.requireNonNull(getCommand("procedure")).setExecutor(new CommandProcedure());

        Updater.update();
    }

    public static StygianGates getInstance() {
        return instance;
    }
}

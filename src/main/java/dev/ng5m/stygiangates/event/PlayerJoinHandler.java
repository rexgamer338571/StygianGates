package dev.ng5m.stygiangates.event;

import dev.ng5m.stygiangates.StygianGates;
import dev.ng5m.stygiangates.util.FakePlayerUtil;
import dev.ng5m.stygiangates.util.NPCUtil;
import dev.ng5m.stygiangates.util.ScoreboardUtil;
import fr.mrmicky.fastboard.FastBoard;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for (String key : FakePlayerUtil.fakePlayers.keySet()) {
            FakePlayerUtil.addForPlayer(FakePlayerUtil.fakePlayers.get(key), player);
        }

        FastBoard board = new FastBoard(player);
        board.updateTitle("§5§l- ★ Stygian Gates ★ -");

        for (ServerPlayer npc : NPCUtil.NPCS) {
            NPCUtil.sendPackets(player, npc);
        }

        StygianGates.boards.put(player.getUniqueId(), board);
    }

}

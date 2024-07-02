package dev.ng5m.stygiangates.util;

import dev.ng5m.stygiangates.StygianGates;
import dev.ng5m.stygiangates.npc.NPC;
import net.minecraft.network.chat.Component;
import org.bukkit.Bukkit;;
import org.bukkit.entity.Player;

import java.util.*;

public class NPCUtil {
    public static List<NPC> NPCS = new ArrayList<>();

    public static void add(NPC npc) {
        NPCS.add(npc);

        StygianGates.getInstance().getConfig().set("npcs." + npc.getStringUUID(), npc);

        for (Player p : Bukkit.getOnlinePlayers()) {
            npc.sendPackets(p);
        }
    }

    public static void remove(String name) {
        for (NPC npc : NPCS) {
            if (npc.getName().contains(Component.literal(name))) {
                NPCS.remove(npc);

                StygianGates.getInstance().getConfig().set("npcs." + npc.getStringUUID(), null);
            }
        }
    }

}

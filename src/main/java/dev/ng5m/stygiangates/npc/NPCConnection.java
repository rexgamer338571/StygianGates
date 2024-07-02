package dev.ng5m.stygiangates.npc;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;
import org.jetbrains.annotations.NotNull;

/**
 * Thanks to Foxikle :3
 * <p>
 * <a href="https://discord.com/invite/Arp6A6ue3u">Fox Den</a>
 * <a href="https://github.com/Foxikle/CustomNPCs/blob/master/v1_20_R3/src/main/java/dev/foxikle/customnpcs/versions/FakeConnection_v1_20_R3.java">Original</a>
 */
public class NPCConnection extends Connection {
    public NPCConnection(PacketFlow side) {
        super(side);
    }

    @Override
    public void setListener(@NotNull PacketListener listener) {}
}

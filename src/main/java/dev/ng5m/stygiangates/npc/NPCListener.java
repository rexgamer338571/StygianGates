package dev.ng5m.stygiangates.npc;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Thanks to Foxikle :3
 * <p>
 * <a href="https://discord.com/invite/Arp6A6ue3u">Fox Den</a>
 * <a href="https://github.com/Foxikle/CustomNPCs/blob/master/v1_20_R3/src/main/java/dev/foxikle/customnpcs/versions/FakeListener_v1_20_R3.java">Original</a>
 */
public class NPCListener extends ServerGamePacketListenerImpl {
    public NPCListener(MinecraftServer server, Connection connection, ServerPlayer npc) {
        super(server, connection, npc, CommonListenerCookie.createInitial(npc.gameProfile));
    }

    @Override
    public void send(@NotNull Packet<?> packet) {}
}

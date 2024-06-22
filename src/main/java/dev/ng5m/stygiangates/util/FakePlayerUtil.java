package dev.ng5m.stygiangates.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.ng5m.stygiangates.StygianGates;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class FakePlayerUtil {

    public static void addFakePlayer(String playerName) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            CraftPlayer craftPlayer = (CraftPlayer) p;
            ServerPlayer nmsPlayer = craftPlayer.getHandle();
            ServerGamePacketListenerImpl connection = nmsPlayer.connection;

            ServerLevel world = ((CraftWorld) p.getWorld()).getHandle();
            MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), playerName);

            gameProfile.getProperties().removeAll("textures");
            gameProfile.getProperties().put("textures", getSkin(playerName));

            ServerPlayer fakePlayer = new ServerPlayer(nmsServer, world, gameProfile, ClientInformation.createDefault());

            fakePlayer.setPosRaw(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());

            fakePlayer.connection = nmsPlayer.connection;

            connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, fakePlayer));
            connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, fakePlayer));
            connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, fakePlayer));
        }

    }

    private static Property getSkin(String name) {
        Property r = null;

        try {
            String UUID = "eecec4558813427182234b28d98bc706";
            JSONParser parser = new JSONParser();

            try {
                InputStream is1 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream();
                String out1 = IOUtils.toString(is1, StandardCharsets.UTF_8);

                JSONObject obj = (JSONObject) parser.parse(out1);
                UUID = obj.get("id").toString();
            } catch (NullPointerException | ParseException | IOException e) {
                StygianGates.getInstance().getLogger().warning("Could not get skin");
            }

            InputStream is = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUID + "?unsigned=false").openStream();
            String out2 = IOUtils.toString(is, StandardCharsets.UTF_8);

            JSONObject obj2 = (JSONObject) parser.parse(out2);
            JSONArray array = (JSONArray) obj2.get("properties");
            JSONObject prop = (JSONObject) array.get(0);

            r = new Property("textures", prop.get("value").toString(), prop.get("signature").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

}

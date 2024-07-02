package dev.ng5m.stygiangates.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.ng5m.stygiangates.StygianGates;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import java.util.Arrays;
import java.util.UUID;

import static dev.ng5m.stygiangates.util.PacketUtil.send;

public class NPC extends ServerPlayer {

    public NPC(String name, String skinOf, Location location) {
        super(
                ((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) location.getWorld()).getHandle(),
                new GameProfile(UUID.randomUUID(), validateName(name)),
                ClientInformation.createDefault()
        );

        this.gameProfile.getProperties().removeAll("textures");
        this.gameProfile.getProperties().put("textures", getSkin(validateName(skinOf)));

        super.connection = new NPCListener(server, new NPCConnection(PacketFlow.CLIENTBOUND), this);

        this.setPosRaw(location.getX(), location.getY(), location.getZ());
    }

    public void sendPackets(Player p) {
        send(p, new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, this));
        send(p, new ClientboundAddEntityPacket(this));
        send(p, new ClientboundRotateHeadPacket(this, (byte) (this.getBukkitYaw() * 256 / 360)));

        super.getEntityData().set(new EntityDataAccessor<>(6, EntityDataSerializers.POSE), Pose.SLEEPING);
        super.getEntityData().set(net.minecraft.world.entity.player.Player.DATA_PLAYER_MODE_CUSTOMISATION, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
    }

    private static String validateName(String s) {
        if (s.length() > 16) {
            return s.substring(0, 16);
        }

        return s;
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
                StygianGates.getInstance().getLogger().warning("Failed getSkin for " + name);
            }

            InputStream is = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUID + "?unsigned=false").openStream();
            String out2 = IOUtils.toString(is, StandardCharsets.UTF_8);

            JSONObject obj2 = (JSONObject) parser.parse(out2);
            JSONArray array = (JSONArray) obj2.get("properties");
            JSONObject prop = (JSONObject) array.get(0);

            r = new Property("textures", prop.get("value").toString(), prop.get("signature").toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return r;
    }
}

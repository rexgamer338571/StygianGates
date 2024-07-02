package dev.ng5m.stygiangates.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.ng5m.stygiangates.StygianGates;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static dev.ng5m.stygiangates.util.PacketUtil.send;

public class NPCUtil {
    public static List<ServerPlayer> NPCS = new ArrayList<>();

    public static ServerPlayer create(String name, String skinOf, Location location) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), validateName(name));

        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", getSkin(validateName(skinOf)));

        ServerPlayer npc = new ServerPlayer(nmsServer, nmsWorld, gameProfile, ClientInformation.createDefault());

        npc.setPosRaw(location.getX(), location.getY(), location.getZ());

        return npc;
    }

    public static void sendPackets(Player p, ServerPlayer npc) {
        SynchedEntityData dataWatcher = npc.getEntityData();

        byte bitmask = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;

        dataWatcher.set(EntityDataSerializers.BYTE.createAccessor(16), bitmask);

        try {
            Field poseField = Entity.class.getDeclaredField("as");
            poseField.setAccessible(true);

            EntityDataAccessor<Pose> POSE = (EntityDataAccessor<Pose>) poseField.get(null);

            dataWatcher.set(POSE, Pose.SLEEPING);
        } catch (Exception x) {
            throw new RuntimeException(x);
        }

        send(p, new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
        send(p, new ClientboundAddEntityPacket(npc));
        send(p, new ClientboundRotateHeadPacket(npc, (byte) (npc.getBukkitYaw() * 256 / 360)));
        send(p, new ClientboundSetEntityDataPacket(npc.getId(), Objects.requireNonNull(dataWatcher.packDirty())));
    }

    public static void add(ServerPlayer npc) {
        NPCS.add(npc);

        StygianGates.getInstance().getConfig().set("npcs." + npc.getStringUUID(), npc);

        for (Player p : Bukkit.getOnlinePlayers()) {
            sendPackets(p, npc);
        }
    }

    public static void remove(String name) {
        for (ServerPlayer npc : NPCS) {
            if (npc.getName().contains(Component.literal(name))) {
                NPCS.remove(npc);

                StygianGates.getInstance().getConfig().set("npcs." + npc.getStringUUID(), null);
            }
        }
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

package dev.ng5m.stygiangates.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.ng5m.stygiangates.StygianGates;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Updater {

    public static void update() {
        try {
            HttpURLConnection connCheck = (HttpURLConnection) new URL("https://api.github.com/repos/rexgamer338571/StygianGates/releases/latest").openConnection();

            String newestVer;

            try (InputStream is = connCheck.getInputStream(); Scanner scanner = new Scanner(is)) {
                String response = scanner.useDelimiter("\\A").next();

                JsonObject releaseInfo = JsonParser.parseString(response).getAsJsonObject();
                newestVer = releaseInfo.get("tag_name").getAsString();
            }

            if (!newestVer.equals(StygianGates.NEWEST_VER)) {
                Bukkit.broadcast(Component.text("No new version of StygianGates is available.").color(TextColor.color(0, 255, 0)), "minecraft.command.op");
                return;
            }

            String updateURL = "https://github.com/rexgamer338571/StygianGates/releases/download/" + newestVer + "/sg-" + newestVer + ".jar";

            try (BufferedInputStream is = new BufferedInputStream(new URL(updateURL).openStream())) {
                FileOutputStream fos = new FileOutputStream(StygianGates.getInstance().getDataFolder() + File.separator + "plugins" + File.separator + "sg-" + newestVer + ".jar");
                final byte[] dataBuffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = is.read(dataBuffer, 0, 1024)) != -1) {
                    fos.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException x) {
                x.printStackTrace();
            }
        } catch (Exception x_) {
            x_.printStackTrace();
        }

    }

}

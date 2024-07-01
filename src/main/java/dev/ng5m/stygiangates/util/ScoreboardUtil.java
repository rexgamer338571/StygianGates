package dev.ng5m.stygiangates.util;


import dev.ng5m.stygiangates.StygianGates;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import static java.lang.Math.ceil;

public class ScoreboardUtil {
    static final int SECONDS_IN_MINUTE = 60;
    static final int SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;
    static final int SECONDS_IN_DAY = 24 * SECONDS_IN_HOUR;
    static final int SECONDS_IN_MONTH = (int) (30.44 * SECONDS_IN_DAY);

    public static void update(FastBoard board) {
        Player p = board.getPlayer();

        board.updateLines(
                "§4§l==========================",
                center("§5§l- • " + p.getName() + " • -"),
                "",
                "§4§l☠ Deaths: §r" + p.getStatistic(Statistic.DEATHS),
                "§4§lWools: §r" + StygianGates.safeInt(p.getUniqueId() + "woolsObtained", 0),
                "",
                "§6§lGold: §r" + StygianGates.safeInt(p.getUniqueId() + "gold", 0),
                "",
                "§5§lTime Played: §r" + formatPlaytime(p.getStatistic(Statistic.PLAY_ONE_MINUTE)),
                "§4§l=========================="
        );
    }

    public static String center(String s) {
        String spaces = " ".repeat((30 - s.length()) / 2);
        return spaces + s + spaces;
    }

    public static String formatPlaytime(int ticks) {
        int totalSeconds = ticks / 20;

        int months = totalSeconds / SECONDS_IN_MONTH;
        totalSeconds %= SECONDS_IN_MONTH;
        int days = totalSeconds / SECONDS_IN_DAY;
        totalSeconds %= SECONDS_IN_DAY;
        int hours = totalSeconds / SECONDS_IN_HOUR;
        totalSeconds %= SECONDS_IN_HOUR;
        int minutes = totalSeconds / SECONDS_IN_MINUTE;
        int seconds = totalSeconds % SECONDS_IN_MINUTE;

        return (months == 0 ? "" : months + "mt ") + (days == 0 ? "" : days + "d ") + (hours == 0 ? "" : hours + "h ") + (minutes == 0 ? "" : minutes + "m ") + (seconds == 0 ? "" : seconds + "s");
    }

    private static int div(double dividend, double divisor) {
        return (int) ceil(dividend / divisor);
    }

}

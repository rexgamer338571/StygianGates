package dev.ng5m.stygiangates.util;


import dev.ng5m.stygiangates.StygianGates;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;

public class ScoreboardUtil {
    static final int SECONDS_IN_MINUTE = 60;
    static final int SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;
    static final int SECONDS_IN_DAY = 24 * SECONDS_IN_HOUR;
    static final int SECONDS_IN_MONTH = (int) (30.44 * SECONDS_IN_DAY);

    public static void update(FastBoard board) {
        if (!(boolean) StygianGates.safe("scoreboard.enabled", true)) return;

        Player p = board.getPlayer();

        board.updateLines(
                placeholders(p, StygianGates.safe("scoreboard.display", StygianGates.DEFAULT_SCOREBOARD))
        );
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

    public static String placeholders(Player p, String input) {
        return input
                .replace("{playerName}", p.getName())
                .replace("{playerDeaths}", ""+p.getStatistic(Statistic.DEATHS))
                .replace("{playerWools}", ""+(int)StygianGates.safe(p.getUniqueId() + "woolsObtained", 0))
                .replace("{playerGold}", ""+(int)StygianGates.safe(p.getUniqueId() + "gold", 0))
                .replace("{playerPlaytime}", formatPlaytime(p.getStatistic(Statistic.PLAY_ONE_MINUTE)));
    }

    public static List<String> placeholders(Player p, List<String> input) {
        List<String> a = new ArrayList<>();

        for (String s : input) a.add(placeholders(p, s));

        return a;
    }

    private static int div(double dividend, double divisor) {
        return (int) ceil(dividend / divisor);
    }

}

package dev.ng5m.stygiangates.util;


import dev.ng5m.stygiangates.StygianGates;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import static java.lang.Math.ceil;

public class ScoreboardUtil {
    private static final double SECOND_TICKS = 20;
    private static final double MINUTE_TICKS = SECOND_TICKS * 60;
    private static final double HOUR_TICKS = MINUTE_TICKS * 60;
    private static final double DAY_TICKS = HOUR_TICKS * 24;
    private static final double MONTH_TICKS = DAY_TICKS * 30.436875;

    public static void update(FastBoard board) {
        Player p = board.getPlayer();

        board.updateLines(
                "§4§l===============",
                "§5§l- • " + p.getName() + " • -",
                "",
                "§4§l☠ Deaths: §r" + p.getStatistic(Statistic.DEATHS),
                "§4§lWools: §r" + StygianGates.safeInt(p.getUniqueId() + "woolsObtained", 0),
                "",
                "§6§lGold: §r" + StygianGates.safeInt(p.getUniqueId() + "gold", 0),
                "",
                "§5§lTime Played: §r" + formatPlaytime(p.getStatistic(Statistic.PLAY_ONE_MINUTE)),
                "§4§l==============="
        );
    }

    public static String formatPlaytime(int ticks) {
        StringBuilder stringBuilder = new StringBuilder();

        int seconds = div(ticks, SECOND_TICKS) % 60;

        int minutes_ = div(ticks, MINUTE_TICKS) % 60;
        int minutes = seconds == 0 && ticks <= MINUTE_TICKS ? minutes_ + 1 : minutes_;

        int hours_ = div(ticks, HOUR_TICKS) % 24;
        int hours = minutes == 0 && ticks <= HOUR_TICKS ? hours_ + 1 : hours_;

        int days_ = div(ticks, DAY_TICKS) % 30;
        int days = hours == 0 && ticks <= DAY_TICKS ? days_ + 1 : days_;

        int months_ = div(ticks, MONTH_TICKS);
        int months = days == 0 && ticks <= MONTH_TICKS ? months_ + 1 : months_;

        stringBuilder.append(months == 0 ? "" : months + "mt ").append(days == 0 ? "" : days + "d ").append(hours == 0 ? "" : hours + "h ").append(minutes == 0 ? "" : minutes + "m ").append(seconds == 0 ? "" : seconds + "s ");

        return stringBuilder.toString();
    }

    private static void idk(int i, int mod, StringBuilder sb, double d, String s) {
        if (i >= d) sb.append(div(i, d) % mod).append(s).append(" ");
    }

    private static int div(double dividend, double divisor) {
        return (int) ceil(dividend / divisor);
    }

}

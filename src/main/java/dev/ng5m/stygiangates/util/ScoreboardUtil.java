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
                "§6§lGold:§r" + StygianGates.safeInt(p.getUniqueId() + "gold", 0),
                "",
                "§5§lTime Played:§r" + formatPlaytime(p.getStatistic(Statistic.PLAY_ONE_MINUTE)),
                "§4§l==============="
        );
    }

    public static String formatPlaytime(int ticks) {
        StringBuilder stringBuilder = new StringBuilder();

        idk(ticks, stringBuilder, MONTH_TICKS, "mt");
        idk(ticks, stringBuilder, DAY_TICKS, "d");
        idk(ticks, stringBuilder, HOUR_TICKS, "h");
        idk(ticks, stringBuilder, MINUTE_TICKS, "m");
        idk(ticks, stringBuilder, SECOND_TICKS, "s");

        return stringBuilder.toString();
    }

    private static void idk(int i, StringBuilder sb, double d, String s) {
        if (i >= d) sb.append(div(i, d)).append(s).append(" ");
    }

    private static int div(double dividend, double divisor) {
        return (int) ceil(dividend / divisor);
    }

}

package dev.ng5m.stygiangates.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardUtil {
    public static void setForPlayer(Player p) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("ServerName", Criteria.DUMMY, Component.text("- ★ Stygian Gates ★ -").color(TextColor.color(0xaa00aa)).decorate(TextDecoration.BOLD));

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score username = objective.getScore("&5&l- • " + p.getName() + " • -");

        Team deaths = scoreboard.registerNewTeam("playerDeaths");
        deaths.addEntry("§4§l☠ Deaths: §r");
        deaths.suffix(Component.text(p.getStatistic(Statistic.DEATHS)));

        objective.getScore("playerDeaths").setScore(14);

    }

    public static void update(Player p) {
        Scoreboard scoreboard = p.getScoreboard();

        scoreboard.getTeam("playerDeaths").suffix(Component.text(p.getStatistic(Statistic.DEATHS)));
    }

}

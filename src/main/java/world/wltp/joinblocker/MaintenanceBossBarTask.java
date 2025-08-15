package world.wltp.joinblocker;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.*;
import java.util.List;

public class MaintenanceBossBarTask extends BukkitRunnable {

    private final Main plugin;
    private final List<String> allowedNames;
    private final BossBar bossBar;

    public MaintenanceBossBarTask(Main plugin) {
        this.plugin = plugin;
        this.allowedNames = plugin.getConfig().getStringList("allowed-names");
        this.bossBar = Bukkit.createBossBar("서버 점검 상태", BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setVisible(true);

        for (Player p : Bukkit.getOnlinePlayers()) {
            addPlayer(p);
        }
    }

    public void addPlayer(Player player) {
        if (!bossBar.getPlayers().contains(player)) {
            bossBar.addPlayer(player);
        }
    }

    @Override
    public void run() {
        String tz = plugin.getConfig().getString("timezone", "Asia/Seoul");
        ZoneId zone = ZoneId.of(tz);

        ZonedDateTime now = ZonedDateTime.now(zone);

        String startDateStr = plugin.getConfig().getString("maintenance.start-date", "1970-01-01");
        String endDateStr   = plugin.getConfig().getString("maintenance.end-date", "9999-12-31");
        String startTimeStr = plugin.getConfig().getString("maintenance.start-time", "00:00");
        String endTimeStr   = plugin.getConfig().getString("maintenance.end-time", "00:00");

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate   = LocalDate.parse(endDateStr);
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime   = LocalTime.parse(endTimeStr);

        ZonedDateTime startDateTime = ZonedDateTime.of(startDate, startTime, zone);
        ZonedDateTime endDateTime   = ZonedDateTime.of(endDate, endTime, zone);

        boolean beforeMaintenance = now.isBefore(startDateTime);
        boolean maintenanceActive = !now.isBefore(startDateTime) && !now.isAfter(endDateTime);

        if (!beforeMaintenance && !maintenanceActive) {
            bossBar.removeAll();
            cancel();
            return;
        }

        Duration duration;
        String colorCode;
        String title;

        if (beforeMaintenance) {
            duration = Duration.between(now, startDateTime);
            colorCode = "&a";
            bossBar.setColor(BarColor.GREEN);
            title = colorCode + "서버 점검 시작까지: " +
                    duration.toHoursPart() + "시간 " +
                    duration.toMinutesPart() + "분 " +
                    duration.toSecondsPart() + "초";
        } else {
            duration = Duration.between(now, endDateTime);
            colorCode = "&f";
            bossBar.setColor(BarColor.YELLOW);
            title = colorCode + "서버 점검 중 | 종료까지: " +
                    duration.toHoursPart() + "시간 " +
                    duration.toMinutesPart() + "분 " +
                    duration.toSecondsPart() + "초";
        }

        bossBar.setProgress(1.0);
        bossBar.setTitle(org.bukkit.ChatColor.translateAlternateColorCodes('&', title));

        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayer(player);
        }
    }
}

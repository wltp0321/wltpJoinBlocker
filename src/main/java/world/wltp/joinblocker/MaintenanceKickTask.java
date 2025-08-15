package world.wltp.joinblocker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.*;
import java.util.List;

public class MaintenanceKickTask extends BukkitRunnable {

    private final Main plugin;

    public MaintenanceKickTask(Main plugin) {
        this.plugin = plugin;
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

        boolean maintenanceActive = !now.isBefore(startDateTime) && !now.isAfter(endDateTime);
        if (!maintenanceActive) return;

        List<String> allowedNames = plugin.getConfig().getStringList("allowed-names");

        for (Player player : Bukkit.getOnlinePlayers()) {
            boolean isAllowed = allowedNames.stream()
                    .anyMatch(name -> name.trim().equalsIgnoreCase(player.getName().trim()));
            if (!isAllowed) {
                Duration remaining = Duration.between(now, endDateTime);
                long hours = remaining.toHours();
                long minutes = remaining.toMinutesPart();
                long seconds = remaining.toSecondsPart();

                String kickMessageBase = plugin.getConfig().getString("kick-message",
                        "&6서버 점검 중!\n&e점검 종료까지: &a%hours%시간 %minutes%분 %seconds%초\n잠시 후 다시 접속해주세요!");

                String message = kickMessageBase
                        .replace("%hours%", String.valueOf(hours))
                        .replace("%minutes%", String.valueOf(minutes))
                        .replace("%seconds%", String.valueOf(seconds));

                player.kickPlayer(org.bukkit.ChatColor.translateAlternateColorCodes('&', message));
            }
        }
    }
}

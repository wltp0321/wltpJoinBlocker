package world.wltp.joinblocker;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main plugin = Main.getInstance();
        String playerName = event.getPlayer().getName().trim().toLowerCase();
        List<String> allowedNames = plugin.getConfig().getStringList("allowed-names");

        boolean isAllowed = allowedNames.stream()
                .anyMatch(name -> name.trim().equalsIgnoreCase(playerName));

        if (!isAllowed && plugin.getBossBarTask() != null) {
            plugin.getBossBarTask().addPlayer(event.getPlayer());
        }
    }



    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Main plugin = Main.getInstance();

        List<String> allowedNames = plugin.getConfig().getStringList("allowed-names");

        // 점검 시작/종료 날짜 및 시간
        String startDateStr = plugin.getConfig().getString("maintenance.start-date", "1970-01-01");
        String endDateStr   = plugin.getConfig().getString("maintenance.end-date", "9999-12-31");
        String startTimeStr = plugin.getConfig().getString("maintenance.start-time", "00:00");
        String endTimeStr   = plugin.getConfig().getString("maintenance.end-time", "00:00");

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate   = LocalDate.parse(endDateStr);
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime   = LocalTime.parse(endTimeStr);

        ZoneId zone = ZoneId.of("Asia/Seoul");
        ZonedDateTime startDateTime = ZonedDateTime.of(startDate, startTime, zone);
        ZonedDateTime endDateTime   = ZonedDateTime.of(endDate, endTime, zone);
        ZonedDateTime now = ZonedDateTime.now(zone);

        boolean maintenanceActive = !now.isBefore(startDateTime) && now.isBefore(endDateTime);

        if (maintenanceActive && !allowedNames.contains(event.getName())) {
            String kickMessageBase = plugin.getConfig().getString("kick-message",
                    "&6&l서버 점검 중!\n&e점검 시간: &a%startDate% &e~ &a%endDate%\n잠시 후 다시 접속해주세요!");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String message = kickMessageBase
                    .replace("%startDate%", startDateTime.format(formatter))
                    .replace("%endDate%", endDateTime.format(formatter));
            message = ChatColor.translateAlternateColorCodes('&', message);

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, message);
        }
    }
}

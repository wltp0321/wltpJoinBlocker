package world.wltp.joinblocker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("joinblocker.reload")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        Main plugin = Main.getInstance();
        plugin.reloadConfig();

        // 기존 Task 취소
        if (plugin.getKickTask() != null) plugin.getKickTask().cancel();
        if (plugin.getBossBarTask() != null) plugin.getBossBarTask().cancel();

        // 새 Task 실행
        plugin.setKickTask(new MaintenanceKickTask(plugin));
        plugin.getKickTask().runTaskTimer(plugin, 0L, 20L);

        plugin.setBossBarTask(new MaintenanceBossBarTask(plugin));
        plugin.getBossBarTask().runTaskTimer(plugin, 0L, 20L);

        sender.sendMessage("§aJoinBlocker config reloaded!");
        return true;
    }
}

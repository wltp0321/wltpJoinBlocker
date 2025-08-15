package world.wltp.joinblocker;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    private MaintenanceKickTask kickTask;
    private MaintenanceBossBarTask bossBarTask;

    public static Main getInstance() { return instance; }

    public MaintenanceKickTask getKickTask() { return kickTask; }
    public void setKickTask(MaintenanceKickTask task) { this.kickTask = task; }

    public MaintenanceBossBarTask getBossBarTask() { return bossBarTask; }
    public void setBossBarTask(MaintenanceBossBarTask task) { this.bossBarTask = task; }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        kickTask = new MaintenanceKickTask(this);
        kickTask.runTaskTimer(this, 0L, 20L);

        bossBarTask = new MaintenanceBossBarTask(this);
        bossBarTask.runTaskTimer(this, 0L, 20L);

        getCommand("reloadjoinblocker").setExecutor(new ReloadCommand());
    }
}

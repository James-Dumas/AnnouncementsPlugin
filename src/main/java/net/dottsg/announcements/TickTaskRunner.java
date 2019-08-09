package net.dottsg.announcements;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TickTaskRunner
{
    private BukkitRunnable runnable;
    private int interval;

    private Plugin plugin = Announcements.getPlugin(Announcements.class);

    public TickTaskRunner(BukkitRunnable runnable, int interval)
    {
        this.runnable = runnable;
        this.interval = interval;
    }

    public void start()
    {
        runnable.runTaskTimer(plugin, interval / 2, interval);
    }

    public void stop()
    {
        runnable.cancel();
    }
}

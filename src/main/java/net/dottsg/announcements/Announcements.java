package net.dottsg.announcements;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public final class Announcements extends JavaPlugin
{
    public static AnnouncementManager announcementManager;

    private static final int DATA_REFRESH_INTERVAL = 1800; // interval in seconds between when the data should be completely re-read from the database
    private static Timer timer = new Timer();

    @Override
    public void onEnable()
    {
        // Plugin startup logic
        log("Starting up");
        announcementManager = new AnnouncementManager();
        announcementManager.setRandomInterval(this.getConfig().getInt("random-interval"));
        if(!AnnouncementManager.SAVE_LOCATION.exists())
        {
            AnnouncementManager.SAVE_LOCATION.mkdirs();
        }

        log("Loading announcements");
        if(announcementManager.loadAnnouncements())
        {
            if(!announcementManager.announcements.isEmpty())
            {
                log(String.format("Loaded %d announcement%s successfully", announcementManager.announcements.size(), announcementManager.announcements.size() == 1 ? "" : "s"));
            }
        }
        else
        {
            log("Failed to load one or more announcement!");
        }

        log("Registering Events");
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        log("Registering commands");
        this.getCommand("addrandom").setExecutor(new CommandAddRandom());
        this.getCommand("addfixed").setExecutor(new CommandAddFixed());
        this.getCommand("addonlogin").setExecutor(new CommandAddOnLogin());
        this.getCommand("remove").setExecutor(new CommandRemove());
        this.getCommand("changemessage").setExecutor(new CommandChangeMessage());
        this.getCommand("list").setExecutor(new CommandList());
        this.getCommand("setrandominterval").setExecutor(new CommandSetRandomInterval());

        // schedule database refreshing
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                log("Reloading announcements");
                if(announcementManager.loadAnnouncements())
                {
                    if(!announcementManager.announcements.isEmpty())
                    {
                        log(String.format("Loaded %d announcement%s successfully", announcementManager.announcements.size(), announcementManager.announcements.size() == 1 ? "" : "s"));
                    }
                }
                else
                {
                    log("Failed to load one or more announcement!");
                }
            }
        }, DATA_REFRESH_INTERVAL * 1000, DATA_REFRESH_INTERVAL * 1000);
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
        timer.cancel();
        announcementManager.stopTasks();
        log("Saving announcements");
        announcementManager.saveAllAnnouncements();
        log("Shutting down");
    }

    public static void log(String msg)
    {
        System.out.println("[Announcements]: " + msg);
    }
}

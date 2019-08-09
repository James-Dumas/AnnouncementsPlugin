package net.dottsg.announcements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;

public class AnnouncementManager
{
    public static final File SAVE_LOCATION = new File(Announcements.getPlugin(Announcements.class).getDataFolder(), "announcements");

    private static File getDatabaseFile(Announcement an)
    {
        return new File(SAVE_LOCATION, an.getName() + ".json");
    }

    public Map<String, Announcement> announcements = new HashMap<>();
    public List<AnnouncementRandom> announcementsRandom = new LinkedList<>();
    public List<AnnouncementFixed> announcementsFixed = new LinkedList<>();
    public List<AnnouncementOnLogin> announcementsOnLogin = new LinkedList<>();

    private Plugin plugin = Announcements.getPlugin(Announcements.class);
    private TickTaskRunner randomRunner = null;

    private Map<String, TickTaskRunner> fixedRunners = new HashMap<>();

    private Random random = new Random();
    private Map<String, Integer> randomProb = new HashMap<>();
    private LinkedList<String> recents = new LinkedList<>();    // Queue

    public void stopTasks()
    {
        if(randomRunner != null)
        {
            randomRunner.stop();
        }

        for(TickTaskRunner t : fixedRunners.values())
        {
            t.stop();
        }
    }

    public void addAnnouncement(Announcement an)
    {
        announcements.put(an.getName(), an);
        if(an instanceof AnnouncementRandom)
        {
            announcementsRandom.add((AnnouncementRandom) an);
            randomProb.put(an.getName(), 1);
        }

        if(an instanceof AnnouncementFixed)
        {
            announcementsFixed.add((AnnouncementFixed) an);
            scheduleFixedAnnouncement((AnnouncementFixed) an);
        }

        if(an instanceof AnnouncementOnLogin)
        {
            announcementsOnLogin.add((AnnouncementOnLogin) an);
        }
    }

    public Announcement getAnnouncement(String name)
    {
        if(announcements.containsKey(name))
        {
            return announcements.get(name);
        }

        return null;
    }

    public Announcement removeAnnouncement(String name)
    {
        Announcement an = null;
        if(announcements.containsKey(name))
        {
            an = announcements.get(name);
            announcements.remove(name);
            boolean random = announcementsRandom.remove(an);
            boolean fixed = announcementsFixed.remove(an);
            announcementsOnLogin.remove(an);

            if(random)
            {
                randomProb.remove(an.getName());
                recents.remove(an.getName());
            }

            if(fixed)
            {
                fixedRunners.get(an.getName()).stop();
                fixedRunners.remove(an.getName());
            }
        }

        return an;
    }

    public void setRandomInterval(int interval)
    {
        plugin.getConfig().set("random-interval", interval);
        plugin.saveConfig();

        if(randomRunner != null)
        {
            randomRunner.stop();
        }

        randomRunner = new TickTaskRunner(new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(announcementsRandom.size() > 0)
                {
                    showRandomAnnouncement();
                }
            }
        }, interval);

        randomRunner.start();
    }

    public void saveAnnouncement(Announcement an)
    {
        Gson gson = new Gson();
        File file = getDatabaseFile(an);
        try
        {
            if(!file.exists())
            {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(an.getJson()));
            fileWriter.close();
        }
        catch(IOException e)
        {
            Announcements.log("Error saving announcement to file!");
        }
    }

    public void saveAllAnnouncements()
    {
        for(Announcement an : announcements.values())
        {
            saveAnnouncement(an);
        }
    }

    public void deleteAnnouncement(Announcement an)
    {
        File file = getDatabaseFile(an);
        if(!file.delete())
        {
            Announcements.log("Error deleting announcement file!");
        }
    }

    public boolean loadAnnouncements()
    {
        boolean ok = true;
        File[] files = SAVE_LOCATION.listFiles((f, s) -> s.endsWith(".json"));
        for(File file : files)
        {
            try
            {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                StringBuilder data = new StringBuilder();
                int chr;
                while((chr = bufferedReader.read()) != -1)
                {
                    data.append((char) chr);
                }

                JsonParser jsonParser = new JsonParser();
                JsonObject obj = jsonParser.parse(data.toString()).getAsJsonObject();

                addAnnouncement(Announcement.fromJson(obj));
            }
            catch(Exception e)
            {
                Announcements.log("Error loading announcement from file '" + file.getName() + "'!");
                ok = false;
            }
        }

        return ok;
    }

    private void showRandomAnnouncement()
    {
        int total = 0;
        for(int p : randomProb.values())
        {
            total += p;
        }

        int r = random.nextInt(total);

        int sum = 0;
        String name = null;
        boolean found = false;
        for(String s : randomProb.keySet())
        {
            int val = randomProb.get(s);
            sum += val;
            randomProb.put(s, ++val);
            if(!found && r < sum)
            {
                name = s;
                found = true;

            }
        }

        // update recents queue
        randomProb.remove(name);
        recents.add(name);
        while(recents.size() > (int) (0.5 * announcementsRandom.size()))
        {
            randomProb.put(recents.remove(), 1);
        }

        Announcements.getPlugin(Announcements.class).getServer().broadcastMessage(announcements.get(name).getMessage());
    }

    private void scheduleFixedAnnouncement(AnnouncementFixed an)
    {
        TickTaskRunner t = new TickTaskRunner(new BukkitRunnable()
        {
            @Override
            public void run()
            {
                plugin.getServer().broadcastMessage(an.getMessage());
            }
        }, an.getInterval());

        fixedRunners.put(an.getName(), t);
        t.start();
    }
}

package net.dottsg.announcements;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandAddFixed implements TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length >= 3)
        {
            String name = args[0];
            if(Announcements.announcementManager.getAnnouncement(name) == null)
            {
                int interval = 0;
                try
                {
                    interval = Integer.parseInt(args[1]);
                }
                catch(NumberFormatException e) {}

                if(interval <= 0)
                {
                    sender.sendMessage(ChatColor.RED + "Invalid time interval! (Must be a number greater than 0)");
                }
                else
                {
                    Announcement an = new AnnouncementFixed(name, String.join(" ", Arrays.copyOfRange(args, 2, args.length)), interval);
                    Announcements.announcementManager.addAnnouncement(an);
                    Announcements.announcementManager.saveAnnouncement(an);
                    sender.sendMessage(ChatColor.GREEN + "Announcement added!");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "There is already an announcement named '" + name + "'!");
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> list = new ArrayList<>();
        if(args.length == 2)
        {
            list.add("1200");
            list.add("6000");
            list.add("36000");
            list.add("72000");
        }

        return list;
    }
}

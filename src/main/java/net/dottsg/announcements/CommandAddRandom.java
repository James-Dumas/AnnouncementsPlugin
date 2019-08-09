package net.dottsg.announcements;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandAddRandom implements TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length >= 2)
        {
            String name = args[0];
            if(Announcements.announcementManager.getAnnouncement(name) == null)
            {
                Announcement an = new AnnouncementRandom(name, String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                Announcements.announcementManager.addAnnouncement(an);
                Announcements.announcementManager.saveAnnouncement(an);
                sender.sendMessage(ChatColor.GREEN + "Announcement added!");
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
        return new ArrayList<>();
    }
}

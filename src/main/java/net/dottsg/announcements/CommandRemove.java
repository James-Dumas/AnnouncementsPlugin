package net.dottsg.announcements;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandRemove implements TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length == 1)
        {
            String name = args[0];
            if(Announcements.announcementManager.getAnnouncement(name) != null)
            {
                Announcements.announcementManager.deleteAnnouncement(Announcements.announcementManager.removeAnnouncement(name));
                sender.sendMessage(ChatColor.GREEN + "Announcement removed!");
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "There is no announcement named '" + name + "'!");
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
        List<String> list;
        if(args.length == 1)
        {
            list = new ArrayList<>(Announcements.announcementManager.announcements.keySet());
            list.sort(String::compareToIgnoreCase);
        }
        else
        {
            list = new ArrayList<>();
        }

        return list;
    }
}

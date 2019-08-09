package net.dottsg.announcements;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandChangeMessage implements TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length >= 2)
        {
            String name = args[0];
            Announcement an = Announcements.announcementManager.getAnnouncement(name);
            if(an != null)
            {
                an.setMessage(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                Announcements.announcementManager.saveAnnouncement(an);
                sender.sendMessage(ChatColor.GREEN + "Message updated!");
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

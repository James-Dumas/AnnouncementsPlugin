package net.dottsg.announcements;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandAddOnLogin implements TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length >= 3)
        {
            String name = args[0];
            if(Announcements.announcementManager.getAnnouncement(name) == null)
            {
                boolean onFirstLogin;

                if(args[1].toLowerCase().equals("true"))
                {
                    onFirstLogin = true;
                }
                else if(args[1].toLowerCase().equals("false"))
                {
                    onFirstLogin = false;
                }
                else
                {
                    return false;
                }

                Announcement an = new AnnouncementOnLogin(name, String.join(" ", Arrays.copyOfRange(args, 2, args.length)), onFirstLogin);
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
        List<String> list = new ArrayList<>();
        if(args.length == 2)
        {
            list.add("true");
            list.add("false");
        }

        return list;
    }
}

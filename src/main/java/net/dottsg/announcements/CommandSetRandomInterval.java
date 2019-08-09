package net.dottsg.announcements;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandSetRandomInterval implements TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length == 1)
        {
            int value = 0;
            try
            {
                value = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException e) {}

            if(value <= 0)
            {
                sender.sendMessage(ChatColor.RED + "Invalid time interval! (Must be a number greater than 0)");
            }
            else
            {
                Announcements.announcementManager.setRandomInterval(value);
                sender.sendMessage(ChatColor.GREEN + "Random interval updated!");
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
        if(args.length == 1)
        {
            list.add("1200");
            list.add("6000");
            list.add("36000");
            list.add("72000");
        }

        return list;
    }
}

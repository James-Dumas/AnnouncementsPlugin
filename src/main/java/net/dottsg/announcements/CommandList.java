package net.dottsg.announcements;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandList implements TabExecutor
{
    private static final int PAGE_ITEM_NUM = 9;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length <= 1)
        {
            int page = 0;
            if(args.length == 1)
            {
                try
                {
                    page = Integer.parseInt(args[0]);
                }
                catch(NumberFormatException e) {}
            }
            else
            {
                page = 1;
            }

            List<String> allNames = new ArrayList<>(Announcements.announcementManager.announcements.keySet());
            int index = PAGE_ITEM_NUM * (page - 1);
            if(allNames.size() == 0)
            {
                sender.sendMessage(ChatColor.RED + "There are no announcements to list!");
            }
            else if(page <= 0 || index >= allNames.size())
            {
                sender.sendMessage(ChatColor.RED + "That page doesn't exist!");
            }
            else
            {
                allNames.sort(String::compareToIgnoreCase);
                List<String> pageNames = allNames.subList(index, Math.min(index + PAGE_ITEM_NUM, allNames.size()));

                StringBuilder msg = new StringBuilder();
                msg.append(ChatColor.YELLOW).append("< PAGE ").append(page).append(" / ").append((int) Math.ceil(allNames.size() / (float) PAGE_ITEM_NUM)).append(" >");
                for(String name : pageNames)
                {
                    msg.append("\n");
                    msg.append(ChatColor.BLUE).append(name).append(ChatColor.WHITE).append(" | ");
                    msg.append(ChatColor.RED);

                    Announcement an = Announcements.announcementManager.announcements.get(name);
                    if(an instanceof AnnouncementRandom)
                    {
                        msg.append("Random");
                    }
                    else if(an instanceof AnnouncementFixed)
                    {
                        msg.append("Fixed").append(ChatColor.WHITE).append(" | ").append(ChatColor.GREEN).append("Interval: ").append(((AnnouncementFixed) an).getInterval());
                    }
                    else if(an instanceof AnnouncementOnLogin)
                    {
                        msg.append("OnLogin").append(ChatColor.WHITE).append(" | ").append(ChatColor.GREEN).append("OnFirstLogin: ").append(((AnnouncementOnLogin) an).onFirstLogin());
                    }
                }

                sender.sendMessage(msg.toString());
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

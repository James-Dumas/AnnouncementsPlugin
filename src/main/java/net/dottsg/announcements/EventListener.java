package net.dottsg.announcements;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        for(AnnouncementOnLogin an : Announcements.announcementManager.announcementsOnLogin)
        {
            if(!an.onFirstLogin() || !e.getPlayer().hasPlayedBefore())
            {
                e.getPlayer().sendMessage(an.getMessage());
            }
        }
    }
}

package net.dottsg.announcements;

import com.google.gson.JsonObject;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;
import java.util.zip.DataFormatException;

public abstract class Announcement
{
    protected String name;
    protected String message;
    protected String formattedMessage;

    public Announcement(String name, String message)
    {
        this.name = name;
        setMessage(message);
    }

    public static Announcement fromJson(JsonObject obj) throws DataFormatException
    {
        String type;
        String name;
        String message;

        try
        {
           type = obj.get("type").getAsString();
           name = obj.get("name").getAsString();
           message = obj.get("message").getAsString();
        }
        catch(NullPointerException e)
        {
            throw new DataFormatException("Invalid format for saved announcement!");
        }

        Announcement an;
        switch(type)
        {
            case "random":
                an = new AnnouncementRandom(name, message);
                break;

            case "fixed":
                int interval = obj.get("interval").getAsInt();
                an = new AnnouncementFixed(name, message, interval);
                break;

            case "onlogin":
                boolean onFirstLogin = obj.get("onfirstlogin").getAsBoolean();
                an = new AnnouncementOnLogin(name, message, onFirstLogin);
                break;

            default:
                throw new DataFormatException("Invalid format for saved announcement!");
        }

        return an;
    }

    public static String formatChatMessage(String msg)
    {
        StringBuilder newMsg = new StringBuilder();
        boolean formatChar = false;
        for(int i = 0; i < msg.length(); i++)
        {
            char c = msg.charAt(i);
            if(c == '&')
            {
                formatChar = true;
            }
            else
            {
                if(formatChar)
                {
                    formatChar = false;
                    ChatColor color = ChatColor.getByChar(c);
                    if(color != null)
                    {
                        newMsg.append(color);
                    }
                    else
                    {
                        newMsg.append('&');
                        newMsg.append(c);
                    }
                }
                else
                {
                    newMsg.append(c);
                }
            }
        }

        if(formatChar)
        {
            newMsg.append('&');
        }

        return newMsg.toString();
    }

    public abstract JsonObject getJson();

    public String getMessage()
    {
        return formattedMessage;
    }

    public void setMessage(String message)
    {
        this.message = message;
        formattedMessage = formatChatMessage(message);
    }

    public String getName()
    {
        return name;
    }

}

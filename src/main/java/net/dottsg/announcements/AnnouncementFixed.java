package net.dottsg.announcements;

import com.google.gson.JsonObject;

public class AnnouncementFixed extends Announcement
{
    private int interval;

    public AnnouncementFixed(String name, String message, int interval)
    {
        super(name, message);
        this.interval = interval;
    }

    public int getInterval()
    {
        return interval;
    }

    @Override
    public JsonObject getJson()
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "fixed");
        obj.addProperty("name", name);
        obj.addProperty("message", message);
        obj.addProperty("interval", interval);

        return obj;
    }
}

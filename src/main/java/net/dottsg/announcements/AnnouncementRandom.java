package net.dottsg.announcements;

import com.google.gson.JsonObject;

public class AnnouncementRandom extends Announcement
{
    public AnnouncementRandom(String name, String message)
    {
        super(name, message);
    }

    @Override
    public JsonObject getJson()
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "random");
        obj.addProperty("name", name);
        obj.addProperty("message", message);

        return obj;
    }
}

package net.dottsg.announcements;

import com.google.gson.JsonObject;

public class AnnouncementOnLogin extends Announcement
{
    private boolean onFirstLogin;

    public AnnouncementOnLogin(String name, String message, boolean onFirstLogin)
    {
        super(name, message);
        this.onFirstLogin = onFirstLogin;
    }

    public boolean onFirstLogin()
    {
        return onFirstLogin;
    }

    @Override
    public JsonObject getJson()
    {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "onlogin");
        obj.addProperty("name", name);
        obj.addProperty("message", message);
        obj.addProperty("onfirstlogin", onFirstLogin);

        return obj;
    }
}

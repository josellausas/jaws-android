package systems.llau.jaws.Base;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pp on 1/31/16.
 */

public class LLNotification implements LLSendable
{
    private int severe;
    private String msg;
    private String identifier;

    public LLNotification(int severity, String message, String id)
    {
        this.severe = severity;
        this.msg = message;
        this.identifier = id;
    }

    public void updateWithJSON(JSONObject json)
    {
        try
        {
            this.severe = json.getInt("severe");
            this.msg = json.getString("msg");
            this.identifier = json.getString("ip");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON()
    {
        JSONObject o = null;
        try
        {
            o = new JSONObject();
            o.put("severe", this.severe);
            o.put("msg", this.msg);
            o.put("ip", this.identifier);
        }
        catch (JSONException e)
        {
            o = null;
            e.printStackTrace();
        }

        return o;
    }
}

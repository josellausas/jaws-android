package systems.llau.jaws.LLau;

import android.content.Context;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import systems.llau.jaws.Base.LLSendable;
import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 12/31/15.
 */
public class LLOrganization extends SugarRecord implements ListItemInterface, LLSendable
{
    private String name;    /** Official name of the organization */

    /**
     * Converts the object to a JSON
     * @return JSONObject the returning JSON
     */
    @Override
    public JSONObject toJSON()
    {
        JSONObject o = null;

        try
        {
            o = new JSONObject();
            o.put("name", this.name);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return o;
    }

    public void updateWithJSON(JSONObject json)
    {
        try
        {
            this.name = json.getString("name");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Custom Consttructor
     * @param name The Organization's official name
     */
    public LLOrganization(String name)
    {
        this.name = name;
    }

    /**
     * The name to represent this in a list
     * @param c The context
     * @return The localized title for the org.
     */
    public String getDisplayName(Context c)
    {
        return this.name;
    }



}

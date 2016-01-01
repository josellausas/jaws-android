package systems.llau.jaws.LLau;

import android.content.Context;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 12/31/15.
 */
public class LLUser extends SugarRecord implements ListItemInterface
{
    private String username;
    private long updated_at;
    private long created_at;


    public LLUser(String username)
    {
        this.username = username;
        this.created_at = System.currentTimeMillis();
        this.updated_at = this.created_at;
    }

    public String getDisplayName(Context c)
    {
        return this.username;
    }

    public String getUsername()
    {
        return this.username;
    }

    public boolean updateWithJSON(JSONObject obj)
    {
        try
        {
            this.username = obj.getString("username");

            // Update the tymestamp
            this.updated_at = System.currentTimeMillis();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }



}

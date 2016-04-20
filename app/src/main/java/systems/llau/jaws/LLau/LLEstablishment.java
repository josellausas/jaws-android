package systems.llau.jaws.LLau;

import android.content.Context;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import systems.llau.jaws.Base.LLSendable;

/**
 * Created by pp on 12/31/15.
 * @class LLEstablishment
 * @brief Abstracts an establishment
 */
public class LLEstablishment extends SugarRecord implements LLTarget, LLSendable
{
    private String name;            /** The name */
    private LLOrganization parent;  /** The parent organization */
    private LLContact mainContact;  /** The point of contact for the establishment */


    /**
     * Converts to a sendable JSON
     * @return JSONObject The JSON representation
     */
    public JSONObject toJSON()
    {
        JSONObject o = null;
        try
        {
            o.put("name", this.name);
            o.put("parent", this.parent.toJSON());
            o.put("mainContact", this.mainContact.toJSON());
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
            this.parent.updateWithJSON(json.getJSONObject("parent"));
            this.mainContact.updateWithJSON((json.getJSONObject("mainContact")));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Custom constructor
     * @param name  The name of the establishment
     * @param parent    The parent organization
     * @param mainContact   The main point of contact for the organization
     */
    public LLEstablishment(String name, LLOrganization parent, LLContact mainContact)
    {
        this.name   = name;
        this.parent = parent;
        this.mainContact = mainContact;
    }


    /**
     * The type of target
     * @return The target type
     */
    public LLTargetType getType()
    {
        return LLTargetType.LL_TARGET_ESTABLISHMENT;
    }

    /**
     * The establishment's name
     * @return  The establishment's name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * A list of available Actions that can be performed to this target type
     * @return A list of available actions
     */
    public List<LLAction> getAvailableActions()
    {
        return new ArrayList<>();
    }

    /**
     * A string representing the object
     * @param c The context
     * @return A localized string
     */
    public String getDisplayName(Context c)
    {
        return this.name;
    }

}

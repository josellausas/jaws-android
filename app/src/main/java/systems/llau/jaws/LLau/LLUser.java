package systems.llau.jaws.LLau;

import android.content.Context;

import com.orm.SugarRecord;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 12/31/15.
 */
public class LLUser extends SugarRecord implements ListItemInterface
{
    private String username;

    public LLUser(String username)
    {
        this.username = username;
    }

    public String getDisplayName(Context c)
    {
        return this.username;
    }



}

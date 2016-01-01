package systems.llau.jaws.LLau;

import android.content.Context;

import com.orm.SugarRecord;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 12/31/15.
 */
public class LLOrganization extends SugarRecord implements ListItemInterface
{
    private String name;

    public LLOrganization(String name)
    {
        this.name = name;
    }

    public String getDisplayName(Context c)
    {
        return this.name;
    }



}

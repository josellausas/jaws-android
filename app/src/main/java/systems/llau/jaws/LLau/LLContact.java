package systems.llau.jaws.LLau;

import android.content.Context;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pp on 12/31/15.
 */
public class LLContact extends SugarRecord implements LLTarget
{
    private String firstName;
    private String lastName;
    private LLOrganization parentOrganization;


    public LLContact(String first, String last, LLOrganization org)
    {
        this.firstName = first;
        this.lastName = last;
        this.parentOrganization = org;
    }

    public LLTargetType getType()
    {
        return LLTargetType.LL_TARGET_CONTACT;
    }

    public List<LLAction> getAvailableActions()
    {
        return new ArrayList();
    }

    public String getDisplayName(Context c)
    {
        return this.firstName + " " + this.lastName;
    }

    public String getName()
    {
        return this.firstName + " " + this.lastName;
    }

}

package systems.llau.jaws.LLau;

import android.content.Context;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pp on 12/31/15.
 */
public class LLEstablishment extends SugarRecord implements LLTarget {
    private String name;
    private LLOrganization parent;
    private LLContact mainContact;

    public LLEstablishment(String name, LLOrganization parent, LLContact mainContact) {
        this.name = name;
        this.parent = parent;
        this.mainContact = mainContact;
    }

    public LLTargetType getType() {
        return LLTargetType.LL_TARGET_ESTABLISHMENT;
    }

    public String getName() {
        return this.name;
    }



    public List<LLAction> getAvailableActions()
    {
        return new ArrayList<>();
    }

    public String getDisplayName(Context c)
    {
        return this.name;
    }

}

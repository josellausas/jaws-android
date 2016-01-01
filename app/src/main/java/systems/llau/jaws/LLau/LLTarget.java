package systems.llau.jaws.LLau;

import java.util.List;

/**
 * Created by pp on 12/31/15.
 */


public interface LLTarget
{
    public enum LLTargetType
    {
        LL_TARGET_ESTABLISHMENT,
        LL_TARGET_CONTACT
    }

    public String getName();
    public LLTargetType getType();
    public List<LLAction> getAvailableActions();

}

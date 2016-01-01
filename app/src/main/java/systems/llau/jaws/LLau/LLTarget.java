package systems.llau.jaws.LLau;

import java.util.List;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 12/31/15.
 */


public interface LLTarget extends ListItemInterface
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

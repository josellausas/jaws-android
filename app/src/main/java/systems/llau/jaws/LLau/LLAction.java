package systems.llau.jaws.LLau;

/**
 * Created by pp on 12/31/15.
 */
public interface LLAction
{
    public enum LLActionType
    {
        LL_ACTION_ABSTRACT,
    }

    public LLActionType getType();

    // Starts the action on the target
    public LLOutcome start(LLTarget target);

}

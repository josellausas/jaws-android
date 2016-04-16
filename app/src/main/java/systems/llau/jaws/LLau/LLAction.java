package systems.llau.jaws.LLau;

/**
 * Created by pp on 12/31/15.
 */
public interface LLAction
{
    /**
     * The types of actions
     */
    public enum LLActionType
    {
        LL_ACTION_ABSTRACT,
    }

    public LLActionType getType();

    /**
     * Mark the beggining of an action perfomed on a target.
     * @param target The receiver of the action
     * @return An initialized Outcome to keep track of the progress
     */
    public LLOutcome start(LLTarget target);

}

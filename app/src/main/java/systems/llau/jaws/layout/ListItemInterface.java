package systems.llau.jaws.layout;

import android.content.Context;

/**
 * Interface for displaying inside a List
 * Created by pp on 12/31/15.
 */
public interface ListItemInterface
{
    /**
     * Required.
     * Return a String to display in the list. Use the context for Localization
     * @param c The context
     * @return A localized string that represents the object in the list
     */
    public String getDisplayName(Context c);

    /**
     * Required.
     * Returns the DatabaseID
     * @return
     */
    public Long getId();

}

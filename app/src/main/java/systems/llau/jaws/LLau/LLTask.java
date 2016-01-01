package systems.llau.jaws.LLau;

import android.content.Context;

/**
 * Created by pp on 12/31/15.
 */
public class LLTask
{
    private String name;

    public LLTask(String name) { this.name = name; }
    public String getDisplayName(Context c) { return this.name; }
}

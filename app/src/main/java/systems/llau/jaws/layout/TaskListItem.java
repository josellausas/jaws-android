package systems.llau.jaws.layout;

import android.content.Context;

import systems.llau.jaws.LLau.LLTask;

/**
 * Created by pp on 12/31/15.
 */
public class TaskListItem
{
    private LLTask taskData;

    public TaskListItem(LLTask task) {taskData = task;}

    public String getItemTitle(Context c) { return taskData.getDisplayName(c); }
}

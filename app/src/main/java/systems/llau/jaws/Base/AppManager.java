package systems.llau.jaws.Base;

import systems.llau.jaws.LLau.LLTask;

/**
 * Created by pp on 3/26/16.
 */
public class AppManager
{
    private static AppManager ourInstance = new AppManager();

    public static AppManager getInstance() {
        return ourInstance;
    }

    private AppManager() {
    }

    public LLTask getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(LLTask selectedTask) {
        this.selectedTask = selectedTask;
    }

    private LLTask selectedTask = null;


}

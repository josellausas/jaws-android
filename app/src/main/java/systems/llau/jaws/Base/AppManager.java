package systems.llau.jaws.Base;

import systems.llau.jaws.LLau.LLTask;

/**
 * @class AppManager
 * @description The app's manager. Controls everything
 */
public class AppManager
{
    /**
     * The Singleton instance variable
     */
    private static AppManager ourInstance = new AppManager();

    /**
     * Singleton access
     * @return An instance of the singleton.
     */
    public static AppManager getInstance()
    {
        return ourInstance;
    }

    /**
     * Default Constructor
     */
    private AppManager()
    {
    }

    /**
     * Returns the selected Task
     * @return The task that was selected by the user
     */
    public LLTask getSelectedTask() {
        return selectedTask;
    }


    /**
     * Sets the task as selected by the User
     * @param selectedTask The Task that was selected
     */
    public void setSelectedTask(LLTask selectedTask)
    {
        this.selectedTask = selectedTask;
    }

    private LLTask selectedTask = null;


}

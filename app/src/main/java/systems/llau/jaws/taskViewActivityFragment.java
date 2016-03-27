package systems.llau.jaws;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import systems.llau.jaws.Base.AppManager;
import systems.llau.jaws.LLau.LLTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class taskViewActivityFragment extends Fragment
{
    private TextView titleLabel = null;
    private TextView dateLabel = null;
    private TextView noteLabel = null;

    private LLTask selectedTask= null;

    public taskViewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_task_view, container, false);

        titleLabel = (TextView)rootView.findViewById(R.id.titleLabel);
        dateLabel = (TextView)rootView.findViewById(R.id.dateLabel);
        noteLabel = (TextView)rootView.findViewById(R.id.noteLabel);

        this.selectedTask = AppManager.getInstance().getSelectedTask();
        if(this.selectedTask != null)
        {
            titleLabel.setText(selectedTask.getTitle());

            String readable = selectedTask.getReadableDate(selectedTask.getDate().getTime());
            dateLabel.setText(readable);
            noteLabel.setText(selectedTask.getNotes());
        }

        if(this.selectedTask == null)
        {
            Crashlytics.getInstance().log("Bad things happened here in selected task");
        }

        return rootView;
    }
}

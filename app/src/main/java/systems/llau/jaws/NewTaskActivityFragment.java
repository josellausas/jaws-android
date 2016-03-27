package systems.llau.jaws;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewTaskActivityFragment extends Fragment
{
    // The UI things
    private EditText titleEditText = null;
    private EditText dateEditText  = null;
    private EditText notesEditText = null;
    private Button   createButton  = null;

    /// Default construcctor
    public NewTaskActivityFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_new_task2, container, false);

        // Load the view elements to handle
        titleEditText = (EditText)rootView.findViewById(R.id.titleEditText);

        // The date
        dateEditText = (EditText)rootView.findViewById(R.id.dateEditText);

        // Notes
        notesEditText = (EditText)rootView.findViewById(R.id.notesEditText);

        // The create button
        createButton = (Button)rootView.findViewById(R.id.createButton);

        return rootView;
    }




}



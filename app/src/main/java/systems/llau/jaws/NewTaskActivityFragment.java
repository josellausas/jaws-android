package systems.llau.jaws;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewTaskActivityFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener

{
    // The UI things
    private EditText titleEditText = null;
    private EditText dateEditText  = null;
    private EditText notesEditText = null;
    private Button   createButton  = null;
    private Button  datePickButton = null;

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

        datePickButton = (Button)rootView.findViewById(R.id.datePickButton);

        datePickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dialog = DatePickerDialog.newInstance(
                        NewTaskActivityFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Log.d("Dg", "Dialog was cancelled");
                    }
                });

                dialog.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        // Dissable autofocus for keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        return rootView;
    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int secs) {
        String time = "You picked the following time: "+hourOfDay+"h"+minute;
//        timeTextView.setText(time);
        Log.i("Time", time);

        // This is the juice!
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
//        dateTextView.setText(date);
        Log.i("Time", date);

        //int hourOfDay, int minute, int second, boolean is24HourMode

//        Calendar now = Calendar.getInstance();
//        TimePickerDialog dialog = TimePickerDialog.newInstance(
//                NewTaskActivityFragment.this,
//                now.get(Calendar.HOUR_OF_DAY),
//                now.get(Calendar.MINUTE),
//                true);
//
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
//        {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                Log.d("Dg", "Dialog was cancelled");
//            }
//        });
//
//        dialog.show(getActivity().getFragmentManager(), "timePicker");

        this.dateEditText.setText(""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year);

    }
}



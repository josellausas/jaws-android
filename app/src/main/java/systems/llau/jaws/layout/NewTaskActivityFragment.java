package systems.llau.jaws.layout;

import android.content.DialogInterface;
import android.net.ParseException;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import systems.llau.jaws.LLau.LLTask;
import systems.llau.jaws.NewTaskActivity;
import systems.llau.jaws.R;

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
            public void onClick(final View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dialog = DatePickerDialog.newInstance(
                        NewTaskActivityFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Log.d("Dg", "Dialog was cancelled");
                        Snackbar.make(v, "Canceled", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

                dialog.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        createButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Check all the data
                String t = NewTaskActivityFragment.this.titleEditText.getText().toString();
                String d = NewTaskActivityFragment.this.dateEditText.getText().toString();
                String n = NewTaskActivityFragment.this.notesEditText.getText().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                try {

                    Date date = formatter.parse(d);
                    System.out.println(date);
                    System.out.println(formatter.format(date));
                    tryToCreate(t, date, n);

                }
                catch (java.text.ParseException e)
                {
                    e.printStackTrace();
                    // Bad bad bad
                    Snackbar.make(v, "Bad date", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        // Dissable autofocus for keyboard
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        return rootView;
    }


    private void tryToCreate(String title, Date date, String notes)
    {
        if(title == null || date == null || notes == null)
        {
            Snackbar.make(this.getView(), "Bad data", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            // This is bad
            return;
        }

        if(title.equals("") || date.equals("") || notes.equals(""))
        {
            Snackbar.make(this.getView(), "Bad Data", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            // No bueno
            return;
        }

        LLTask createdTask = new LLTask(title, date, notes);

        Snackbar.make(this.getView(), "Created Success", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        createdTask.save();

        getActivity().onBackPressed();

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



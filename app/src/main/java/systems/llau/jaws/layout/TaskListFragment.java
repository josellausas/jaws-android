package systems.llau.jaws.layout;

import android.app.ListFragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import systems.llau.jaws.Base.AppManager;
import systems.llau.jaws.LLau.LLTask;
import systems.llau.jaws.MainActivity;
import systems.llau.jaws.NewTaskActivity;
import systems.llau.jaws.R;
import systems.llau.jaws.taskViewActivity;

/**
 * Created by pp on 12/31/15.
 */
public class TaskListFragment extends ListFragment
{
    private List<ListItemInterface> taskItemList = null;
    private ListItemAdapter adapter = null;
    private ListView listView = null;

    private TaskListFragment getMyself(){return this;}

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancec)
    {
        View rootView = inflater.inflate(R.layout.fragment_easy_list, container, false);

        // Indicates that we override the menu.
        setHasOptionsMenu(true);

        listView = (ListView)rootView.findViewById(android.R.id.list);

        // Create a Fab
        // The action button
        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewTaskIntent = new Intent(getActivity(), NewTaskActivity.class);
                pushActivity(createNewTaskIntent);
            }
        });

        return rootView;
    }

    private void pushActivity(Intent intent)
    {
        Intent createNewTaskIntent = intent;
        // Use TaskStackBuilder to build the back stack and get the PendingIntent
        PendingIntent pendingIntent =
                TaskStackBuilder.create(getActivity())
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(createNewTaskIntent)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setContentIntent(pendingIntent);

        // Set the fragment we were last on:
        MainActivity activity = (MainActivity)getActivity();

        activity.setPreviousFragmentActive(getMyself());


        try
        {
            pendingIntent.send(getActivity(), 0, createNewTaskIntent);
        }
        catch (PendingIntent.CanceledException e)
        {
            e.printStackTrace();
        }
    }

    private void setupAdapter()
    {
        adapter = new ListItemAdapter(getActivity(), taskItemList);
        setListAdapter(adapter);
    }



    private void loadTaskList()
    {
        // LOL, typecast with a variable!!! OMG Java
        List l = LLTask.listAll(LLTask.class);
        taskItemList = l;
    }

    private Long getTodayDate()
    {
        // Creates a calendar date with today.
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        //cal.set(Calendar.DAY_OF_MONTH, maxDay);
        //cal.set(Calendar.MONTH, maxMonth);
        //cal.set(Calendar.YEAR, maxYear);
        return cal.getTimeInMillis();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        loadTaskList();
        setupAdapter();
    }

    private void refreshUI()
    {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
            {
                return true;
            }


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        LLTask taskSelected = (LLTask)taskItemList.get(position);

        // Show the thing selected
//        taskViewActivity viewActivity = new taskViewActivity();
//        viewActivity.setTask(taskSelected);

        AppManager app = AppManager.getInstance();
        app.setSelectedTask(taskSelected);

        Intent intent = new Intent(getActivity(), taskViewActivity.class);

        pushActivity(intent);

    }



}

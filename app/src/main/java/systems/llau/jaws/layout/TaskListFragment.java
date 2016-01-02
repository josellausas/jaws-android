package systems.llau.jaws.layout;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import systems.llau.jaws.LLau.LLTask;
import systems.llau.jaws.R;

/**
 * Created by pp on 12/31/15.
 */
public class TaskListFragment extends ListFragment
{
    private List<ListItemInterface> taskItemList = null;
    private ListItemAdapter adapter = null;
    private ListView listView = null;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancec)
    {
        View rootView = inflater.inflate(R.layout.fragment_easy_list, container, false);

        // Indicates that we override the menu.
        setHasOptionsMenu(true);

        listView = (ListView)rootView.findViewById(android.R.id.list);






        return rootView;
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

    }



}

package systems.llau.jaws.layout;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import systems.llau.jaws.LLau.LLUser;
import systems.llau.jaws.R;

/**
 * Created by pp on 1/1/16.
 */
public class UserListFragment extends ListFragment
{
    private List<ListItemInterface> userItemList = null;
    private ListItemAdapter adapter = null;

    private ListView listView = null;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        View rootView = inflater.inflate(R.layout.fragment_users_list, container, false);

        listView = (ListView)rootView.findViewById(android.R.id.list);

        // Create a Fab
        // The action button
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });





        return rootView;
    }


    private void setupListAdapter()
    {
        adapter = new ListItemAdapter(getActivity(), userItemList);
        setListAdapter(adapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        loadList();

        setupListAdapter();


    }

    private void loadList()
    {

        List<LLUser> users = LLUser.listAll(LLUser.class);

        if(userItemList == null)
        {
            userItemList = new ArrayList<>();
        }
        else
        {
            userItemList.clear();
        }

        for(int i = 0; i < users.size(); ++i)
        {
            // Treat them as interfaces:
            ListItemInterface u = (ListItemInterface)users.get(i);
            userItemList.add(u);
        }
    }

}

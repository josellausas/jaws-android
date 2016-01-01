package systems.llau.jaws.layout;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

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

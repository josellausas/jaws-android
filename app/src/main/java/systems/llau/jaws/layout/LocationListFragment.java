package systems.llau.jaws.layout;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import systems.llau.jaws.Base.LLLocation;
import systems.llau.jaws.R;

/**
 * Created by pp on 3/23/16.
 */
public class LocationListFragment extends ListFragment
{

    private List<ListItemInterface> locationListItems;
    private ListItemAdapter adapter = null;
    private ListView listView = null;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstane)
    {
        View rootView = inflater.inflate(R.layout.location_layout, container, false);

        listView = (ListView)rootView.findViewById(android.R.id.list);

        return rootView;
    }

    private void setupAdapter()
    {
        adapter = new ListItemAdapter(getActivity(), locationListItems);
        setListAdapter(adapter);
    }

    private void loadList()
    {
        List l = LLLocation.listAll(LLLocation.class);
        locationListItems = l;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadList();
        setupAdapter();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {

    }
}

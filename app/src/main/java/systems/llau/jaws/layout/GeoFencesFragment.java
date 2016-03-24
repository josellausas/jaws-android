package systems.llau.jaws.layout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

import systems.llau.jaws.R;

/**
 * Created by pp on 3/23/16.
 */
public class GeoFencesFragment extends Fragment
{
    private MapFragment mapFragment = null;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstane)
    {
        View rootView = inflater.inflate(R.layout.geo_fences, container, false);

        mapFragment = MapFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment_container, mapFragment);
        transaction.commit();
        return rootView;
    }
}

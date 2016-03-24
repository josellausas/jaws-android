package systems.llau.jaws.layout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import systems.llau.jaws.Base.LLLocation;
import systems.llau.jaws.MainActivity;
import systems.llau.jaws.R;

/**
 * Created by pp on 3/23/16.
 */
public class GeoFencesFragment extends Fragment implements OnMapReadyCallback
{
    private MapView mapView = null;
    private GoogleMap map   = null;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstane)
    {
        View rootView = inflater.inflate(R.layout.geo_fences, container, false);

        // The Google maps
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_HYBRID)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);


        mapView = (MapView)rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstane);

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        MapsInitializer.initialize(this.getActivity());

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback()
        {
            @Override
            public void onMapLoaded() {
                // Set the map type
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);


                MainActivity activity = (MainActivity)getActivity();

                Location loc = activity.getLastLocation();
                LatLng currentPos = new LatLng(loc.getLatitude(), loc.getLongitude());
                if(loc != null)
                {
                    map.addMarker(new MarkerOptions()
                            .position(currentPos)
                            .title("Last location"));
                }

                List<LLLocation> allLocs = LLLocation.listAll(LLLocation.class);

                for(int i = 0; i < allLocs.size(); ++i)
                {
                    LLLocation l = allLocs.get(i);
                    map.addMarker(new MarkerOptions().position(new LatLng(l.getLatitude(), l.getLongitude()))
                            .title("Previous position"));
                }

                // Set the camera

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15));
            }
        });




        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        // Set the map type
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Get the current location
        MainActivity activity = (MainActivity)getActivity();
        Location loc = activity.getLastLocation();
        LatLng currentPos = new LatLng(loc.getLatitude(), loc.getLongitude());
        if(loc != null)
        {
            map.addMarker(new MarkerOptions()
                    .position(currentPos)
                    .title("Last location"));
        }

        List<LLLocation> allLocs = LLLocation.listAll(LLLocation.class);

        for(int i = 0; i < allLocs.size(); ++i)
        {
            LLLocation l = allLocs.get(i);
            map.addMarker(new MarkerOptions().position(new LatLng(l.getLatitude(), l.getLongitude()))
                    .title("Previous position"));
        }

        // Set the camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15));
//        map.animateCamera(CameraUpdateFactory.zoomIn());
    }

    @Override
    public void onResume()
    {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause()
    {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        mapView.onLowMemory();
        super.onLowMemory();
    }
}

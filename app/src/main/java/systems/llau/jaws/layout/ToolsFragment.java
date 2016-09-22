package systems.llau.jaws.layout;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.api.client.googleapis.auth.oauth2.*;


import systems.llau.jaws.R;


public class ToolsFragment extends Fragment
{
    private Button wakatimeButton = null;

    public ToolsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_tools, container, false);

        // The Wakatime action button
        wakatimeButton = (Button)rootView.findViewById(R.id.button_wakatime);
        wakatimeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Do the wakatime oauth here
                Log.i("WAK", "Clicked the button");

                wakaAuth();
            }
        });

        return rootView;
    }

    private void wakaAuth()
    {



        try {

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    @Override
    public void onResume()
    {
        super.onResume();
    }
}

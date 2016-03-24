package systems.llau.jaws.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import systems.llau.jaws.R;

/**
 * Created by pp on 3/23/16.
 */
public class NewTaskFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancec)
    {
        View rootView = inflater.inflate(R.layout.fragment_new_task, container, false);

        return rootView;
    }
}

package systems.llau.jaws.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import systems.llau.jaws.R;

/**
 * Created by pp on 12/30/15.
 */
public class MessagesFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstane)
    {
        return inflater.inflate(R.layout.fragment_messages_list, container, false);

    }
}

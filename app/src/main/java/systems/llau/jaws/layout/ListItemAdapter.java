package systems.llau.jaws.layout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import systems.llau.jaws.R;


/**
 * Created by pp on 12/31/15.
 */
public class ListItemAdapter extends ArrayAdapter
{
    public ListItemAdapter(Context c, List<ListItemInterface> items)
    {
        super(c, android.R.layout.simple_list_item_1, items);
    }

    private class ViewHolder
    {
        TextView titleText;
    }

    public View getView(int pos, View convertView, ViewGroup parent)
    {
        ViewHolder holder   = null;
        View viewToUse      = null;
        ListItemInterface listItem = (ListItemInterface)getItem(pos);

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
        {
            viewToUse = inflater.inflate(R.layout.list_item_deafult, null);

            holder = new ViewHolder();
            holder.titleText = (TextView)viewToUse.findViewById(R.id.titleText);

            viewToUse.setTag(holder);

        }
        else
        {
            viewToUse = convertView;
            holder = (ViewHolder)viewToUse.getTag();
        }

        // Set the name for the thing
        String name = listItem.getDisplayName(getContext());
        holder.titleText.setText(name);

        return viewToUse;
    }
}

package systems.llau.jaws.LLau;

import android.content.Context;

import com.orm.SugarRecord;

import java.sql.Date;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 12/31/15.
 */
public class LLMessage extends SugarRecord implements ListItemInterface
{
    private String payload;
    private LLUser destination;
    private long created_at;
    private long delivered_at;

    public LLMessage(LLUser destination, String msg)
    {
        this.payload      = msg;
        this.destination  = destination;
        this.created_at   = System.currentTimeMillis();
        this.delivered_at = 0;
    }

    public String getDisplayName(Context c)
    {
        return "To: " + this.destination.getDisplayName(c);
    }

    public void stampDelivery()
    {
        this.delivered_at = System.currentTimeMillis();
    }

    public boolean wasDelivered()
    {
        if(delivered_at == 0)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

}

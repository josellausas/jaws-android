package systems.llau.jaws.Base;

import android.content.Context;
import android.location.Location;
import com.orm.SugarRecord;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 3/23/16.
 */
public class LLLocation extends LLLogable implements ListItemInterface
{
    double latitude;        /// Latitude
    double longitude;       /// Longitude

    public LLLocation() {}

    public LLLocation(Location l)
    {
        super(System.currentTimeMillis());
        this.latitude  = l.getLatitude();
        this.longitude = l.getLongitude();
    }

    public LLLocation(double lat, double longi)
    {
        super(System.currentTimeMillis());
        this.latitude  = lat;
        this.longitude = longi;
    }

    public List<LLLocation> getAll()
    {
        return LLLocation.listAll(LLLocation.class);
    }

    public String getDisplayName(Context context)
    {
        return "Date: " + this.getReadableDate(this.getTimestamp());
    }



    public double getLatitude()
    {
        return this.latitude;
    }

    public double getLongitude()
    {
        return this.longitude;
    }

}

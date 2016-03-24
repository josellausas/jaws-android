package systems.llau.jaws.Base;

import android.location.Location;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by pp on 3/23/16.
 */
public class LLLocation extends SugarRecord
{
    double latitude;        /// Latitude
    double longitude;       /// Longitude
    long   timestamp;       /// Time created

    public LLLocation() {}

    public LLLocation(Location l)
    {
        this.timestamp = System.currentTimeMillis();
        this.latitude  = l.getLatitude();
        this.longitude = l.getLongitude();
    }

    public LLLocation(double lat, double longi)
    {
        this.timestamp = System.currentTimeMillis();
        this.latitude  = lat;
        this.longitude = longi;
    }

    public List<LLLocation> getAll()
    {
        return LLLocation.listAll(LLLocation.class);
    }

}

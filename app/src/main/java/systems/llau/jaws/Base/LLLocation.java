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
    double latitude;        /** Latitude */
    double longitude;       /** Longitude */

    /**
     * Default Constructor
     */
    public LLLocation() {}

    /**
    *   LLLocation()
    *
    *   @brief Constructs a new location with a location object
    *   @param l The location object
    *
     */
    public LLLocation(Location l)
    {
        super(System.currentTimeMillis());
        this.latitude  = l.getLatitude();
        this.longitude = l.getLongitude();
    }

    /**
    * LLLocation()
    *
    * @brief Constructs a new location with a latitude and a longitude
    * @param lat Latitude coordinate
    * @param longi Longitude coordinate
    *
    */
    public LLLocation(double lat, double longi)
    {
        super(System.currentTimeMillis());
        this.latitude  = lat;
        this.longitude = longi;
    }

    /**
     * getAll()
     * @brief Retuns a list of all the locations
     * @function getAll()
     * @return A list of all the locations
     */
    public List<LLLocation> getAll()
    {
        return LLLocation.listAll(LLLocation.class);
    }

    /**
     * GetDisplayName()
     *
     * @brief Retuns the name to display inside a listView
     * @param context The app's context
     * @return A string that represents this object that is localized
     */
    public String getDisplayName(Context context)
    {
        return "Date: " + this.getReadableDate(this.getTimestamp());
    }

    /**
     * @brief Returns the lattidude
     * @return The lattiude for this location
     */
    public double getLatitude()
    {
        return this.latitude;
    }

    /**
     * @function getLongitude()
     * @return The longitude
     */
    public double getLongitude()
    {
        return this.longitude;
    }

}

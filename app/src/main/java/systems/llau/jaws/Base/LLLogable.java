package systems.llau.jaws.Base;

import com.orm.SugarRecord;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 3/26/16.
 * @class LLLogable
 *
 * @brief Abstacts a class for something that can be logged
 *
 */
public class LLLogable extends SugarRecord
{
    private Long timestamp = null;

    /**
     * The timestamp for this log
     * @return The time that this log was created
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp created
     * @param timestamp The time this log was created
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Default Constructor
     */
    public LLLogable()
    {
        // Does nothing at all.
    }

    /**
     * Custom constructor
     * @brief Constructs a new Log from a timestamp
     * @param ts The time of creation
     */
    public LLLogable(Long ts)
    {
        this.timestamp = ts;
    }

    /**
     * The readable form. This is displayed inside a list
     * @param timestamp The timestamp
     * @return  The readable date
     */
    public String getReadableDate(long timestamp)
    {
        DateFormat.getInstance().format(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy - (HH:mm)");
        Date resultdate = new Date(timestamp);
        return sdf.format(resultdate);
    }
}

package systems.llau.jaws.Base;

import com.orm.SugarRecord;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 3/26/16.
 */
public class LLLogable extends SugarRecord
{
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    private Long timestamp = null;

    public LLLogable()
    {

    }

    public LLLogable(Long ts)
    {
        this.timestamp = ts;
    }

    public String getReadableDate(long timestamp)
    {
        DateFormat.getInstance().format(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy - (HH:mm)");
        Date resultdate = new Date(timestamp);
        return sdf.format(resultdate);
    }
}

package systems.llau.jaws.LLau;

import android.content.Context;


import com.orm.SugarRecord;

import net.danlew.android.joda.JodaTimeAndroid;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import systems.llau.jaws.Base.LLLocation;
import systems.llau.jaws.Base.LLLogable;
import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 12/31/15.
 */
public class LLTask extends LLLogable implements ListItemInterface
{
    // Private members
    private Long      id = null;
    private String title = null;
    private Long    date = null;
    private String notes = null;
    private boolean isComplete = false;

    public LLTask()
    {

    }

    /// Constructor
    public LLTask(String title, java.util.Date dueDate, String notes )
    {
        super(System.currentTimeMillis());

        this.title = title;
        this.date  = dueDate.getTime();
        this.notes = notes;

    }

    /// Accesors
    public String getDisplayName(Context c) { return this.title; }
    public Long getId()                     { return this.id; }
    public void setDate(Time date)          { this.date = date.getTime();}
    public Time getDate()                   { return new Time(this.date.longValue());}
    public void setNotes(String n)          { this.notes = n;}
    public String getNotes() {return this.notes;}
    public void setTitle(String t)          { this.title = t;}
    public String getTitle()                { return this.title;}
    public boolean isComplete() {return this.isComplete;}
    public void setComplete(boolean b){this.isComplete = b;}



    //

    /// Returns a list of all the Tasks
    public List<LLTask> listAll()
    {
        List<LLTask> list = LLTask.listAll(LLTask.class);
        return list;
    }

    public String getReadableDate(long timestamp)
    {
        DateFormat.getInstance().format(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        Date resultdate = new Date(timestamp);
        return sdf.format(resultdate);
    }

}



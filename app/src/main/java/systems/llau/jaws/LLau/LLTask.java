package systems.llau.jaws.LLau;

import android.content.Context;


import com.orm.SugarRecord;

import net.danlew.android.joda.JodaTimeAndroid;

import java.sql.Time;
import java.util.List;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 12/31/15.
 */
public class LLTask extends SugarRecord implements ListItemInterface
{
    // Private members
    private Long      id = null;
    private String title = null;
    private Long    date = null;
    private String notes = null;

    /// Constructor
    public LLTask(String title, Time dueDate, String notes )
    {
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
    public void setTitle(String t)          { this.title = t;}
    public String getTitle()                { return this.title;}

    /// Returns a list of all the Tasks
    public List<LLTask> listAll()
    {
        List<LLTask> list = LLTask.listAll(LLTask.class);
        return list;
    }

}

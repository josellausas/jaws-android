package systems.llau.jaws.LLau;

import android.content.Context;


import com.orm.SugarRecord;

import java.util.List;

import systems.llau.jaws.layout.ListItemInterface;

/**
 * Created by pp on 12/31/15.
 */
public class LLTask extends SugarRecord implements ListItemInterface
{
    private Long id;
    private String name;

    public LLTask(String name) { this.name = name; }
    public String getDisplayName(Context c) { return this.name; }
    public Long getId() { return this.id; }

    public List<LLTask> listAll() {
        List<LLTask> list = LLTask.listAll(LLTask.class);
        return list;
    }

}

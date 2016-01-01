package systems.llau.jaws.LLau;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by pp on 1/1/16.
 */
public class LLSyncManager
{
    private static LLSyncManager ourInstance = new LLSyncManager();

    public static LLSyncManager getInstance() {
        return ourInstance;
    }

    private String username;

    private Map<Long, LLMessage> undeliveredMessages;
    private Map<Long, LLJornada> undeliveredJornadas;
    private Map<Long, LLOutcome> undeliveredOutcomes;

    // Actions we are allowed to perform
    private Map<Long, LLAction>          actionList;
    private Map<Long, LLUser>            usersList;
    private Map<Long, LLOrganization>    organizationList;
    private Map<Long, LLContact>         contactsList;
    private Map<Long, LLEstablishment>   establishmentList;


    private List<LLTask> taskList;

    private LLSyncManager()
    {
        username = "Ilegal user!!!";

        // Allocates new maps for each list.
        this.undeliveredMessages = new HashMap<Long, LLMessage>();
        this.undeliveredJornadas = new HashMap<Long, LLJornada>();
        this.undeliveredOutcomes = new HashMap<Long, LLOutcome>();

        this.actionList         = new HashMap<Long, LLAction>();
        this.usersList          = new HashMap<Long, LLUser>();
        this.organizationList   = new HashMap<Long, LLOrganization>();
        this.contactsList       = new HashMap<Long, LLContact>();
        this.establishmentList  = new HashMap<Long, LLEstablishment>();
    }

    public void init(String username)
    {
        this.username = username;
    }

    private LLUser userWithJSON(JSONObject obj)
    {
        try
        {
            String username = obj.getString("username");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }

        return new LLUser(username);
    }


    public boolean syncUsers(JSONArray array)
    {
        boolean authorized = false;

        // An array of JSON users:
        for(int i = 0; i < array.length(); ++i)
        {
            try
            {
                JSONObject userJSON = array.getJSONObject(i);

                String username = userJSON.getString("username");

                // Skip our own username
                if( !username.equals(this.username) && username != null )
                {
                    LLUser userToSync = null;

                    // Search for an existing user
                    Iterator<Long> userIterator = this.usersList.keySet().iterator();

                    while (userIterator.hasNext())
                    {
                        Long theID = userIterator.next();

                        // Get the value:
                        LLUser searchUser = this.usersList.get(theID);

                        if(searchUser.getUsername().equals(username))
                        {
                            // Found a match!!! Time to update!
                            // This overwirtes with the server information
                            userToSync = searchUser;
                        }

                    }

                    // Create a new one if it doesnt exist int he list
                    if(userToSync == null)
                    {

                        // Contructs a new thing
                        userToSync = userWithJSON(userJSON);
                    }
                    else
                    {
                        // Update the data from the thing
                        boolean success = userToSync.updateWithJSON(userJSON);

                        if(success == false)
                        {
                            return false;
                        }
                    }

                    userToSync.save();


                    // Map it to memory with its database id
                    this.usersList.put(userToSync.getId(), userToSync);

                }
                else
                {
                    // this indicates that we exist in the list:
                    authorized = true;

                }
            }
            catch (JSONException e)
            {
                // Log it.
                e.printStackTrace();

                // Indicates failure
                return false;
            }
        }
        // Indicates success
        return true;
    }

}

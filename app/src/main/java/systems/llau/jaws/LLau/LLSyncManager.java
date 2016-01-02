package systems.llau.jaws.LLau;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by pp on 1/1/16.
 */
public class LLSyncManager
{
    private static LLSyncManager ourInstance = new LLSyncManager();

    public static LLSyncManager getInstance() {
        return ourInstance;
    }

    private String myUsername;

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
        myUsername = "Ilegal user!!!";

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
        this.myUsername = username;

        // Init the users:
        List<LLUser> users = LLUser.listAll(LLUser.class);
        for(int i = 0; i < users.size(); ++i)
        {
            LLUser u = users.get(i);
            this.usersList.put(u.getId(), u);
        }

        // TODO: Do the other classes
    }

    private LLUser userWithJSON(JSONObject obj)
    {
        LLUser u = null;

        try
        {
            String uname = obj.getString("username");
            u = new LLUser(uname);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }

        return u;
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

                String thisUserJSONName = userJSON.getString("username");

                // Skip our own username
                if( (!(thisUserJSONName.equals(this.myUsername)) && (thisUserJSONName != null)) )
                {
                    LLUser userToSync = null;

                    // Search for an existing user
                    Iterator<Long> userIterator = this.usersList.keySet().iterator();

                    while (userIterator.hasNext())
                    {
                        Long theID = userIterator.next();

                        // Get the value:
                        LLUser searchUser = this.usersList.get(theID);

                        // Search for one inside our thing.
                        if(searchUser.getUsername().equals(thisUserJSONName))
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

    private void postMessage()
    {
        // Attempt to post:
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.add("hola", "mundo");
        StringEntity payload = null;
        try {

            payload = new StringEntity("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.addHeader("Content-Type", "application/x-www-urlencoded");
        client.setBasicAuth("jose", "polo&xzaz");
        client.post("http://llau.systems/api/new", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });
    }
}

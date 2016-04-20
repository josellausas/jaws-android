package systems.llau.jaws.Base;

import org.json.JSONObject;

/**
 * Created by pp on 4/18/16.
 * Interface for sending something to the server
 */
public interface LLSendable
{
    /**
     * Required.
     * Convert the object to a json object and return
     * @return The JSON representation of the object
     */
    public JSONObject toJSON();

    /**
     * Required.
     * Updates the current object with a JSON
     * @param json
     */
    public void updateWithJSON(JSONObject json);

}

package systems.llau.jaws.Base;
import android.util.Base64;
import android.util.Log;

import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import systems.llau.jaws.LLau.LLTask;

/**
 * @class AppManager
 * @description The app's manager. Controls everything
 */
public class AppManager
{
    /**
     * The Singleton instance variable
     */
    private static AppManager ourInstance = new AppManager();

    /**
     * Singleton access
     * @return An instance of the singleton.
     */
    public static AppManager getInstance()
    {
        return ourInstance;
    }

    /**
     * Default Constructor
     */
    private AppManager()
    {
        // Default constructor
    }

    /**
     * Returns the selected Task
     * @return The task that was selected by the user
     */
    public LLTask getSelectedTask() {
        return selectedTask;
    }


    /**
     * Sets the task as selected by the User
     * @param selectedTask The Task that was selected
     */
    public void setSelectedTask(LLTask selectedTask)
    {
        this.selectedTask = selectedTask;
    }

    private LLTask selectedTask = null;

    /**
     * Stores the secret key used to communicate with the server
     */
    private String secretKey;
    public void setSecretKey(String k)
    {
        this.secretKey = k;
    }
    public String getSecretKey()
    {
        return this.secretKey;
    }


    public void ListSupportedAlgorithms()
    {
        String result = "";

        // get all the providers
        Provider[] providers = Security.getProviders();

        for (int p = 0; p < providers.length; p++) {
            // get all service types for a specific provider
            Set<Object> ks = providers[p].keySet();
            Set<String> servicetypes = new TreeSet<String>();
            for (Iterator<Object> it = ks.iterator(); it.hasNext(); ) {
                String k = it.next().toString();
                k = k.split(" ")[0];
                if (k.startsWith("Alg.Alias."))
                    k = k.substring(10);

                servicetypes.add(k.substring(0, k.indexOf('.')));
            }

            // get all algorithms for a specific service type
            int s = 1;
            for (Iterator<String> its = servicetypes.iterator(); its.hasNext(); ) {
                String stype = its.next();
                Set<String> algorithms = new TreeSet<String>();
                for (Iterator<Object> it = ks.iterator(); it.hasNext(); ) {
                    String k = it.next().toString();
                    k = k.split(" ")[0];
                    if (k.startsWith(stype + "."))
                        algorithms.add(k.substring(stype.length() + 1));
                    else if (k.startsWith("Alg.Alias." + stype + "."))
                        algorithms.add(k.substring(stype.length() + 11));
                }

                int a = 1;
                for (Iterator<String> ita = algorithms.iterator(); ita.hasNext(); ) {
                    result += ("[P#" + (p + 1) + ":" + providers[p].getName() + "]" +
                            "[S#" + s + ":" + stype + "]" +
                            "[A#" + a + ":" + ita.next() + "]\n");
                    a++;
                }

                s++;
            }
        }
    }

    static final String TAG = "SymmetricAlgorithmAES";

    public String encode(String toEncode)
    {

        // Original text
        String theTestText = "This is just a simple test";

        // Set up secret key spec for 128-bit AES encryption and decryption
        SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }

        // Encode the original data with AES
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(theTestText.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error");
        }

        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);

    }

    public String decode(byte[] todecode)
    {
        // Set up secret key spec for 128-bit AES encryption and decryption
        SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }
        // Decode the encoded data with AES
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            decodedBytes = c.doFinal(todecode);
        } catch (Exception e) {
            Log.e(TAG, "AES decryption error");
        }

        return new String(decodedBytes);
    }



}

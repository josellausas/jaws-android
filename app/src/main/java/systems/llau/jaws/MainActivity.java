package systems.llau.jaws;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import net.danlew.android.joda.JodaTimeAndroid;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONException;
import org.json.JSONObject;

import systems.llau.jaws.Base.LLLocation;
import systems.llau.jaws.Base.LLNotification;
import systems.llau.jaws.layout.DashboardFragment;
import systems.llau.jaws.layout.GeoFencesFragment;
import systems.llau.jaws.layout.LocationListFragment;
import systems.llau.jaws.layout.MessagesFragment;
import systems.llau.jaws.layout.TaskListFragment;
import systems.llau.jaws.layout.UserListFragment;
import java.util.ArrayList;

/**
 * MyMQTTMessage
 * =============
 * Abtracts a MQTT message.
 * It encrypts and encodes the message so it is ready to go over the internet.
 *
 */
class MyMQTTMessage
{
    public String     channel;      /** The Channel where the messages get published */
    public JSONObject payload;      /** The message payload that gets encrypted */

    /**
     * Custom Constructor
     * ------------------
     * Creates a new MQTT message to be delivered to the default channel
     *
     * @param channel The channel to send to
     * @param object The JSON object to send to the server
     */
    public MyMQTTMessage(String channel, JSONObject object)
    {
        this.channel = channel;
        this.payload = object;
    }
}

/**
 * MainActivity
 * ============
 * The app's main activity.
 * Implements a GoogleApiClient
 * Implements a NavigationView
 */
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{

    /** The mqtt clien */
    private MqttAsyncClient          mqtt                   = null;
    /** The mqtt connect options */
    private MqttConnectOptions       options                = null;
    /** Stores the failed messages */
    private ArrayList<MyMQTTMessage> failedMessages         = null;
    /** Identifies our client to the server */
    private String                   identifier             = null;
    /** The google API Client */
    private GoogleApiClient          apiClient              = null;
    /** The last known GPS location */
    private Location                 lastLocation           = null;

    /** Keeps track of the last fragment that was active */
    private Fragment previosFragmentActive  = null;
    public void setPreviousFragmentActive(Fragment f){this.previosFragmentActive = f;}


    /**
     * Gets the phone's unique identifier and returns it.
     * @return The phone's unique identifier
     */
    private String getPhoneIdentifier()
    {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }


    /**
     * Sends a message to the server.
     * @param severity The level of importance of the message
     * @param msg   The message payload to send
     */
    private void notifyServer(int severity, String msg)
    {
        if(identifier == null)
        {
            // Get the information from the telephone and register it with MQTT
            identifier = getPhoneIdentifier();
        }

        // Creates a new notification to be sent
        LLNotification note = new LLNotification(severity, msg, identifier);

        // Publish as a json string
        this.publishMessage("notify/device", note.toJSON());
    }


    /// Publishes a message to MQTT
    private void publishMessage(final String channel, final JSONObject msg)
    {
        try
        {
            // Create an mqtt
            if(mqtt == null)
            {
                Log.i("Main", "Creating mqtt ");
                // Create the mqtt
                String uri = "tcp://m10.cloudmqtt.com:11915";

                mqtt = new MqttAsyncClient(uri, "elcliente2001", null);

                options = new MqttConnectOptions();
                options.setCleanSession(true);
                options.setUserName("device01");
                options.setPassword("device01".toCharArray());
                String lastWillMessage = "androidDevice offline";
                // Config the thing
                options.setWill("v1/status/androidDevice", lastWillMessage.getBytes(), 1, true);
            }

            // Check if its connected
            if(mqtt.isConnected() == false)
            {
                Log.i("Main", "Connecting mqtt ");
                mqtt.connect(options, getApplicationContext(), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken)
                    {
                        try
                        {
                            mqtt.publish("v1/status/androidDevice", "androidDevice online".getBytes(), 2, true);
                            mqtt.publish("v1/" + channel ,msg.toString().getBytes(), 1, false);
                        }
                        catch (MqttPersistenceException e)
                        {
                            e.printStackTrace();
                            messageHasFailed(channel, msg);
                        }
                        catch (MqttSecurityException e)
                        {
                            e.printStackTrace();
                            messageHasFailed(channel, msg);
                        }
                        catch (MqttException e)
                        {
                            e.printStackTrace();
                            messageHasFailed(channel, msg);
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                    {
                        exception.printStackTrace();
                        messageHasFailed(channel, msg);
                    }
                });
            }
            else
            {
                try
                {
                    mqtt.publish("v1/" + channel, msg.toString().getBytes(), 1, false);
                }
                catch (MqttPersistenceException e)
                {
                    e.printStackTrace();
                    messageHasFailed(channel, msg);
                }
                catch (MqttSecurityException e)
                {
                    e.printStackTrace();
                    messageHasFailed(channel, msg);
                }
                catch (MqttException e)
                {
                    e.printStackTrace();
                    messageHasFailed(channel, msg);
                }
            }
        }
        catch (MqttPersistenceException e)
        {
            e.printStackTrace();
            messageHasFailed(channel, msg);
        }
        catch (MqttSecurityException e)
        {
            e.printStackTrace();
            messageHasFailed(channel, msg);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
            messageHasFailed(channel, msg);
        }
    }

    /// Callback for when the message has failed
    private void messageHasFailed(String chan,JSONObject msg)
    {
        // Creates a message instance and adds it to the failed message list
        MyMQTTMessage failedOne = new MyMQTTMessage(chan, msg);
        this.failedMessages.add(failedOne);
    }


    @Override protected void onCreate(Bundle savedInstanceState)
    {
        // Do the thing
        super.onCreate(savedInstanceState);

        // Initialize the Joda time
        JodaTimeAndroid.init(this);

        // Start with a clean failed messages list
        this.failedMessages = new ArrayList<MyMQTTMessage>();

        // Inflate the view.
        setContentView(R.layout.activity_main);

        // Setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Activity drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(this.previosFragmentActive != null)
        {
            showFragmentNow(previosFragmentActive);
        }
        else
        {
            // This is the default fragment to show:
            DashboardFragment dashboardFragment = new DashboardFragment();
            showFragmentNow(dashboardFragment);
        }

        // Setup google services
        this.checkPlayServices();

        if(apiClient == null) {
            apiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        if(savedInstanceState == null)
        {
            SharedPreferences settings = getSharedPreferences("JawsPreferences", 0);
            boolean isInstalled = settings.getBoolean("installed", false);
            if(isInstalled)
            {
                // Install the thing
                installApp();
            }
        }
    }

    private void installApp()
    {
        // Get the information from the telephone and register it with MQTT
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String imsi     = telephonyManager.getSubscriberId();
        String deviceId = telephonyManager.getDeviceId();
        String version  = telephonyManager.getDeviceSoftwareVersion();
        String operator = telephonyManager.getNetworkOperatorName();
        String number   = telephonyManager.getLine1Number();

        JSONObject json = new JSONObject();

        try
        {
            json.put("a", imsi);
            json.put("b", deviceId);
            json.put("c", version);
            json.put("d", operator);
            json.put("e", number);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        SharedPreferences settings = getSharedPreferences("JawsPreferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("installed", true);

        // Commit the edits!
        editor.commit();
        publishMessage("notify/device", json);
    }

    private void showFragmentNow(Fragment frag)
    {
        FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_fragment_container, frag);
        transaction.commit();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart()
    {
        apiClient.connect();
        super.onStart();

        SharedPreferences settings = getSharedPreferences("JawsPreferences", 0);
        String lastClass = settings.getString("lastFragment", null);

        if(lastClass != null)
        {
            if(lastClass.equals(TaskListFragment.class.toString()))
            {
                setTitle("Task List");
                this.previosFragmentActive = new TaskListFragment();
            }


        }

        if(this.previosFragmentActive != null)
        {

            showFragmentNow(previosFragmentActive);
        }
        else
        {
            // This is the default fragment to show:
            DashboardFragment dashboardFragment = new DashboardFragment();
            showFragmentNow(dashboardFragment);
        }


    }

    @Override
    public void onStop()
    {
        Fragment f = this.previosFragmentActive;
        apiClient.disconnect();
        super.onStop();
    }

    /*
     * Sets up the Settings menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDestroy()
    {
        if(this.previosFragmentActive != null)
        {
            SharedPreferences settings = getSharedPreferences("JawsPreferences", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("lastFragment", previosFragmentActive.getClass().toString());

            // Commit the edits!
            editor.commit();
            Log.e("SAVE", "Saving this string: " + previosFragmentActive.getClass().toString());
        }


        super.onDestroy();
    }


    /*
     * Handles options selections
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
     *  Handles the Menu selection
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        // Menu switch
        switch (id)
        {
            case R.id.nav_dashboard:
            {
                notifyServer(0, "Started Dashboard");
                setTitle("Dashboard");
                DashboardFragment newFragment = new DashboardFragment();
                showFragmentNow(newFragment);

            } break;

            case R.id.nav_users:
            {
                setTitle("Users");
                UserListFragment newFragment = new UserListFragment();
                showFragmentNow(newFragment);
            }break;

            case R.id.nav_tasks:
            {
                notifyServer(0, "Stated tasks");
                setTitle("Tasks");
                TaskListFragment taskListFragment = new TaskListFragment();
                showFragmentNow(taskListFragment);

            }break;

            case R.id.nav_organizations:
            {
                setTitle("Organizations");
                // TODO: Implement

            }break;

            case R.id.nav_dev_tools:
            {
                setTitle("Dev Tools");
                // TODO: Implement

            }break;

            case R.id.nav_messages:
            {
                setTitle("Messages");
                MessagesFragment msgFragment = new MessagesFragment();
                showFragmentNow(msgFragment);
            }break;

            case R.id.nav_geofences:
            {
                setTitle("Geo-fences");
                GeoFencesFragment fragment = new GeoFencesFragment();
                showFragmentNow(fragment);
            }break;

            case R.id.location_log:
            {
                setTitle("Locations");
                LocationListFragment frag = new LocationListFragment();
                showFragmentNow(frag);
            } break;


            default:
            {
                Log.e("MainActivity", "Menu ID Unkwown");
            }
        }
        return true;
    }


    private boolean checkPlayServices()
    {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        return true;
    }

    @Override
    public void onConnectionSuspended(int error)
    {
        // The connection got suspended
    }

    /// Gets the last known location
    public Location getLastLocation()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);

            LLLocation instancedLocation = new LLLocation(lastLocation);
            instancedLocation.save();

            String locationStr = "Loc: " + lastLocation.getLatitude() + " - " + lastLocation.getLongitude();
            Log.e("LOC", locationStr);
            return lastLocation;
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return lastLocation;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 0:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getLastLocation();
                }
            }break;
        }
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.e("TAG", "Hola");
        Location uno = this.getLastLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
    }
}

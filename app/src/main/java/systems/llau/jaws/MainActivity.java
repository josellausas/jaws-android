package systems.llau.jaws;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.android.gms.common.GooglePlayServicesUtil;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONException;
import org.json.JSONObject;
import systems.llau.jaws.Base.LLNotification;
import systems.llau.jaws.layout.DashboardFragment;
import systems.llau.jaws.layout.GeoFencesFragment;
import systems.llau.jaws.layout.MessagesFragment;
import systems.llau.jaws.layout.TaskListFragment;
import systems.llau.jaws.layout.UserListFragment;
import java.util.ArrayList;

class MyMQTTMessage
{
    public String channel;
    public JSONObject payload;

    public MyMQTTMessage(String c, JSONObject o)
    {
        this.channel = c;
        this.payload = o;
    }
}

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private MqttAsyncClient    mqtt     = null;
    private MqttConnectOptions options  = null;
    private ArrayList<MyMQTTMessage> failedMessages = null;
    private String identifier = null;

    private void notifyServer(int severity, String msg)
    {
        if(identifier == null)
        {
            // Get the information from the telephone and register it with MQTT
            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            identifier = telephonyManager.getSubscriberId();
        }
        LLNotification note = new LLNotification(severity, msg, identifier);
        // Publish as a json string
        this.publishMessage("notify/device", note.toJSON());
    }


    private void publishMessage(final String channel, final JSONObject msg)
    {

        Log.i("Main", "Publishing " + msg);
        try
        {
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

    private void messageHasFailed(String chan,JSONObject msg)
    {
        // Creates a message instance and adds it to the failed message list
        MyMQTTMessage failedOne = new MyMQTTMessage(chan, msg);
        this.failedMessages.add(failedOne);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Start with a clean failed messages list
        this.failedMessages = new ArrayList<MyMQTTMessage>();

        // TODO: Load from disk the last failed messages

        // Inflate the view.
        setContentView(R.layout.activity_main);

        // Setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Activity drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // This is the default fragment to show:
        DashboardFragment dashboardFragment = new DashboardFragment();
        showFragmentNow(dashboardFragment);

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

        // Check for installation:





    }

    private void installApp()
    {
        // Get the information from the telephone and register it with MQTT
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        String deviceId = telephonyManager.getDeviceId();
        String version = telephonyManager.getDeviceSoftwareVersion();
        String operator = telephonyManager.getNetworkOperatorName();
        String number = telephonyManager.getLine1Number();


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


            default:
            {
                Log.e("MainActivity", "Menu ID Unkwown");
            }
        }
        return true;
    }


}

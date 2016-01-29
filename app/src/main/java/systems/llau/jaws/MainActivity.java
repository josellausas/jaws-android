package systems.llau.jaws;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import systems.llau.jaws.layout.DashboardFragment;
import systems.llau.jaws.layout.MessagesFragment;
import systems.llau.jaws.layout.TaskListFragment;
import systems.llau.jaws.layout.UserListFragment;


import java.util.Enumeration;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    MqttAsyncClient mqtt = null;

    private void publishMessage(final String msg)
    {

        Log.i("Main", "Publishing " + msg);
        try
        {
            if(mqtt == null)
            {
                Log.i("Main", "Creating mqtt ");
                // Create the mqtt
                String uri = "tcp://m10.cloudmqtt.com:11915";

                mqtt = new MqttAsyncClient(uri, "device01", null);

                MqttConnectOptions option = new MqttConnectOptions();
                option.setUserName("device01");
                option.setPassword("device01".toCharArray());
                String lastWillMessage = "androidDevice offline";
                // Config the thing
                option.setWill("v1/status/androidDevice", lastWillMessage.getBytes(), 2, true);
            }


            if(mqtt.isConnected() == false)
            {
                Log.i("Main", "Connecting mqtt ");
                mqtt.connect(getApplicationContext(), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken)
                    {
                        try
                        {
                            mqtt.publish("v1/status/androidDevice", "androidDevice online".getBytes(), 2, true);
                            mqtt.publish("v1/notify/server",msg.getBytes(), 1, true);
                        }
                        catch (MqttPersistenceException e)
                        {

                            e.printStackTrace();
                        }
                        catch (MqttSecurityException e)
                        {
                            e.printStackTrace();
                        }
                        catch (MqttException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                    {
                        exception.printStackTrace();
                    }
                });
            }
            else
            {
                try
                {
                    mqtt.publish("v1/status/androidDevice", "androidDevice online".getBytes(), 2, true);
                    mqtt.publish("v1/notify/server", msg.getBytes(), 1, true);
                }
                catch (MqttPersistenceException e)
                {

                    e.printStackTrace();
                }
                catch (MqttSecurityException e)
                {
                    e.printStackTrace();
                }
                catch (MqttException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (MqttPersistenceException e)
        {
            e.printStackTrace();
        }
        catch (MqttSecurityException e)
        {
            e.printStackTrace();
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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


        publishMessage("Started app");

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
                publishMessage("androidDevice: Stated Dashboard");
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
                publishMessage("androidDevice: Stated tasks");
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


            default:
            {
                Log.e("MainActivity", "Menu ID Unkwown");
            }
        }




        return true;
    }
}

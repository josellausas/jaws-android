package systems.llau.jaws;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import cz.msebera.android.httpclient.Header;
import io.fabric.sdk.android.Fabric;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.loopj.android.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import systems.llau.jaws.LLau.LLSyncManager;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>
{
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText             mPasswordView;
    private View                 mProgressView;
    private View                 mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Initialize crashlytics
        Fabric.with(this, new Crashlytics());

        // Set the UI view
        setContentView(R.layout.activity_login);

        // Setup the UI
        this.setupUI();

        // Request Permissions for contacts
        populateAutoComplete();
    }


    /**
     * Setup the user's graphic interface
     */
    private void setupUI()
    {
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        // Setup the UI
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            // Handles the action for password
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // Sets up the login butto0n
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    /**
     * Checks for previously saved user and password and complets the infor in the GUI
     */
    private void populateAutoComplete()
    {
        if (!mayRequestContacts())
        {
            Crashlytics.getInstance().log("No permission to request contacts");
            Crashlytics.getInstance().crash();
            return;
        }

        getLoaderManager().initLoader(0, null, this);

        // Load from prefs
        SharedPreferences settings = getSharedPreferences("JawsPreferences", 0);
        String usr =settings.getString("username", null);   // Gets the user string
        if(usr != null)
        {
            String password = settings.getString("password", null);

            this.mEmailView.setText(usr.toString());
            this.mPasswordView.setText(password.toString());
        }


    }

    /**
     * Save valid credentials to use later
     * @param username User's name
     * @param password User's password
     */
    private void saveCredentials(String username, String password)
    {
        SharedPreferences settings = getSharedPreferences("JawsPreferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.putString("password", password);

        // If we come from login, we show the default views.
        editor.remove("lastFragment");

        //------------------------
        // Save the changes last!
        editor.commit();
    }

    /**
     * Checks if we have permission to request contacts
     * @return True if permission is good
     */
    private boolean mayRequestContacts()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS))
        {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        boolean checkForEmail = false;

        // Dismisses the keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.mEmailView.getWindowToken(), 0);

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email    = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        else if ((!isEmailValid(email)) && checkForEmail)
        {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            showProgress(true);

            getPermissionFromServer(email, password);
        }
    }

    private boolean isEmailValid(String email)
    {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password)
    {
        //TODO: Replace this with your own logic
        return password.length() > 4;

        // TODO: Check for evilness here
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        // What is this for?
    }

    /**
     * Adds an email to the autocomplete list
     * @param emailAddressCollection The email to be added
     */
    private void addEmailsToAutoComplete(List<String> emailAddressCollection)
    {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * It's good to know yourself
     * @return Yourself
     */
    private LoginActivity getMyself()
    {
        return this;
    }


    /**
     * Hashes the password with Sha256
     * @param pass The password to hash
     * @return The hash or null
     */
    private String hashPass(String pass)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pass.getBytes());
            byte[] bytes = md.digest();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < bytes.length; i++)
            {
                // Convert to hexthing
                String tmp = Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
                buffer.append(tmp);
            }
            return buffer.toString();
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Attempts login
     * Converts the username to a URLEncode(base64(x)) string
     * Converts the password to a URLEncode(base64(sha256(y))) string
     * This is the last step where the password is naked
     * @param username Username as input by the user
     * @param password Password as input by the user
     */
    private void getPermissionFromServer(final String username, final String password)
    {
        // This is the server's URL
        String loginURL = getString(R.string.url_login);

        //----------------------------------------------
        // Requests the tasks from the server
        // WARNING: This bypasses SSL certificate checking
        // TODO: Remove this in production UNSAFE as fuck!
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        RequestParams   params = new RequestParams();
        try
        {
            // Convert to base64 the username
            byte[] userData = username.getBytes("UTF-8");
            String base64username = Base64.encodeToString(userData, Base64.DEFAULT);

            // Hash and base64 the password
            byte[] passData = hashPass(password).getBytes("UTF-8");
            String base64Pass = Base64.encodeToString(passData, Base64.DEFAULT);


            // Encode the strings
            String encodedUser = URLEncoder.encode(base64username);
            params.add("user", encodedUser);

            // Encode the strings
            String encodedPass = URLEncoder.encode(base64Pass , "utf-8");
            params.add("pass", encodedPass);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        // Do the actual POST request
        client.post(getApplicationContext(), loginURL, params, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response)
            {
                showProgress(false);

                // Needed for everything to work:
                LLSyncManager.getInstance().init(username);

                boolean success = LLSyncManager.getInstance().syncUsers(response);


                LoginEvent e = new LoginEvent();

                e.putSuccess(true);

                Answers.getInstance().logLogin(e);

                // Show the drawer activity
                if (success == true) {
                    // App success behavior
                    saveCredentials(username, password);

                    Toast.makeText(getMyself(), "Login OK!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed Initial Sync! Please retry", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
            {
                Crashlytics.getInstance().logException(throwable);

                showProgress(false);

                Log.e("HTTP", "Error: " + statusCode);
                throwable.printStackTrace();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse)
            {

                Crashlytics.getInstance().logException(throwable);
                showProgress(false);

                Log.e("HTTP", "Error: " + statusCode);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
            {
                Crashlytics.getInstance().logException(throwable);
                showProgress(false);

                Log.e("HTTP", "Error: " + statusCode);
                throwable.printStackTrace();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        });
    }
}


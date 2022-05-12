package org.webworks.datatool.Activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webworks.datatool.BuildConfig;
import org.webworks.datatool.Fragment.HIVTestingFragment;
import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.FingerPrintRepository;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Repository.UserRepository;
import org.webworks.datatool.Session.SessionManager;
import org.webworks.datatool.Utility.ApiGetFacility;
import org.webworks.datatool.Utility.BindingMeths;
import org.webworks.datatool.Utility.UtilFuns;
import org.webworks.datatool.Web.Connectivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TestingActivity extends SessionManager
        implements NavigationView.OnNavigationItemSelectedListener, HIVTestingFragment.OnFragmentInteractionListener {
    Context context;
    UserRepository userRepository;
    FingerPrintRepository fingerPrintRepository;
    DrawerLayout container;
    SweetAlertDialog syncProgress;
    private String PREFS_NAME;
    private String PREF_USER_GUID;
    private String PREF_SPOKE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_testing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Entries");
        userRepository = new UserRepository(context);
        fingerPrintRepository = new FingerPrintRepository(context);

        PREFS_NAME = context.getResources().getString(R.string.pref_name);
        PREF_USER_GUID = context.getResources().getString(R.string.pref_user);
        PREF_SPOKE_ID = getString(R.string.pref_spoke_id);

        FloatingActionButton fab = findViewById(R.id.fab_testing);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TestingFormActivity.class);
                startActivity(intent);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_testing);
        navigationView.setNavigationItemSelectedListener(this);

        container = (DrawerLayout) findViewById(R.id.drawer_layout);

        initializeDefaultFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        /*initializeViews();
        setClickListeners();
        assignValues();*/
        super.onResume();

        Intent intent;
        if (!userRepository.userSessionValid()) {
            finish();
            intent = new Intent(context, LoginActivity.class);
            intent.putExtra("SESSION-LOGIN", 1);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.testing, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(context, SearchClientActivity.class)));

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();



        if (id == R.id.nav_logout){
            Intent intent;
            intent = new Intent(context, LoginActivity.class);
            userRepository.updateUserSession(1);
            intent.putExtra("SESSION-LOGIN", 1);
            startActivity(intent);
        }

        if (id == R.id.nav_refresh_facilities){
            Toast.makeText(context, "Facility list will be updated", Toast.LENGTH_LONG).show();
            new ApiGetFacility(context).execute();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            finish();
            Intent intent = new Intent(context, TestingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_synchronize) {

            syncProgress = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            syncProgress.setTitleText("Synchronizing");
            syncProgress.setContentText("Wait while we synchronize your records");
            syncProgress.setCancelable(true);
            syncProgress.show();
            ReferralFormRepository referralFormRepository = new ReferralFormRepository(context);
            ArrayList<ClientForm> clientReferralForms = referralFormRepository.getNonePostedSampleForms();

            if(clientReferralForms.size() > 0) {
                if (Connectivity.isConnected(context)) {
                    new PostForms().execute(postForm(clientReferralForms));
                } else {
                    if(syncProgress.isShowing()) syncProgress.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Status")
                            .setContentText(getString(R.string.not_connected, "To perform this synchronize"))
                            .show();
                    //Toast.makeText(context, getString(R.string.not_connected, "To perform this synchronize"), Toast.LENGTH_LONG).show();
                }
            }
            else {
                if(syncProgress.isShowing()) syncProgress.dismiss();
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Status")
                        .setContentText(getString(R.string.no_form_for_sync))
                        .show();
                //Toast.makeText(context, getString(R.string.no_form_for_sync), Toast.LENGTH_LONG).show();
            }

            //just added this here in case tester fail to sync contacts
            //syncContacts();

            /*if (Connectivity.isConnected(context)) {
                syncFormDetails();
            }*/
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Method initializes the testing fragment as default
     * */
    private void initializeDefaultFragment() {
        HIVTestingFragment hivTestingFragment = new HIVTestingFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.form_fragment_container, hivTestingFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onRssItemSelected(String x) {

    }

    private String postForm(ArrayList<ClientForm> forms) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString(PREF_USER_GUID, "");
        int spokeID = sharedPreferences.getInt(PREF_SPOKE_ID, 0);
        JSONArray array=new JSONArray();

        for (int i = 0; i < forms.size(); i++){
            ClientForm form = forms.get(i);
            JSONObject json = new JSONObject();
            try {
                json.put("user_id", userID);
                json.put("spoke_id", spokeID == 0 ? "" : spokeID );
                json.put("form_id", form.getId());
                json.put("firstname", form.getClientName());
                json.put("surname", form.getClientLastname());
                json.put("code", form.getClientCode());
                json.put("client_identifier",form.getClientIdentifier());
                json.put("finger_print", fingerPrintRepository.getClientFingerPrints(form.getClientIdentifier()));

                array.put(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  array.toString();
    }

    private class PostForms extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... param) {
            // Create data variable for sent values to server
            String data = param[0];
            String text = "";
            BufferedReader reader;
            // Send data
            try
            {
                // Defined URL  where to send data
                URL url = new URL(getResources().getString(R.string.api_url) + getString(R.string.post_referrals));
                // Send POST data request
                HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                // Read Server Response
                while((line = reader.readLine()) != null)
                {   // Append server response in string
                    sb.append(line);
                }
                text = sb.toString();
                conn.disconnect();
                return text;
            }
            catch(Exception e) {
                return "Error";
            }
        }

        protected void onPostExecute(String result) {
             if(!result.equals(null) && !result.isEmpty() && !result.equals("Error")) {
                ArrayList<String> ids = UtilFuns.getUploadedId(result);
                for (int i = 0; i < ids.size(); i++) {
                    ReferralFormRepository referralFormRepository = new ReferralFormRepository(context);
                    String[] id = ids.get(i).split(":");
                    if (!id[0].equals("")) {
                        referralFormRepository.updateUploadedFromApi(id[1], Integer.parseInt(id[0]));
                        //Toast.makeText(context, getString(R.string.sync_success), Toast.LENGTH_LONG).show();
                        if(syncProgress.isShowing()) syncProgress.dismiss();
                        if (!isFinishing()) {
                            try {
                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Status")
                                        .setContentText(getString(R.string.sync_success))
                                        .show();
                            } catch (WindowManager.BadTokenException e) {
                                Log.e("WindowManagerBad ", e.toString());
                            }
                        }
                    }
                }
            }
            else {
                if(syncProgress.isShowing()) syncProgress.dismiss();
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Status")
                        .setContentText(getString(R.string.sync_error))
                        .show();
            }
            super.onPostExecute(result);
        }
    }
}

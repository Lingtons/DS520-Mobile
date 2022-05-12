package org.webworks.datatool.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.webworks.datatool.Model.User;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.UserRepository;
import org.webworks.datatool.Utility.ApiGetFacility;
import org.webworks.datatool.Utility.UtilFuns;
import org.webworks.datatool.Web.Connectivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import cn.pedant.SweetAlert.SweetAlertDialog;
public class LoginActivity extends AppCompatActivity {
    Context context;
    private String PREFS_NAME;
    private String PREF_USER_GUID;
    private String PREF_USER_EMAIL;
    private String PREF_USER_PASSWORD;
    private String PREF_FACILITY_GUID;
    private String PREF_STATE_CODE;
    private String PREF_FACILITY_NAME;
    private String PREF_SPOKE_ID;
    EditText userName, password;
    Button loginButton;
    private ProgressDialog progress;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        PREFS_NAME = this.getResources().getString(R.string.pref_name);
        PREF_USER_GUID = this.getResources().getString(R.string.pref_user);
        PREF_USER_PASSWORD = context.getString(R.string.pref_password);
        PREF_FACILITY_GUID = this.getResources().getString(R.string.pref_facility);
        PREF_USER_EMAIL =context.getString(R.string.pref_user_email);
        PREF_FACILITY_NAME = this.getResources().getString(R.string.pref_facility_name);
        PREF_STATE_CODE = getString(R.string.pref_state_code);
        PREF_SPOKE_ID = getString(R.string.pref_spoke_id);
        user = new User();

        userName = findViewById(R.id.txt_user_name);
        password = findViewById(R.id.txt_password);
        loginButton = findViewById(R.id.btn_login);


        //show the login dialog form
        Intent intent = getIntent();
        if (intent.hasExtra("SESSION-LOGIN")) {
            showSessionLoginForm();
        }
        else {
            showLoginForm();
        }
    }
    /**
     * Method displays the login form and logs user in
     * */
    private void showSessionLoginForm() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLoginForm()) {
                    User sessionUser = new User();
                    sessionUser.setEmail(userName.getText().toString().trim());
                    sessionUser.setPassword(password.getText().toString().trim());
                    UserRepository userRepository = new UserRepository(context);
                    if (userRepository.sessionUserExists(sessionUser)) {
                        userRepository.updateUserSession(0);
                        finish();
                        Intent intent = new Intent(context, TestingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(context, getString(R.string.login_check_online), Toast.LENGTH_LONG).show();
                        //get the user input and sends to the server
                        progress=new ProgressDialog(context);
                        progress.setMessage(getString(R.string.login_dialog_message));
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.show();

                        user.setEmail(userName.getText().toString().trim());
                        user.setPassword(password.getText().toString().trim());
                        if (Connectivity.isConnected(context)){
                            new Login().execute(PostLoginForm(user));
                        }else{
                            Toast.makeText(context, getString(R.string.no_internet_connect), Toast.LENGTH_LONG).show();
                            if (progress.isShowing()) progress.dismiss();
                        }
                    }
                }
            }
        });
    }
    /**
     * Method displays the login form and posts login to server
     * */
    private void showLoginForm() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TestingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

//                if (validateLoginForm()) {
//                    if (Connectivity.isConnected(context)) {
//                        progress=new ProgressDialog(context);
//                        progress.setMessage(getString(R.string.login_dialog_message));
//                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                        progress.setIndeterminate(true);
//                        progress.show();
//                        //get the user input and sends to the server
//                        user.setEmail(userName.getText().toString().trim());
//                        user.setPassword(password.getText().toString().trim());
//                        new Login().execute(PostLoginForm(user));
//
//                        new ApiGetFacility(context).execute();
//                    }
//                    else {
//                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText("Status")
//                                .setContentText(getString(R.string.not_connected, "to login"))
//                                .show();
//                    }
//                }
            }
        });
    }
    /**
     * Method validate the login form by checking for correct and non-empty email and password fields
     * password is registered users phone number
     * */
    public boolean validateLoginForm() {
        if(userName.getText().toString().equals("")) {
            Toast.makeText(context, getString(R.string.validate_error, "Email"), Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.getText().toString().equals("")) {
            Toast.makeText(context, getString(R.string.validate_error, "Password"), Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.getText().toString().trim().length() < 5) {
            Toast.makeText(context, getString(R.string.short_input, "Password"), Toast.LENGTH_LONG).show();
            return false;
        }
//        if(!isPhoneNumberValid(password.getText().toString().trim())) {
//            Toast.makeText(context, getString(R.string.invalid_input, "Password"), Toast.LENGTH_LONG).show();
//            return false;
//        }
        return true;
    }
    /**
     * Method uses regular expression to check email to comform to user@user.com
     * */
    private boolean isEmailValid(String email) {
        return email.matches("^[_A-Za-z0-9]+(\\.[_A-Za-z0-9]+)*@[_A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }
    /**
     * Method check for correct phone input using regular expression
     * */
    private boolean isPhoneNumberValid(String number) {
        return number.matches("^[0-9]*$");
    }
    /**
     * Method posts converts user input to json ready to b posted to the server
     * */
    private String PostLoginForm(User user) {
        JSONObject json = new JSONObject();
        try {
            json.put("username", user.getEmail());
            json.put("password", user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  json.toString();
    }
    /**
     * Class posts user input to the server and gets response
     * if user exists, Landing screen(Landing Activity will be shown)
     * else login page stays
     * */
    private class Login extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... param) {
            String data = param[0];
            String text = "";
            BufferedReader reader=null;
            try
            {
                URL url = new URL(getResources().getString(R.string.api_url) + getResources().getString(R.string.api_login));
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null)
                {
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
            String cleanedResult = UtilFuns.cleanResult(result);
            if(cleanedResult.equals("current_facility_error")){
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Status")
                        .setContentText("No Facility Assigned")
                        .show();
            }
            else if (!(result.equals(null) || result.isEmpty()) && !result.equals("Error") &&!cleanedResult.equals("Error") && !cleanedResult.equals("current_facility_error") && !cleanedResult.equals("Invalid Login Credential")) {
                try {
                    JSONObject jobject = new JSONObject(result);
                    //User user = new User();
                    JSONObject userData = (JSONObject) jobject.get("data");

                    user.setEmail(userData.getString("email"));
                    user.setGuid(userData.getString("id"));
                    String password = userData.getString("phone");
                    user.setFacility(userData.getString("facility_id"));
                    int spoke_id;
                    try{
                        spoke_id = userData.getInt("spoke_id");
                    }catch (Exception e){
                        spoke_id = 0;
                    }

                    String facility_name = null;
                            String state_code = null;
                    if (user.getFacility() != null){
                        JSONObject facilityObj = new JSONObject(userData.getString("facility"));
                        facility_name = facilityObj.getString("name");
                        state_code = facilityObj.getString("state_code");
                    }

                    UserRepository userRepository = new UserRepository(context);
                    userRepository.addUser(user);
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    sharedPreferences.edit().putString(PREF_USER_GUID, user.getGuid()).putString(PREF_USER_EMAIL, user.getEmail()).putString(PREF_FACILITY_GUID, user.getFacility()).putString(PREF_USER_PASSWORD, password).putString(PREF_FACILITY_NAME, facility_name).putString(PREF_STATE_CODE, state_code).putInt(PREF_SPOKE_ID, spoke_id).apply();

                    Intent intent = new Intent(context, TestingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, getString(R.string.login_error), Toast.LENGTH_LONG).show();
                }

            }
            else {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Status")
                        .setContentText(getString(R.string.login_error))
                        .show();
            }
            if (progress.isShowing()) progress.dismiss();
            super.onPostExecute(result);
        }
    }
}

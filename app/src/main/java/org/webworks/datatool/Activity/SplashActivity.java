package org.webworks.datatool.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import org.webworks.datatool.Model.User;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.UserRepository;
import org.webworks.datatool.Utility.ApiGetFacility;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {
    Context context;
    private String PREFS_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;
        PREFS_NAME = this.getResources().getString(R.string.pref_name);

        //delays the application for 3seconds and starts the right activity
        final int waitTime = new Random().nextInt(3) * 1000;
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000 + waitTime);
                    startRightActivity();
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * Method check for the activity to start
     * */
    public void startRightActivity() {

        UserRepository userRepository = new UserRepository(context);
        if (userRepository.userExists() == 1) {
            Intent intent = new Intent(context, TestingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if (userRepository.userExists() == 2) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("SESSION-LOGIN", 1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}

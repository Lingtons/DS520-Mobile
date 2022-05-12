package org.webworks.datatool.Session;

import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;

import org.webworks.datatool.Repository.UserRepository;

/**
 * This activity is the activity that manages session activity fo the apliccation
 * all classes that extendens this activity will have session manged by this activity
 * All sensitive activities should extend this activity
 * */

public class SessionManager extends AppCompatActivity {

    public static final long DISCONNECT_TIMEOUT = 10 * 60 * 1000;

    private static Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            logUserOut();
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    private void logUserOut() {
        //user can still be redirected to the login page from here
        UserRepository userRepository = new UserRepository(SessionManager.this);
        userRepository.updateUserSession(1);
    }
}

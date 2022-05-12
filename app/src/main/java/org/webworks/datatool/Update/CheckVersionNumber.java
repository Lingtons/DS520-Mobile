package org.webworks.datatool.Update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;


import org.webworks.datatool.R;
import org.webworks.datatool.Repository.UpdateRepository;
import org.webworks.datatool.Utility.UtilFuns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;



public class CheckVersionNumber extends AsyncTask<String,String,String> {

    private Context context;

    public CheckVersionNumber(Context _context){
        this.context = _context;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(strings[0]);
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        String input = "";

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            input = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Unauthorized")){

        }else{
            int currentVersionNumber = 0, updateVersionNumber = 0;
            UpdateRepository updateRepository = new UpdateRepository(context);

            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                currentVersionNumber = packageInfo.versionCode;
                //updateVersionNumber = Integer.parseInt(UpdateMonitor.downloadVersionNumber());
                updateVersionNumber = Integer.parseInt(result);
            } catch (PackageManager.NameNotFoundException e) {
                currentVersionNumber = 0;
            } catch (NumberFormatException e) {
                updateVersionNumber = 0;
            }
            if (updateVersionNumber > currentVersionNumber) {
                //show user popup to download update
                new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(context.getString(R.string.update_alert_title))
                        .setContentText(context.getString(R.string.update_alert_content))
                        .setConfirmText(context.getString(R.string.update_alert_ok_button))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                new UpdateDownloader(context).execute(context.getString(R.string.url) + context.getString(R.string.update_application_download));
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setCancelText(context.getString(R.string.update_alert_cancel_button))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
            }
            updateRepository.updateLastCheck(new Date().toString());
        }

        super.onPostExecute(result);
    }
}

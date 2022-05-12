package org.webworks.datatool.Utility;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;


import androidx.core.app.ActivityCompat;

import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.Model.DashboardSorter;
import org.webworks.datatool.Model.ServicesNeeded;
import org.webworks.datatool.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.net.ssl.SSLContext;


public abstract class UtilFuns {

    private static String PREF_USER_GUID;
    private static String PREF_USER_EMAIL;
    private static String PREF_USER_PASSWORD;
    private static String PREFS_NAME;
    private static String PREF_AUTH_TOKEN;
    private static String PREF_STATE_CODE;
    public UtilFuns() {

    }

    public static ArrayList<String> prepareSelectServices(String selectedServices) {
        if (!selectedServices.equals("")) {
            ArrayList<String> sort = new ArrayList<>();
            String[] co = selectedServices.split(",");
            for (int i = 0; i < co.length; i++) {
                String[] c = co[i].trim().split("=>");
                if (c.length > 0)
                    sort.add(c[1]);
            }
            return sort;
        }
        else
            return null;
    }

    public static ArrayList<ServicesNeeded> getSelectServices(ArrayList<String> list) {
        ArrayList<ServicesNeeded> sort = new ArrayList<>();
        if (list != null && !list.equals(""))
        for (int i = 0; i < list.size(); i++) {
            ServicesNeeded service = new ServicesNeeded(list.get(i));
            sort.add(service);
        }
        return sort;
    }

    public static String getServicesAsStringList(String selectedServices) {
        StringBuilder builder = new StringBuilder();
        builder.append("<b><u>Services</u></b><br>");
        if (!selectedServices.equals("")) {
            String[] co = selectedServices.split(",");
            for (int i = 0; i < co.length; i++) {
                String[] c = co[i].trim().split("=>");
                if (c.length > 0) {
                    builder.append(String.valueOf(i + 1));
                    builder.append(". ");
                    builder.append(c[1]);
                    if (i < co.length - 1) {
                        builder.append("<br>");
                    }
                }
            }
        }
        return builder.toString();
    }

    public static String getServicesAsString(String selectedServices) {
        if (!selectedServices.equals("")) {
            StringBuilder builder = new StringBuilder();
            String[] co = selectedServices.split(",");
            for (int i = 0; i < co.length; i++) {
                String[] c = co[i].trim().split("=>");
                if (c.length > 0) {
                    //builder.append(String.valueOf(i + 1));
                    builder.append(c[1]);
                    if (i < co.length - 1) {
                        builder.append("<br>");
                    }
                }
            }
            return builder.toString();
        }
        return "";
    }

    public static ArrayList<Integer> getColors(Context context) {
        ArrayList<Integer> color = new ArrayList<>();
        /*int[] res = {R.color.defaultList, R.color.htsList, R.color.referList, R.color.receiveList, R.color.enrolList};
        for (int i = 0; i < res.length; i++) {
            int co = context.getResources().getColor(res[i]);
            color.add(co);
        }*/
        return color;
    }

    public static ArrayList<String> getUploadedId(String input) {
        ArrayList<String> output = new ArrayList<>();
        String text = "";
        if(input.startsWith("\"")) {
            text = input.substring(1, input.length());
        }
        if(text.endsWith("\"")) {
            text = text.substring(0, text.length()-1);
        }
        String[] list = text.split(",");
        for (int i = 0; i < list.length; i++) {
            output.add(list[i]);
        }
        return output;
    }

    public static String getOneUploadedId(String input) {
        String text = "";
        if(input.startsWith("\"")) {
            text = input.substring(1, input.length());
        }
        if(text.endsWith("\"")) {
            text = text.substring(0, text.length()-1);
        }
        String[] f = text.split(":");
        if (!f[0].equals("")) {
            return f[1].trim();
        }
        else {
            return "";
        }
    }

    public static String convertApiDateToReadable(String input) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat inputFormat3 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");


        Date date = null;
        try {
            if (input.length() == 19) {
                date = inputFormat2.parse(input);
            }else if(input.length() == 21){
                date = inputFormat3.parse(input);
            }
            else {
                date = inputFormat.parse(input);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return outputFormat.format(date);
    }

    public static String formatCode(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) {
            output = "0" + output;
        }
        return output;
    }

    public static String getFacilityAbbr(String input) {
        String output;
        String[] a = input.split(" ");
        if (a.length > 2) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < a.length; i++) {
                builder.append(a[i].charAt(0));
                if (i > 1) {
                    break;
                }
            }
            output = builder.toString();
        }
        else {
            output = input.substring(0,3);
        }
        return output;
    }

    public static int resetCode(int input){
        if (input > 9999) { return 0; }
        else { return input; }
    }

    public static int calculateAge(String dob) {
        String yr = dob.substring(dob.length() - 4, dob.length());
        int presentYear = Calendar.getInstance().get(Calendar.YEAR);
        int personYear = Integer.parseInt(yr);
        return presentYear - personYear;
    }

    public static int getMonthFromDate(String dob) {
        int month = 0;
        if(dob.length() > 0) {
            String[] s = dob.split("/");
            month = Integer.parseInt(s[1]);
        }
        return month;
    }

    public static int getDayFromDate(String dob) {
        int day = 0;
        if(dob.length() > 0) {
            String[] s = dob.split("/");
            day = Integer.parseInt(s[0]);
        }
        return day;
    }

    public static String cleanResult(String input) {
        String text = "";
        if(input.startsWith("\"")) {
            text = input.substring(1, input.length());
        }
        if(text.endsWith("\"")) {
            text = text.substring(0, text.length()-1);
        }
        return text;
    }

    public static String getMonthAndDay(){
        //This method gets the current month and the present day
        //get the current month
        int curMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        //get the current day
        int curDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        //array of single numbers
        int[] singleNumbers = {0,1,2,3,4,5,6,7,8,9};
        //ensure that day is of two digit
        String currentDay = null;
        for (int n : singleNumbers) {
            if (n == curDay)
                currentDay = "0" + String.valueOf(curDay);
            else
                currentDay = String.valueOf(curDay);
        }
        //build and return the result in a required format
        return curMonth + "/" + currentDay + "/";
    }

    public static void flipStuff(final View v) {
        ObjectAnimator flip = ObjectAnimator.ofFloat(v, "rotationY", 0f, 360f);
        flip.setDuration(2000);
        flip.start();
    }

    public static void scaleScreenIcon(final View v) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(v, "scaleX", 0.5f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(v, "scaleY", 0.5f);
        scaleDownX.setDuration(1000);
        scaleDownY.setDuration(1000);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        scaleDown.start();
    }

    public static String changeStuff(int count, String text) {
        StringBuilder builder = new StringBuilder();
        builder.append("<b>");
        builder.append(count);
        builder.append("</b><br/>");
        builder.append(text);
        return builder.toString();
    }

    public static int calculateRemaining(int total, int actual) {
        return total - actual;
    }

    public static String getTodaysDate(){
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int realmonth = mm + 1;
        return dd + "/" + realmonth + "/" + yy;
    }

    public static HashMap<String, String> groupForms(ArrayList<ClientForm> forms) {
        HashMap<String, ArrayList<ClientForm>> formGroups = new HashMap<>();
        HashMap<String, String> formGroupCount = new HashMap<>();
        for (ClientForm f : forms) {
            String cd = getYMD(f.getCreateDate());
            if (!formGroups.containsKey(cd)) {
                ArrayList<ClientForm> p = new ArrayList<>();
                p.add(f);
                formGroups.put(cd, p);
                formGroupCount.put(cd, cd);
            }
            else {
                formGroups.get(cd).add(f);
                formGroupCount.put(cd, cd);
            }
        }
        return formGroupCount;
    }

    public static HashMap<String, Integer> groupFormsByDate(ArrayList<ClientForm> forms) {
        HashMap<String, ArrayList<ClientForm>> formGroups = new HashMap<>();
        HashMap<String, Integer> formGroupCount = new HashMap<>();
        for (ClientForm f : forms) {
            String cd = getYMD(f.getCreateDate());
            if (!formGroupCount.containsKey(cd)) {
                ArrayList<ClientForm> p = new ArrayList<>();
                p.add(f);
                formGroups.put(cd, p);
                formGroupCount.put(cd, p.size());
            }
            else {
                formGroups.get(cd).add(f);
                formGroupCount.put(cd, formGroups.get(cd).size());
            }
        }

        return formGroupCount;
    }

    public static ArrayList<DashboardSorter> groupFormsByDates(ArrayList<ClientForm> forms) {
        HashMap<String, ArrayList<ClientForm>> formGroups = new HashMap<>();
        HashMap<String, Integer> formGroupCount = new HashMap<>();
        for (ClientForm f : forms) {
            String cd = getYMD(f.getCreateDate());
            if (!formGroupCount.containsKey(cd)) {
                ArrayList<ClientForm> p = new ArrayList<>();
                p.add(f);
                formGroups.put(cd, p);
                formGroupCount.put(cd, p.size());
            }
            else {
                formGroups.get(cd).add(f);
                formGroupCount.put(cd, formGroups.get(cd).size());
            }
        }
        ArrayList<DashboardSorter> dashboardSorters = new ArrayList<>();
        for (String key : formGroupCount.keySet()) {
            DashboardSorter sorter = new DashboardSorter();
            sorter.setFormDate(key);
            sorter.setFormCount(formGroupCount.get(key));
            dashboardSorters.add(sorter);
        }
        return dashboardSorters;

    }

    public static String getYMD(Date input) {
        //added null check to avoid exception
        if (input !=null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            return formatter.format(input);
        }

        return null;
    }

    public static String getCurMonthName(int month) {
        final String[] months = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "August", "September", "October", "November", "December"};

        return months[month];
    }

    public static Date formatDate(String input){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(input);
        }
        catch (ParseException e) {
            date = null;
        }
        return date;
    }

    public static void initializeSSLContext(){
        try {
            SSLContext.getInstance("TLS").init(null, null, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("ngstateslg.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static Date convertStringToDate(String dateString){
        Date date = null;
        Date formattedDate = null;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        try{
            date = df.parse(dateString);
            //formattedDate = df.format(date);
        }
        catch (Exception ex){
            System.out.println(ex);
        }
        return date;
    }

    public static String stripBrackets(ArrayList<String> strings){
        if (strings != null){
            String ids = strings.toString();
            ids = ids.substring(1, ids.length() - 1);

            return ids;
        }
        return null;
    }

    public static String getDeviceId(Context context, Activity activity){
        TelephonyManager telephonyManager;
        String DeviceId;
        //telephonyManager = (TelephonyManager) getSystemService(context.TELEPHONY_SERVICE);
        telephonyManager = (TelephonyManager) activity.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        DeviceId = telephonyManager.getDeviceId();

        return DeviceId;
    }

    public static String getFormattedDateTime(Date dateObj){
        return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ").format(dateObj);
    }

    /**
     * Returns MAC address of the given interface name.
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return  mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:",aMac));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public static String generateClientID(Context context, Activity activity){
        PREFS_NAME = context.getResources().getString(R.string.pref_name);
        PREF_USER_GUID = context.getResources().getString(R.string.pref_user);
        PREF_STATE_CODE = context.getResources().getString(R.string.pref_state_code);

        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        String characters = "ABCDEFGHIJKLMNPQRSTUVWXYZ1234567890";
        int length = 5;
        char[] text = new char[8];
        Random randomString = new Random();
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(randomString.nextInt(characters.length()));
        }
        String deviceId = getDeviceId(context,activity);
        String deviceString = deviceId == null ? "00" : getLastFourCharacters(deviceId);
        String userID = sharedPreferences.getString(PREF_USER_GUID, "");
        String stateCode = sharedPreferences.getString(PREF_STATE_CODE,"UUU");
        return stateCode+"/"+userID+deviceString+"/"+new String(text);

    }

    private static String getLastFourCharacters(String characters){
        return characters.substring(characters.length()-4);
    }

//    private static String getShortenedUserId(String userId)
//    {
//        return userId.substring(userId.length()-2);
//    }

    public static String getPrefUserEmail(Context context){
        PREFS_NAME = context.getResources().getString(R.string.pref_name);
        PREF_USER_EMAIL =context.getString(R.string.pref_user_email);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(PREF_USER_EMAIL, "");
        return email;
    }

    public static String getPrefUserPassword(Context context){
        PREFS_NAME = context.getResources().getString(R.string.pref_name);
        PREF_USER_PASSWORD = context.getString(R.string.pref_password);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        String password = sharedPreferences.getString(PREF_USER_PASSWORD, "");
        return password;
    }

    public static String encodeImage(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }


}


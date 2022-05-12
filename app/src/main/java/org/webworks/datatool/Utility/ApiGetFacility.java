package org.webworks.datatool.Utility;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webworks.datatool.Model.Facility;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.FacilityRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ApiGetFacility extends AsyncTask<Void, Void, String> {
    private Context context;
    FacilityRepository facilityRepository;

    public ApiGetFacility(Context context) {
        this.context = context;
        facilityRepository = new FacilityRepository(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(Void... voids) {
        String text = "";
        BufferedReader reader;
        // Send data
        try
        {
            URL url = new URL(context.getResources().getString(R.string.api_url) + context.getResources().getString(R.string.api_get_facilities));
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            text = stringBuilder.toString();
            conn.disconnect();
            return text;
        }
        catch(Exception e) {
            return "error";
        }
    }

    protected void onPostExecute(String result) {

        if(!(result.equals(null) || result.isEmpty()) && !result.equals("Error")) {
            try {
                JSONObject appDataObject = new JSONObject(result);
                JSONArray facilitiesArray = (JSONArray) appDataObject.get("facilities");
                JSONObject jreader;
                ArrayList<Facility> facilities = new ArrayList<>();

                for (int i= 0; i < facilitiesArray.length(); i++){
                    Facility facility = new Facility();
                    jreader = facilitiesArray.getJSONObject(i);
                    facility.setFacilityId(jreader.getInt("id"));
                    facility.setFacilityName(jreader.getString("name"));
                    facility.setDatimCode(jreader.getString("code"));
                    facility.setLgaCode(jreader.getString("lga_code"));
                    facilities.add(facility);
                }
                facilityRepository.addFacilities(facilities);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        super.onPostExecute(result);
    }
}

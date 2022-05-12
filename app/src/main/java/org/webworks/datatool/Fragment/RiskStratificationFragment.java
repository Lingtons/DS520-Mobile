package org.webworks.datatool.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.webworks.datatool.Activity.TestingActivity;
import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Repository.Repository;
import org.webworks.datatool.Utility.BindingMeths;
import org.webworks.datatool.Utility.UtilFuns;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


public class RiskStratificationFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private OnFragmentInteractionListener mListener;
    private Context context;
    SharedPreferences sharedPreferences;
    private final String PRE_TEST_INFORMATION = "Pre_Test";
    private TextView txtview_pedScore, txtview_adultScore;
    private LinearLayout linearLayoutPediatric, linearLayoutAdult, rstControls, riskLevelLayout;
    private LabelledSpinner spinnerAgeGroup, spinnerTestResult, clientRiskLevel, referralClientLga, referralClientState, testingPointParent, testingPoint;
    private Button saveRiskForm, skipRiskForm, updateRiskForm, saveCloseRiskForm;
    private EditText txtRecentTestDate, referralClientGeoCode;
    private RadioGroup rstPedRadio1, rstPedRadio2, rstPedRadio3, rstPedRadio4, rstPedRadio5, rstPedRadio6, rstPedRadio7;
    private RadioGroup rstAdultRadio1, rstAdultRadio2, rstAdultRadio3, rstAdultRadio4,rstAdultRadio5,rstAdultRadio6,rstAdultRadio7,rstAdultRadio8;
    private String packageName, DeviceId;
    private ImageButton btnLocation;
    ReferralFormRepository referralFormRepository;
    private final String EXTRA_FORM_ID = "FORM_ID";
    private final String EXTRA_TRACED_CLIENT_ID = "TracedClientId";
    private final String TAG = "DemoLocation";
    private boolean formFilled;
    private ClientForm form;
    private String TRACED_CLIENT_ID;
    private String PREFS_NAME;
    private String PREF_VERSION_CODE_KEY;
    private String PREF_USER_GUID;
    private String PREF_FACILITY_GUID;
    private String PREF_LAST_CODE;
    private String userId;
    private String facilityId;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private Repository repository;
    private static final int MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100;



    public RiskStratificationFragment() {
        // Required empty public constructor
    }

    public static RiskStratificationFragment newInstance() {
        RiskStratificationFragment fragment = new RiskStratificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        packageName = context.getPackageName();
        getActivity().setTitle("Risk Stratification");
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int formId = bundle.getInt(EXTRA_FORM_ID);
            String TracedClientId = bundle.getString(EXTRA_TRACED_CLIENT_ID);
            if (TracedClientId != null){
                TRACED_CLIENT_ID = TracedClientId;
            }

            if (formId != 0) {
                formFilled = true;
                form = new ClientForm();
                referralFormRepository = new ReferralFormRepository(context);
                form = referralFormRepository.getReferralFormById(formId);
            }
        }
        PREFS_NAME = context.getResources().getString(R.string.pref_name);
        PREF_VERSION_CODE_KEY = context.getResources().getString(R.string.pref_version);
        PREF_USER_GUID = context.getResources().getString(R.string.pref_user);
        PREF_FACILITY_GUID = context.getResources().getString(R.string.pref_facility);
        PREF_LAST_CODE = context.getResources().getString(R.string.pref_code);

        repository = new Repository(context);

        sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(PREF_USER_GUID, "");
        facilityId = sharedPreferences.getString(PREF_FACILITY_GUID,"");

        DeviceId = UtilFuns.getDeviceId(context, this.getActivity());

        Log.d(TAG, "onCreate ...............................");

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION);

                // MY_PERMISSION_REQUEST_READ_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }else{
            createLocationRequest();
        }

//        mGoogleApiClient = new GoogleApiClient.Builder(context)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createLocationRequest();
                    // permission was granted, yay! Do the contacts-related task you need to do.

                } else {

                }
                return;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_risk_stratification, container, false);

        initializeFields(view);
        setListeners();
        if (formFilled) {
            assignValuesToFields(form, view);
        }

        return view;
    }

    private void setListeners()
    {
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI();
            }
        });
        spinnerAgeGroup.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedAgeGroup = spinnerAgeGroup.getSpinner().getSelectedItem().toString();
                if (formFilled)
                    riskLevelLayout.setVisibility(View.GONE);

                if (selectedAgeGroup.equals("Adult")){
                    rstControls.setVisibility(View.VISIBLE);
                    linearLayoutAdult.setVisibility(View.VISIBLE);
                    linearLayoutPediatric.setVisibility(View.GONE);
                }else if (selectedAgeGroup.equals("Pediatrics")){
                    rstControls.setVisibility(View.VISIBLE);
                    linearLayoutAdult.setVisibility(View.GONE);
                    linearLayoutPediatric.setVisibility(View.VISIBLE);
                }else{
                    rstControls.setVisibility(View.GONE);
                    linearLayoutAdult.setVisibility(View.GONE);
                    linearLayoutPediatric.setVisibility(View.GONE);
                    riskLevelLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtRecentTestDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    final Calendar calendar = Calendar.getInstance();
                    final int yy = calendar.get(Calendar.YEAR);
                    int mm = calendar.get(Calendar.MONTH);
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            int age = yy - year;
                            int realmonth = month + 1;
                            String period = day + "/" + realmonth + "/" + year;
                            txtRecentTestDate.setText(period);
                        }
                    }, yy, mm, dd);
                    datePicker.show();
                }
            }
        });

        saveRiskForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRiskForm();
            }
        });

        updateRiskForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRiskForm(form);
            }
        });

        skipRiskForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(EXTRA_FORM_ID, form.getId());
                    PretestFragment fragment = PretestFragment.newInstance();
                    mListener.onContinueButtonClicked(PRE_TEST_INFORMATION, fragment, bundle);
                }
            }
        });

        saveCloseRiskForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndCloseRiskForm();
            }
        });

        toggleSave();

        //Adult RadioGroups listeners
        rstAdultRadio1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_adultScore.setText(String.valueOf(getScoreCount(adultRadioGroups())));
                toggleSave();
            }
        });
        rstAdultRadio2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_adultScore.setText(String.valueOf(getScoreCount(adultRadioGroups())));
                toggleSave();
            }
        });
        rstAdultRadio3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_adultScore.setText(String.valueOf(getScoreCount(adultRadioGroups())));
                toggleSave();
            }
        });
        rstAdultRadio4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_adultScore.setText(String.valueOf(getScoreCount(adultRadioGroups())));
                toggleSave();
            }
        });
        rstAdultRadio5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_adultScore.setText(String.valueOf(getScoreCount(adultRadioGroups())));
                toggleSave();
            }
        });
        rstAdultRadio6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_adultScore.setText(String.valueOf(getScoreCount(adultRadioGroups())));
                toggleSave();
            }
        });
        rstAdultRadio7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_adultScore.setText(String.valueOf(getScoreCount(adultRadioGroups())));
                toggleSave();
            }
        });
        rstAdultRadio8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_adultScore.setText(String.valueOf(getScoreCount(adultRadioGroups())));
                toggleSave();
            }
        });

        //Pediatrics RadioGroups listeners
        rstPedRadio1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_pedScore.setText(String.valueOf(getScoreCount(pediatricsRadioGroups())));
                toggleSave();
            }
        });
        rstPedRadio2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_pedScore.setText(String.valueOf(getScoreCount(pediatricsRadioGroups())));
                toggleSave();
            }
        });
        rstPedRadio3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_pedScore.setText(String.valueOf(getScoreCount(pediatricsRadioGroups())));
                toggleSave();
            }
        });
        rstPedRadio4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_pedScore.setText(String.valueOf(getScoreCount(pediatricsRadioGroups())));
                toggleSave();
            }
        });
        rstPedRadio5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_pedScore.setText(String.valueOf(getScoreCount(pediatricsRadioGroups())));
                toggleSave();
            }
        });
        rstPedRadio6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_pedScore.setText(String.valueOf(getScoreCount(pediatricsRadioGroups())));
                toggleSave();
            }
        });
        rstPedRadio7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                txtview_pedScore.setText(String.valueOf(getScoreCount(pediatricsRadioGroups())));
                toggleSave();
            }
        });

        testingPoint.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        clientRiskLevel.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (clientRiskLevel.getSpinner().getSelectedItemPosition() > 1){
                    //saveClosePretestForm.setVisibility(View.VISIBLE);
                    rstControls.setVisibility(View.VISIBLE);
                    saveRiskForm.setVisibility(View.VISIBLE);
                    saveCloseRiskForm.setVisibility(View.GONE);
                }else if(clientRiskLevel.getSpinner().getSelectedItemPosition() == 1){
                    rstControls.setVisibility(View.VISIBLE);
                    saveCloseRiskForm.setVisibility(View.VISIBLE);
                    saveRiskForm.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void initializeFields(View view) {
        linearLayoutAdult = view.findViewById(R.id.rstAdult);
        linearLayoutPediatric = view.findViewById(R.id.rstPediatric);
        riskLevelLayout = view.findViewById(R.id.rst_risk_level_layout);
        referralClientState = view.findViewById((R.id.referral_client_state));
        referralClientLga = view.findViewById(R.id.referral_client_lga);
        testingPointParent = view.findViewById(R.id.testing_point_parent);
        testingPoint = view.findViewById(R.id.testing_point);

        rstControls = view.findViewById(R.id.rst_controls);
        spinnerAgeGroup = view.findViewById(R.id.rst_agegroup);
        clientRiskLevel = view.findViewById(R.id.rst_risk_level);
        spinnerTestResult = view.findViewById(R.id.rst_adult_most_recent_test_result);
        txtRecentTestDate = view.findViewById(R.id.rst_adult_approx_recent_test_date);
        referralClientGeoCode = (EditText)view.findViewById(R.id.referral_client_geo_code);
        btnLocation = view.findViewById(R.id.btnLocation);
        //textViews
        txtview_pedScore = view.findViewById(R.id.rst_ped_hiv_risk_score);
        txtview_adultScore = view.findViewById(R.id.rst_adult_hiv_risk_score);
        //Buttons
        saveCloseRiskForm = view.findViewById(R.id.btn_risk_save_close_form);
        saveRiskForm = view.findViewById(R.id.btn_risk_save_form);
        updateRiskForm = view.findViewById(R.id.btn_risk_update_form);
        skipRiskForm = view.findViewById(R.id.btn_risk_skip_form);
        //RadioGroups Pediatrics
        rstPedRadio1 = view.findViewById(R.id.rst_ped_mother_positive_or_member_deceased);
        rstPedRadio2 = view.findViewById(R.id.rst_ped_child_ever_tested_for_hiv);
        rstPedRadio3 = view.findViewById(R.id.rst_ped_skin_problems_in_last_six_months);
        rstPedRadio4 = view.findViewById(R.id.rst_ped_poor_health_in_last_six_months);
        rstPedRadio5 = view.findViewById(R.id.rst_ped_living_with_tb_diagnosed_person_or_tb_symptoms);
        rstPedRadio6 = view.findViewById(R.id.rst_ped_forced_sex);
        rstPedRadio7 = view.findViewById(R.id.rst_ped_ever_had_anal_or_vaginal_sex_without_condom);
        //RadioGroups Adult
        rstAdultRadio1 = view.findViewById(R.id.rst_adult_test_based_on_physician_request);
        rstAdultRadio2 = view.findViewById(R.id.rst_adult_ever_had_anal_or_vaginal_sex_without_condom);
        rstAdultRadio3 = view.findViewById(R.id.rst_adult_ever_had_blood_transfusion);
        rstAdultRadio4 = view.findViewById(R.id.rst_adult_have_sti_symptoms);
        rstAdultRadio5 = view.findViewById(R.id.rst_adult_have_tb_symptoms);
        rstAdultRadio6 = view.findViewById(R.id.rst_adult_have_shared_sharp_objects);
        rstAdultRadio7 = view.findViewById(R.id.rst_adult_have_paid_for_or_sold_sex);
        rstAdultRadio8 = view.findViewById(R.id.rst_adult_forced_sex);


        linearLayoutPediatric.setVisibility(View.GONE);
        linearLayoutAdult.setVisibility(View.GONE);
        rstControls.setVisibility(View.GONE);
        updateRiskForm.setVisibility(View.GONE);
        skipRiskForm.setVisibility(View.GONE);
        riskLevelLayout.setVisibility(View.GONE);

        /*
         * Binding DropDown Data
         **/
        BindingMeths binding = new BindingMeths(context);
        binding.bindAgeGroup(spinnerAgeGroup);
        binding.bindRstTestResult(spinnerTestResult);
        binding.bindRiskLevel(clientRiskLevel);
        binding.bindStateData(referralClientState, referralClientLga);
        binding.bindTestingPointParentData(testingPointParent, testingPoint);
    }

    private void assignValuesToFields(final ClientForm referralForm, View view) {

        riskLevelLayout.setVisibility(View.GONE);


        referralClientState.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String stateCode = null;
                try {
                    stateCode = repository.bindStateData().get(i).getState_code();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //LabelledSpinner lga = (LabelledSpinner)spinner.findViewById(R.id.referral_client_lga);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        testingPointParent.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String parentCode = null;
                try {
                    parentCode = repository.bindParentTestingPointData().get(i).getParentCode();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        getActivity().setTitle("Risk Stratification");
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private ArrayList<RadioGroup> pediatricsRadioGroups() {
        ArrayList<RadioGroup> radioGroups = new ArrayList<RadioGroup>();
        radioGroups.add(rstPedRadio1);
        radioGroups.add(rstPedRadio2);
        radioGroups.add(rstPedRadio3);
        radioGroups.add(rstPedRadio4);
        radioGroups.add(rstPedRadio5);
        radioGroups.add(rstPedRadio6);
        radioGroups.add(rstPedRadio7);
        return radioGroups;
    }

    private ArrayList<RadioGroup> adultRadioGroups() {
        ArrayList<RadioGroup> radioGroups = new ArrayList<RadioGroup>();
        radioGroups.add(rstAdultRadio1);
        radioGroups.add(rstAdultRadio2);
        radioGroups.add(rstAdultRadio3);
        radioGroups.add(rstAdultRadio4);
        radioGroups.add(rstAdultRadio5);
        radioGroups.add(rstAdultRadio6);
        radioGroups.add(rstAdultRadio7);
        radioGroups.add(rstAdultRadio8);
        return radioGroups;
    }

    private void assignValuesFromJson(ArrayList<RadioGroup> adultRG, ArrayList<RadioGroup> pediatricRG, ArrayList<TextView> scores, String json, View pview) {


        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                switch (i) {
                    //knowledge
                    case 0: {
                        jsonObject = new JSONObject(jsonArray.get(0).toString());
                        Iterator<String> keys = jsonObject.keys();
                        do {
                            populateRst(jsonObject, pview, keys);
                        } while (keys.hasNext());
                        break;
                    }
                    //risk
                    case 1:{
                        jsonObject = new JSONObject(jsonArray.get(1).toString());
                        Iterator<String> keys = jsonObject.keys();
                        do {
                            populateRst(jsonObject, pview, keys);
                        } while (keys.hasNext());
                        break;
                    }
                    //scores
                    case 2:
                        jsonObject = new JSONObject(jsonArray.get(2).toString());
                        for (int u = 0; u < scores.size(); u++) {
                            Iterator<String> keys = jsonObject.keys();
                            do {
                                try {
                                    String name = scores.get(u).getResources().getResourceEntryName(scores.get(u).getId());
                                    String match = keys.next();
                                    if (name.equals(match)) {
                                        scores.get(u).setText(jsonObject.getString(match));
                                        break;
                                    }
                                }
                                catch (Exception e) {
                                }
                            }while (keys.hasNext());
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateRst(JSONObject jsonObject, View pview, Iterator<String> keys){
        try {
            String key_name = keys.next();
            Object keyvalue = jsonObject.get(key_name);

            int id = context.getResources().getIdentifier(key_name, "id", packageName);
            RadioGroup rg = (RadioGroup) pview.findViewById(id);
            if (rg != null) {
                int idYes = getResources().getIdentifier(key_name + "_yes", "id", context.getPackageName());
                int idNo = getResources().getIdentifier(key_name + "_no", "id", context.getPackageName());
                RadioButton rbYes = pview.findViewById(idYes);
                RadioButton rbNo = pview.findViewById(idNo);
                if((int)keyvalue == 0){
                    rbNo.setChecked(true);

                }else if ((int)keyvalue == 1){
                    rbYes.setChecked(true);
                }
            }
        } catch (Exception e) {
        }
    }

    private String getButtonsAsJson(ArrayList<RadioGroup> groups) {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < groups.size(); i++) {

            RadioGroup radioGroup = (RadioGroup)getView().findViewById(groups.get(i).getId());

            RadioButton button = (RadioButton) getView().findViewById(groups.get(i).getCheckedRadioButtonId());
            String str = "";
            String answer;
            try {
                answer = button.getText().toString();
                //str = button.getResources().getResourceEntryName(button.getId());
                str = radioGroup.getResources().getResourceEntryName(radioGroup.getId());
            } catch (Exception e) {
                answer = "No";
            }
            try {
                if (answer.equals("Yes")) {
                    //jsonObject.put(String.valueOf(groups.get(i).getCheckedRadioButtonId()), 1);
                    jsonObject.put(str, 1);
                } else {//if (answer.equals("No"))
                    //jsonObject.put(String.valueOf(groups.get(i).getCheckedRadioButtonId()), 0);
                    jsonObject.put(str, 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    private String getRiskInfoAsJson() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(getButtonsAsJson(adultRadioGroups()));
        jsonArray.put(getButtonsAsJson((pediatricsRadioGroups())));
        jsonArray.put(getScoreAsJson(scoreList()));
        return jsonArray.toString();
    }

    private ArrayList<TextView> scoreList() {
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(txtview_adultScore);
        textViews.add(txtview_pedScore);
        return textViews;
    }

    private String getScoreAsJson(ArrayList<TextView> textViews) {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < textViews.size(); i++) {
            try {
                String str = textViews.get(i).getResources().getResourceEntryName(textViews.get(i).getId());
                //jsonObject.put(String.valueOf(textViews.get(i).getId()), textViews.get(i).getText().toString().trim());
                jsonObject.put(str, textViews.get(i).getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  jsonObject.toString();
    }

    private void submitRiskForm() {
        if (validate()) {
            referralFormRepository = new ReferralFormRepository(context);
            final ClientForm ClientForm = new ClientForm();

            String stateCode;
            String lgaCode;
            try {
                stateCode = repository.bindStateData().get(referralClientState.getSpinner().getSelectedItemPosition()).getState_code();
                lgaCode = repository.getLgaCodeByStateAndIndex(stateCode, referralClientLga.getSpinner().getSelectedItemPosition());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String testingAreaCode;
            String testingPointCode;
            try {
                testingAreaCode = repository.bindParentTestingPointData().get(testingPointParent.getSpinner().getSelectedItemPosition()).getParentCode();
                testingPointCode = repository.getTestingPointCodeByParentAndIndex(testingAreaCode, testingPoint.getSpinner().getSelectedItemPosition());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //}
        }
    }

    private void clearForm() {

    }

    private boolean validate() {
        if (spinnerAgeGroup.getSpinner().getSelectedItemPosition() == 0) {
            Toast.makeText(context, getString(R.string.drop_down_validate, "Age Group"), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (referralClientState.getSpinner().getSelectedItemPosition() == 0){
            Toast.makeText(context, getString(R.string.drop_down_validate, "State"), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (referralClientLga.getSpinner().getSelectedItemPosition() == 0){
            Toast.makeText(context, getString(R.string.drop_down_validate, "LGA"), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!referralClientGeoCode.getText().toString().trim().equals("")){
            String geocode = referralClientGeoCode.getText().toString().trim();
            if (!geocode.matches("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")){
                Toast.makeText(context, "Invalid Geocode value", Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(context, "Geocode is required", Toast.LENGTH_LONG).show();
            return false;
        }

        if (testingPointParent.getSpinner().getSelectedItemPosition() == 0){
            Toast.makeText(context, getString(R.string.drop_down_validate, "Testing Area"), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (testingPoint.getSpinner().getSelectedItemPosition() == 0){
            Toast.makeText(context, getString(R.string.drop_down_validate, "Testing Point"), Toast.LENGTH_SHORT).show();
            return false;
        }


        if (spinnerAgeGroup.getSpinner().getSelectedItem().toString().trim().equals("Adult")){
            if ((rstAdultRadio1.getCheckedRadioButtonId() == -1) || (rstAdultRadio2.getCheckedRadioButtonId() == -1)||(rstAdultRadio3.getCheckedRadioButtonId() == -1)
                    || (rstAdultRadio4.getCheckedRadioButtonId() == -1) || (rstAdultRadio5.getCheckedRadioButtonId() == -1) || (rstAdultRadio6.getCheckedRadioButtonId() == -1)
                    || (rstAdultRadio7.getCheckedRadioButtonId() == -1) || (rstAdultRadio8.getCheckedRadioButtonId() == -1)
            ){
                Toast.makeText(context, getString(R.string.radio_validation, "Adult Risk Stratification"), Toast.LENGTH_LONG).show();
                return false;
            }

        }else if(spinnerAgeGroup.getSpinner().getSelectedItem().toString().trim().equals("Pediatrics")){
            if ((rstPedRadio1.getCheckedRadioButtonId() == -1) || (rstPedRadio2.getCheckedRadioButtonId() == -1)||(rstPedRadio3.getCheckedRadioButtonId() == -1)
                    || (rstPedRadio4.getCheckedRadioButtonId() == -1) || (rstPedRadio5.getCheckedRadioButtonId() == -1) || (rstPedRadio6.getCheckedRadioButtonId() == -1)
                    || (rstPedRadio7.getCheckedRadioButtonId() == -1)
            ){
                Toast.makeText(context, getString(R.string.radio_validation, "Pediatric Risk Stratification"), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (spinnerTestResult.getSpinner().getSelectedItemPosition() != 0){
            if (txtRecentTestDate.getText().toString().equals("")){
                Toast.makeText(context, "Test date is required if test result is selected", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void updateRiskForm(ClientForm ClientForm) {
        if (validate()) {
            referralFormRepository = new ReferralFormRepository(context);

            String stateCode;
            String lgaCode;
            try {
                stateCode = repository.bindStateData().get(referralClientState.getSpinner().getSelectedItemPosition()).getState_code();
                lgaCode = repository.getLgaCodeByStateAndIndex(stateCode, referralClientLga.getSpinner().getSelectedItemPosition());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String testingAreaCode;
            String testingPointCode;
            try {
                testingAreaCode = repository.bindParentTestingPointData().get(testingPointParent.getSpinner().getSelectedItemPosition()).getParentCode();
                testingPointCode = repository.getTestingPointCodeByParentAndIndex(testingAreaCode, testingPoint.getSpinner().getSelectedItemPosition());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            long saves = referralFormRepository.updateRiskStratification(ClientForm);
            if (saves == -1) {
                Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
            }
            clearForm();
            Bundle bundle = new Bundle();
            bundle.putInt(EXTRA_FORM_ID, ClientForm.getId());
            mListener.onSkipButtonClicked(PRE_TEST_INFORMATION, bundle);
        }
    }

    private void saveAndCloseRiskForm(){
        if (validate()){
            referralFormRepository = new ReferralFormRepository(context);
            final ClientForm ClientForm = new ClientForm();

            String stateCode;
            String lgaCode;
            try {
                stateCode = repository.bindStateData().get(referralClientState.getSpinner().getSelectedItemPosition()).getState_code();
                lgaCode = repository.getLgaCodeByStateAndIndex(stateCode, referralClientLga.getSpinner().getSelectedItemPosition());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String testingAreaCode;
            String testingPointCode;
            try {
                testingAreaCode = repository.bindParentTestingPointData().get(testingPointParent.getSpinner().getSelectedItemPosition()).getParentCode();
                testingPointCode = repository.getTestingPointCodeByParentAndIndex(testingAreaCode, testingPoint.getSpinner().getSelectedItemPosition());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private int getScoreCount(ArrayList<RadioGroup> groups) {
        int score = 0;
        for (int i = 0; i < groups.size(); i++) {
            RadioButton button = (RadioButton)getView().findViewById(groups.get(i).getCheckedRadioButtonId());
            String answer = "";
            try {
                answer = button.getText().toString();
            }catch (Exception e) {
                answer = "No";
            }
            if (answer.equals("Yes")) {
                score++;
            }
        }

        return score;
    }

    private void toggleSave(){
        saveRiskForm.setVisibility(View.GONE);
        saveCloseRiskForm.setVisibility(View.GONE);
        int pedScore = Integer.parseInt(txtview_pedScore.getText().toString());
        int adultScore = Integer.parseInt(txtview_adultScore.getText().toString());
        final int totalScore = pedScore + adultScore;

        //if pretest not already filled
        if (form == null){
            if(totalScore > 0){
                riskLevelLayout.setVisibility(View.GONE);
                saveCloseRiskForm.setVisibility(View.GONE);
            }else{
                riskLevelLayout.setVisibility(View.VISIBLE);
            }
            if (totalScore >=1){
                saveRiskForm.setVisibility(View.VISIBLE);
            }else{
                saveRiskForm.setVisibility(View.GONE);
            }
        }
    }

    public interface OnFragmentInteractionListener {

        void onSkipButtonClicked(String fragmentTag, Bundle bundle);

        void onContinueButtonClicked(String fragmentTag, Fragment fragment, Bundle bundle);
    }

    protected void createLocationRequest() {
        try{
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        }catch(Exception e){
            Log.d("LS", e.getMessage() == null ? "" : e.getMessage());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        try{
            if (mGoogleApiClient !=null)
                mGoogleApiClient.connect();
        }catch (Exception e){
            Log.d("Start LS", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient !=null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    private void updateUI() {
        if (mGoogleApiClient != null){
            if (mCurrentLocation != null) {
                String lat = String.valueOf(mCurrentLocation.getLatitude());
                String lng = String.valueOf(mCurrentLocation.getLongitude());
                referralClientGeoCode.setText(lng + "," + lat );
            }else{
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }else{
            Toast.makeText(context, "Please ensure location permission is set", Toast.LENGTH_LONG).show();
        }
    }
}

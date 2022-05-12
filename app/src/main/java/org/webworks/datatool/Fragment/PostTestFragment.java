package org.webworks.datatool.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webworks.datatool.Activity.TestingActivity;
import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.FacilityRepository;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Utility.BindingMeths;
import java.util.ArrayList;
import java.util.Iterator;

public class PostTestFragment extends Fragment {

    Context context;
    LabelledSpinner referralTestedBefore;
    RadioGroup postCouncelling1, postCouncelling2, postCouncelling3, postCouncelling4, postCouncelling5, postCouncelling6, postCouncelling7, postCouncelling8, postCouncelling9, postCouncelling10,
            postCouncelling11, postCouncelling12, postCouncelling13, postCouncelling14;
    Button saveAndRefer, continueAndRefer, updateAndRefer;
    private String PREFS_NAME;
    private String PREF_VERSION_CODE_KEY;
    private String PREF_USER_GUID;
    private String PREF_FACILITY_GUID;
    private String PREF_LAST_CODE;
    ReferralFormRepository referralFormRepository;
    private final String REFERRAL_FORM = "Referral_Form";
    private final String ENROL_FORM = "Enrol_Form";
    private final String EXTRA_FORM_ID = "FORM_ID";
    private boolean formFilled;
    private ClientForm form;
    private String packageName;
    private OnFragmentInteractionListener mListener;

    public PostTestFragment() {
        // Required empty public constructor
    }

    public static PostTestFragment newInstance() {
        PostTestFragment fragment = new PostTestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Post test counselling");
        context = getActivity().getApplicationContext();
        packageName = context.getPackageName();
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int formId = bundle.getInt(EXTRA_FORM_ID);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_test, container, false);
        initializeFields(view);
        setListeners();
        if (formFilled) {
            assignValuesToFields(form, view);
        }
        return view;
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
        getActivity().setTitle("Post test counselling");
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initializeFields(View view) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String facilityGuid = "";//sharedPreferences.getString(PREF_FACILITY_GUID, "");

        referralTestedBefore = (LabelledSpinner)view.findViewById(R.id.referral_tested_this_year);
        //saveAndEnrol = (Button)view.findViewById(R.id.save_post_test_enroll);
        saveAndRefer = (Button)view.findViewById(R.id.save_post_test_refer);
        continueAndRefer = (Button)view.findViewById(R.id.continue_post_test_refer);
        updateAndRefer = (Button) view.findViewById(R.id.update_post_test_refer);
        postCouncelling1 = (RadioGroup) view.findViewById(R.id.request_and_result_form_signed_by_testers);
        postCouncelling2 = (RadioGroup) view.findViewById(R.id.request_and_result_form_filled_with_ct_intake_form);
        postCouncelling3 = (RadioGroup) view.findViewById(R.id.client_received_hiv_result);
        postCouncelling4 = (RadioGroup) view.findViewById(R.id.post_test_councelling_done);
        postCouncelling5 = (RadioGroup) view.findViewById(R.id.risk_reduction_plan_developed);
        postCouncelling6 = (RadioGroup) view.findViewById(R.id.post_test_disclosure_plan_developed);
        postCouncelling7 = (RadioGroup) view.findViewById(R.id.will_bring_partners_for_testing);
        postCouncelling8 = (RadioGroup) view.findViewById(R.id.will_bring_own_children_less_than_5_years_old_for_testing);
        postCouncelling9 = (RadioGroup) view.findViewById(R.id.provided_with_information_on_fp_and_dual_contraception);
        postCouncelling10 = (RadioGroup) view.findViewById(R.id.client_or_partner_use_fp_methods);
        postCouncelling11 = (RadioGroup) view.findViewById(R.id.client_or_partner_use_condom_as_one_fp_method);
        postCouncelling12 = (RadioGroup) view.findViewById(R.id.correct_condom_use_demonstrated);
        postCouncelling13 = (RadioGroup) view.findViewById(R.id.condoms_provided);
        postCouncelling14 = (RadioGroup) view.findViewById(R.id.client_referred_to_other_services);
        /*
        * Binding DropDown Data
        **/
        BindingMeths binding = new BindingMeths(context);
        binding.bindTestedBefore(referralTestedBefore);
        /*
        * Initial states
        * */
        continueAndRefer.setVisibility(View.GONE);
        updateAndRefer.setVisibility(View.GONE);
    }

    private void setListeners() {

        saveAndRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndReferForm(form);
            }
        });
//        continueAndRefer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putInt(EXTRA_FORM_ID, form.getId());
//                ReferralFragment fragment = ReferralFragment.newInstance();
//                mListener.onContinueButtonClicked(REFERRAL_FORM, fragment, bundle);
//            }
//        });

        updateAndRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAndReferForm(form);
            }
        });
    }

    private ArrayList<RadioGroup> postTestRadioGroups() {
        ArrayList<RadioGroup> radioGroups = new ArrayList<RadioGroup>();
        radioGroups.add(postCouncelling1);
        radioGroups.add(postCouncelling2);
        radioGroups.add(postCouncelling3);
        radioGroups.add(postCouncelling4);
        radioGroups.add(postCouncelling5);
        radioGroups.add(postCouncelling6);
        radioGroups.add(postCouncelling7);
        radioGroups.add(postCouncelling8);
        radioGroups.add(postCouncelling9);
        radioGroups.add(postCouncelling10);
        radioGroups.add(postCouncelling11);
        radioGroups.add(postCouncelling12);
        radioGroups.add(postCouncelling13);
        radioGroups.add(postCouncelling14);
        return radioGroups;
    }

    private void saveAndEnrolForm(ClientForm ClientForm) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String facilityGuid = sharedPreferences.getString(PREF_FACILITY_GUID, "");
        FacilityRepository facilityRepository = new FacilityRepository(context);
        int facilityId = 0; //facilityRepository.getFacilityIdByGuid(facilityGuid);
        if (facilityId != 0) {
            if (validate()) {
                referralFormRepository = new ReferralFormRepository(context);


                long saves = referralFormRepository.updateReferralPostTest(ClientForm);
                if (saves == -1) {
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
                }
                clearForm();
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_FORM_ID, ClientForm.getId());
                mListener.onSkipButtonClicked(ENROL_FORM, bundle);
            }
        }
    }

    private void updateAndEnrolForm(ClientForm ClientForm) {
        if (validate()) {

            long saves = referralFormRepository.updateReferralPostTest(ClientForm);
            if (saves == -1) {
                Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
            }
            clearForm();
            Bundle bundle = new Bundle();
            bundle.putInt(EXTRA_FORM_ID, ClientForm.getId());
            mListener.onSkipButtonClicked(ENROL_FORM, bundle);
        }
    }

    private void saveAndReferForm(ClientForm ClientForm) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String facilityGuid = sharedPreferences.getString(PREF_FACILITY_GUID, "");
        FacilityRepository facilityRepository = new FacilityRepository(context);
        int facilityId = 0;//facilityRepository.getFacilityIdByGuid(facilityGuid);
        if(facilityId != 0) {
            if (validate()) {
                referralFormRepository = new ReferralFormRepository(context);


                long saves = referralFormRepository.updateReferralPostTest(ClientForm);
                if (saves == -1) {
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
                }
//                Bundle bundle = new Bundle();
//                bundle.putInt(EXTRA_FORM_ID, form.getId());
//                clearForm();
//                mListener.onSkipButtonClicked(REFERRAL_FORM, bundle);

            }
        }
        else {
            Toast.makeText(context, "Your facility is not yet registered", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAndReferForm(ClientForm ClientForm) {
        if (validate()) {
            referralFormRepository = new ReferralFormRepository(context);

            long saves = referralFormRepository.updateReferralPostTest(ClientForm);
            if (saves == -1) {
                Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
            }

        }
    }

    private void assignValuesToFields(ClientForm referralForm, View view) {
    }

    private boolean validate() {
        if (referralTestedBefore.getSpinner().getSelectedItemPosition() == 0) {
            Toast.makeText(context, getString(R.string.drop_down_validate, "answer first question"), Toast.LENGTH_SHORT).show();
            return false;
        }

        if ((postCouncelling1.getCheckedRadioButtonId() == -1) || (postCouncelling2.getCheckedRadioButtonId() == -1)||(postCouncelling3.getCheckedRadioButtonId() == -1)
                || (postCouncelling4.getCheckedRadioButtonId() == -1) || (postCouncelling5.getCheckedRadioButtonId() == -1) || (postCouncelling6.getCheckedRadioButtonId() == -1)
        ){
            Toast.makeText(context, getString(R.string.radio_validation, "Post Test Counselling"), Toast.LENGTH_LONG).show();
            return false;
        }

        if ((postCouncelling7.getCheckedRadioButtonId() == -1) || (postCouncelling8.getCheckedRadioButtonId() == -1)||(postCouncelling9.getCheckedRadioButtonId() == -1)
                || (postCouncelling10.getCheckedRadioButtonId() == -1) || (postCouncelling11.getCheckedRadioButtonId() == -1) || (postCouncelling12.getCheckedRadioButtonId() == -1)
        ){
            Toast.makeText(context, getString(R.string.radio_validation, "Post Test Counselling"), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void clearForm() {
        referralTestedBefore.getSpinner().setSelection(0);
    }

    private String getPostTestAsJson() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(getButtonsAsJson(postTestRadioGroups()));
        return jsonArray.toString();
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
    
    private void assignValuesFromJson(ArrayList<RadioGroup> groups, String json, View pview) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());

                Iterator<String> keys = jsonObject.keys();
                do {
                    populatePosttest(jsonObject, pview, keys );

                }while (keys.hasNext());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populatePosttest(JSONObject jsonObject, View pview, Iterator<String> keys){
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

    public interface OnFragmentInteractionListener {

        void onSkipButtonClicked(String fragmentTag, Bundle bundle);

        void onContinueButtonClicked(String fragmentTag, Fragment fragment, Bundle bundle);
    }

}
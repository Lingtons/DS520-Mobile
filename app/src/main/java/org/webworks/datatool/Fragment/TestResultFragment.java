package org.webworks.datatool.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.satsuware.usefulviews.LabelledSpinner;

import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.FacilityRepository;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Utility.BindingMeths;
import org.webworks.datatool.Utility.Constants;


public class TestResultFragment extends Fragment {

    private Context context;
    private OnFragmentInteractionListener mListener;
    private Button saveTestForm, continueTestForm, updateTestForm;
    private LabelledSpinner referralTestResult;
    private String PREFS_NAME;
    private String PREF_VERSION_CODE_KEY;
    private String PREF_USER_GUID;
    private String PREF_FACILITY_GUID;
    private String PREF_LAST_CODE;
    private final String POST_TEST_INFORMATION = "Post_Test";
    private final String FINGER_PRINT = "Finger_Print";
    ReferralFormRepository referralFormRepository;
    private final String EXTRA_FORM_ID = "FORM_ID";
    private boolean formFilled;
    private ClientForm form;

    public TestResultFragment() {
        // Required empty public constructor
    }

    public static TestResultFragment newInstance() {
        TestResultFragment fragment = new TestResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("HIV Result");
        context = getActivity().getApplicationContext();
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
        View view = inflater.inflate(R.layout.fragment_test_result, container, false);
        initializeFields(view);
        setListeners();
        if (formFilled) {
            assignValuesToFields(form);
        }
        return view;
    }

    @Override
    public void onResume() {
        getActivity().setTitle("HIV Result");
        super.onResume();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initializeFields(View view) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String facilityGuid = sharedPreferences.getString(PREF_FACILITY_GUID, "");

        FacilityRepository facilityRepository = new FacilityRepository(context);
        //String facilityCode = facilityRepository.getFacilityCode(facilityGuid);
        //facilityId = facilityRepository.getFacilityIdByGuid(facilityGuid);

        referralTestResult = (LabelledSpinner)view.findViewById(R.id.referral_test_result);
        saveTestForm = (Button)view.findViewById(R.id.btn_test_save_form);
        //skipTestForm = (Button)view.findViewById(R.id.btn_test_skip_form);
        continueTestForm = (Button)view.findViewById(R.id.btn_test_continue_form);
        updateTestForm = (Button)view.findViewById(R.id.btn_test_update_form);
        /*
        * Binding DropDown Data
        **/
        BindingMeths binding = new BindingMeths(context);
        binding.bindHivResult(referralTestResult);
        /*
        * Initial states
        * */
        continueTestForm.setVisibility(View.GONE);
        updateTestForm.setVisibility(View.GONE);
    }

    private void setListeners() {
        saveTestForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTestResultForm(form);
            }
        });

        updateTestForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTestedForm(form);
            }
        });

        continueTestForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_FORM_ID, form.getId());
                PostTestFragment fragment = PostTestFragment.newInstance();
                mListener.onContinueButtonClicked(POST_TEST_INFORMATION, fragment, bundle);
            }
        });
    }

    private void assignValuesToFields(ClientForm referralForm) {

    }

    private void submitTestResultForm(final ClientForm ClientForm ) {
        if (validate()) {
            referralFormRepository = new ReferralFormRepository(context);

            if(referralTestResult.getSpinner().getSelectedItemPosition() == 2 || referralTestResult.getSpinner().getSelectedItemPosition() == 3) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.form_title)
                        .setMessage(getString(R.string.negative_client_message, referralTestResult.getSpinner().getSelectedItem().toString()))
                        .setPositiveButton(R.string.snack_Bar_action,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        long saves = referralFormRepository.updateReferralResult(ClientForm);
                                        if (saves == -1) {
                                            Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
                                        }
                                        clearForm();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(EXTRA_FORM_ID, ClientForm.getId());
                                        mListener.onSkipButtonClicked(POST_TEST_INFORMATION, bundle);
                                    }
                                }
                        )
                        .setNegativeButton(R.string.update_alert_cancel_button,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .create().show();
            }
            else {
                //Positive Case

                long saves = referralFormRepository.updateReferralResult(ClientForm);
                if (saves == -1) {
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
                }
                clearForm();
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_FORM_ID, ClientForm.getId());
                //mListener.onSkipButtonClicked(POST_TEST_INFORMATION, bundle);
                if (Constants.ENABLE_PBS){
                    mListener.onSkipButtonClicked(FINGER_PRINT, bundle);
                }else{
                    mListener.onSkipButtonClicked(POST_TEST_INFORMATION, bundle);
                }
            }
        }
    }

    private void updateTestedForm(final ClientForm ClientForm) {
        if (validate()) {
            referralFormRepository = new ReferralFormRepository(context);
            if(referralTestResult.getSpinner().getSelectedItemPosition() == 2 || referralTestResult.getSpinner().getSelectedItemPosition() == 3) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.form_title)
                        .setMessage(context.getString(R.string.negative_client_message, referralTestResult.getSpinner().getSelectedItemPosition() == 2 ? "Negative" : "Indeterminate"))
                        .setPositiveButton(R.string.snack_Bar_action,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        long saves = referralFormRepository.updateReferralResult(ClientForm);
                                        if (saves == -1) {
                                            Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
                                        }

                                        clearForm();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(EXTRA_FORM_ID, ClientForm.getId());
                                        mListener.onSkipButtonClicked(POST_TEST_INFORMATION, bundle);
                                    }
                                }
                        )
                        .setNegativeButton(R.string.update_alert_cancel_button,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .create().show();
            }
            else {
                //Positive Case

                long saves = referralFormRepository.updateReferralResult(ClientForm);
                if (saves == -1) {
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
                }
                clearForm();
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_FORM_ID, ClientForm.getId());
                if (Constants.ENABLE_PBS){
                    mListener.onSkipButtonClicked(FINGER_PRINT, bundle);
                }else{
                    mListener.onSkipButtonClicked(POST_TEST_INFORMATION, bundle);
                }
            }
        }
    }

    private boolean validate() {
        if(referralTestResult.getSpinner().getSelectedItemPosition() == 0) {
            Toast.makeText(context, getString(R.string.drop_down_validate, "Client Result"), Toast.LENGTH_SHORT).show();
                return false;
        }
        return true;
    }

    private void clearForm() {
        referralTestResult.getSpinner().setSelection(0);
    }

    public interface OnFragmentInteractionListener {

        void onSkipButtonClicked(String fragmentTag, Bundle bundle);

        void onContinueButtonClicked(String fragmentTag, Fragment fragment, Bundle bundle);
    }
}

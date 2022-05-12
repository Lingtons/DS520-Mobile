package org.webworks.datatool.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.satsuware.usefulviews.LabelledSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.FacilityRepository;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Utility.BindingMeths;
import org.webworks.datatool.Utility.ClickToggleVisibility;
import org.webworks.datatool.Utility.UtilFuns;

import java.util.ArrayList;
import java.util.Iterator;

public class PretestFragment extends Fragment {

    private Context context;
    private OnFragmentInteractionListener mListener;
    private TextView knowledgeAssessment, hivRiskAssessment, clinicalTbScreening, syndromicStiScreening, referralHivRiskScore, referralClinicalScore, referralStiScore;
    private TableLayout knowledgeAssessmentChild, hivRiskAssessmentChild, clinicalTbScreeningChild, syndromicStiScreeningChild;
    private FrameLayout knowledgeAssesmentIconUp, knowledgeAssesmentIconDown, hivRiskAssessmentIconUp, hivRiskAssessmentIconDown;
    private FrameLayout clinicalTbScreeningIconUp, clinicalTbScreeningIconDown, syndromicStiScreeningIconUp, syndromicStiScreeningIconDown;
    private TableRow female1, female2, male1, male2, female3;
    private RadioGroup hivRiskAssessment1, hivRiskAssessment2, hivRiskAssessment3, hivRiskAssessment4, hivRiskAssessment5, hivRiskAssessment6;
    private RadioGroup knowledgeRadio1, knowledgeRadio2, knowledgeRadio3, knowledgeRadio4, knowledgeRadio5, knowledgeRadio6, knowledgeRadio7;
    private RadioGroup clinicalRadio1, clinicalRadio2, clinicalRadio3, clinicalRadio4;
    private RadioGroup syndromicRadio1, syndromicRadio2, syndromicRadio3, syndromicRadio4, syndromicRadio5;
    private Button savePreTestForm, skipPreTestForm, updatePreTestForm, saveClosePretestForm;
    private LabelledSpinner referralClientSex;
    private LinearLayout questionLayout;
    private String PREFS_NAME;
    private String PREF_VERSION_CODE_KEY;
    private String PREF_USER_GUID;
    private String PREF_FACILITY_GUID;
    private String PREF_LAST_CODE;
    private final String TEST_RESULTS = "Test_Results";
    private final String SOCIAL_DEMOGRAPHICS = "Social_Demographics";
    ReferralFormRepository referralFormRepository;
    private final String EXTRA_FORM_ID = "FORM_ID";
    private final String EXTRA_TRACED_CLIENT_ID = "TracedClientId";
    private boolean formFilled;
    private ClientForm form;
    private String TRACED_CLIENT_ID;
    private String packageName;


    public PretestFragment() {
        // Required empty public constructor
    }

    public static PretestFragment newInstance() {
        PretestFragment fragment = new PretestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Pre test counselling");
        context = getActivity().getApplicationContext();
        packageName = context.getPackageName();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pretest, container, false);
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
        getActivity().setTitle("Pre test counselling");
        super.onResume();
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

        referralClientSex = (LabelledSpinner)view.findViewById(R.id.referral_sex);
        knowledgeAssessment = (TextView) view.findViewById(R.id.knowledge_assessment_text);
        hivRiskAssessment = (TextView)view.findViewById(R.id.hiv_risk_assessment_text);
        clinicalTbScreening = (TextView)view.findViewById(R.id.clinical_tb_screening_text);
        syndromicStiScreening = (TextView)view.findViewById(R.id.syndromic_sti_screening_text);
        knowledgeAssesmentIconUp = (FrameLayout) view.findViewById(R.id.knowledge_assessment_icon_up);
        knowledgeAssesmentIconDown = (FrameLayout) view.findViewById(R.id.knowledge_assessment_icon_down);
        hivRiskAssessmentIconUp = (FrameLayout) view.findViewById(R.id.hiv_risk_assessment_icon_up);
        hivRiskAssessmentIconDown = (FrameLayout) view.findViewById(R.id.hiv_risk_assessment_icon_down);
        clinicalTbScreeningIconUp = (FrameLayout) view.findViewById(R.id.clinical_tb_screening_icon_up);
        clinicalTbScreeningIconDown = (FrameLayout) view.findViewById(R.id.clinical_tb_screening_icon_down);
        syndromicStiScreeningIconUp = (FrameLayout) view.findViewById(R.id.syndromic_sti_screening_icon_up);
        syndromicStiScreeningIconDown = (FrameLayout) view.findViewById(R.id.syndromic_sti_screening_icon_down);
        knowledgeAssessmentChild = (TableLayout)view.findViewById(R.id.knowledge_assessment);
        hivRiskAssessmentChild = (TableLayout)view.findViewById(R.id.hiv_risk_assessment);
        clinicalTbScreeningChild = (TableLayout)view.findViewById(R.id.clinical_tb_screening);
        syndromicStiScreeningChild = (TableLayout)view.findViewById(R.id.syndromic_sti_screening);
        savePreTestForm = (Button)view.findViewById(R.id.btn_pretest_save_form);
        skipPreTestForm = (Button)view.findViewById(R.id.btn_pretest_skip_form);
        updatePreTestForm = (Button)view.findViewById(R.id.btn_pretest_update_form);
        saveClosePretestForm = view.findViewById(R.id.btn_pretest_save_close_form);
        referralHivRiskScore = (TextView)view.findViewById(R.id.hiv_risk_score);
        referralClinicalScore = (TextView)view.findViewById(R.id.clinical_tb_score);
        referralStiScore = (TextView) view.findViewById(R.id.sti_score);
        hivRiskAssessment1 = (RadioGroup) view.findViewById(R.id.risk_assessment_ever_had_sexual_intercourse);
        hivRiskAssessment2 = (RadioGroup) view.findViewById(R.id.risk_assessment_blood_transfussion_in_last_3_month);
        hivRiskAssessment3 = (RadioGroup) view.findViewById(R.id.risk_assessment_unprotected_sex_with_casual_partner_in_last_3_months);
        hivRiskAssessment4 = (RadioGroup) view.findViewById(R.id.risk_assessment_unprotected_sex_with_regular_partner_in_last_3_months);
        hivRiskAssessment5 = (RadioGroup) view.findViewById(R.id.risk_assessment_sti_in_last_3_months);
        hivRiskAssessment6 = (RadioGroup) view.findViewById(R.id.risk_assessment_more_than_1_sex_partner_in_last_3_months);
        knowledgeRadio1 = (RadioGroup) view.findViewById(R.id.knowledge_assessment_previously_tested_negative);
        knowledgeRadio2 = (RadioGroup) view.findViewById(R.id.knowledge_assessment_pregnant);
        knowledgeRadio3 = (RadioGroup) view.findViewById(R.id.knowledge_assessment_informed_about_transmission_routes);
        knowledgeRadio4 = (RadioGroup) view.findViewById(R.id.knowledge_assessment_informed_about_risk_factors);
        knowledgeRadio5 = (RadioGroup) view.findViewById(R.id.knowledge_assessment_informed_on_preventing_transmission_method);
        knowledgeRadio6 = (RadioGroup) view.findViewById(R.id.knowledge_assessment_informed_about_possible_test_results);
        knowledgeRadio7 = (RadioGroup) view.findViewById(R.id.knowledge_assessment_informed_consent_for_testing_given);
        clinicalRadio1 = (RadioGroup) view.findViewById(R.id.tb_screening_current_cough);
        clinicalRadio2 = (RadioGroup) view.findViewById(R.id.tb_screening_weight_loss);
        clinicalRadio3 = (RadioGroup) view.findViewById(R.id.tb_screening_fever);
        clinicalRadio4 = (RadioGroup) view.findViewById(R.id.tb_screening_night_sweats);
        syndromicRadio1 = (RadioGroup) view.findViewById(R.id.sti_screening_complain_of_vaginal_discharge);
        syndromicRadio2 = (RadioGroup) view.findViewById(R.id.sti_screening_complain_of_lower_abominal_pain);
        syndromicRadio3 = (RadioGroup) view.findViewById(R.id.sti_screening_complain_of_urethral_discharge);
        syndromicRadio4 = (RadioGroup) view.findViewById(R.id.sti_screening_complain_of_scrotal_swelling);
        syndromicRadio5 = (RadioGroup) view.findViewById(R.id.sti_screening_complain_of_genital_sore);
        female1 = (TableRow) view.findViewById(R.id.sti_female1);
        female2 = (TableRow) view.findViewById(R.id.sti_female2);
        male1 = (TableRow) view.findViewById(R.id.sti_male1);
        male2 = (TableRow) view.findViewById(R.id.sti_male2);
        female3 = (TableRow) view.findViewById(R.id.pretest_female_question);

        questionLayout = view.findViewById(R.id.questionBody);

        if (referralClientSex.getSpinner().getSelectedItemPosition() == 0){
            questionLayout.setVisibility(View.GONE);
        }else {
            questionLayout.setVisibility(View.VISIBLE);
        }

        /*
        * Binding DropDown Data
        **/
        BindingMeths binding = new BindingMeths(context);
        binding.bindSex(referralClientSex);
        //binding.bindHivResult(referralHivResult);
        /*
        * Initial states
        * */
        knowledgeAssessmentChild.setVisibility(View.GONE);
        hivRiskAssessmentChild.setVisibility(View.GONE);
        clinicalTbScreeningChild.setVisibility(View.GONE);
        syndromicStiScreeningChild.setVisibility(View.GONE);
        updatePreTestForm.setVisibility(View.GONE);
        saveClosePretestForm.setVisibility(View.GONE);
        knowledgeAssesmentIconUp.setVisibility(View.GONE);
        hivRiskAssessmentIconUp.setVisibility(View.GONE);
        clinicalTbScreeningIconUp.setVisibility(View.GONE);
        syndromicStiScreeningIconUp.setVisibility(View.GONE);
        //knowledgeAssesmentIconUp.setBackgroundResource(R.drawable.ic_arrow_drop_up);
        if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)) {
            knowledgeAssesmentIconUp.setBackgroundResource(R.drawable.ic_arrow_drop_up);
            knowledgeAssesmentIconDown.setBackgroundResource(R.drawable.ic_arrow_drop_down);
            hivRiskAssessmentIconUp.setBackgroundResource(R.drawable.ic_arrow_drop_up);
            hivRiskAssessmentIconDown.setBackgroundResource(R.drawable.ic_arrow_drop_down);
            clinicalTbScreeningIconUp.setBackgroundResource(R.drawable.ic_arrow_drop_up);
            clinicalTbScreeningIconDown.setBackgroundResource(R.drawable.ic_arrow_drop_down);
            syndromicStiScreeningIconUp.setBackgroundResource(R.drawable.ic_arrow_drop_up);
            syndromicStiScreeningIconDown.setBackgroundResource(R.drawable.ic_arrow_drop_down);
        }
        /*
        * Gender Logic
        * */
        if (form !=null){

            //determine if pretest has been filled before


        }

    }

    private void setListeners() {
        knowledgeAssessment.setOnClickListener(new ClickToggleVisibility(context, knowledgeAssessmentChild, knowledgeAssesmentIconUp, knowledgeAssesmentIconDown));
        hivRiskAssessment.setOnClickListener(new ClickToggleVisibility(context, hivRiskAssessmentChild, hivRiskAssessmentIconUp, hivRiskAssessmentIconDown));
        clinicalTbScreening.setOnClickListener(new ClickToggleVisibility(context, clinicalTbScreeningChild, clinicalTbScreeningIconUp, clinicalTbScreeningIconDown));
        syndromicStiScreening.setOnClickListener(new ClickToggleVisibility(context, syndromicStiScreeningChild, syndromicStiScreeningIconUp, syndromicStiScreeningIconDown));


        toggleSave();

        savePreTestForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // submitPreTestForm();
                updatePretestedForm(form);
            }
        });

        skipPreTestForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(validate()){
                  if (mListener != null) {
                      Bundle bundle = new Bundle();
                      bundle.putInt(EXTRA_FORM_ID, form.getId());
                      SocialDemoFragment fragment = SocialDemoFragment.newInstance();
                      mListener.onContinueButtonClicked(SOCIAL_DEMOGRAPHICS, fragment, bundle);
                  }
              }

            }
        });

        saveClosePretestForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // saveAndClosePretestForm();
            }
        });

        hivRiskAssessment1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralHivRiskScore.setText(String.valueOf(getScoreCount(riskRadioGroups())));
                toggleSave();
            }
        });
        hivRiskAssessment1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralHivRiskScore.setText(String.valueOf(getScoreCount(riskRadioGroups())));
                toggleSave();
            }
        });
        hivRiskAssessment2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralHivRiskScore.setText(String.valueOf(getScoreCount(riskRadioGroups())));
                toggleSave();
            }
        });
        hivRiskAssessment3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralHivRiskScore.setText(String.valueOf(getScoreCount(riskRadioGroups())));
                toggleSave();
            }
        });
        hivRiskAssessment4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralHivRiskScore.setText(String.valueOf(getScoreCount(riskRadioGroups())));
                toggleSave();
            }
        });
        hivRiskAssessment5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralHivRiskScore.setText(String.valueOf(getScoreCount(riskRadioGroups())));
                toggleSave();
            }
        });
        hivRiskAssessment6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralHivRiskScore.setText(String.valueOf(getScoreCount(riskRadioGroups())));
                toggleSave();
            }
        });
        clinicalRadio1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralClinicalScore.setText(String.valueOf(getScoreCount(clinicalRadioGroups())));
                toggleSave();
            }
        });
        clinicalRadio2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralClinicalScore.setText(String.valueOf(getScoreCount(clinicalRadioGroups())));
                toggleSave();
            }
        });
        clinicalRadio3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralClinicalScore.setText(String.valueOf(getScoreCount(clinicalRadioGroups())));
                toggleSave();
            }
        });
        clinicalRadio4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralClinicalScore.setText(String.valueOf(getScoreCount(clinicalRadioGroups())));
                toggleSave();
            }
        });
        syndromicRadio1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralStiScore.setText(String.valueOf(getScoreCount(syndromicRadioGroups())));
                toggleSave();
            }
        });
        syndromicRadio2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralStiScore.setText(String.valueOf(getScoreCount(syndromicRadioGroups())));
                toggleSave();
            }
        });
        syndromicRadio3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralStiScore.setText(String.valueOf(getScoreCount(syndromicRadioGroups())));
                toggleSave();
            }
        });
        syndromicRadio4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralStiScore.setText(String.valueOf(getScoreCount(syndromicRadioGroups())));
                toggleSave();
            }
        });
        syndromicRadio5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                referralStiScore.setText(String.valueOf(getScoreCount(syndromicRadioGroups())));
                toggleSave();
            }
        });

        referralClientSex.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(referralClientSex.getSpinner().getSelectedItemPosition() == 0){
                    questionLayout.setVisibility(View.GONE);
                }
                else if (referralClientSex.getSpinner().getSelectedItemPosition() == 1){
                    questionLayout.setVisibility(View.VISIBLE);
                    //male, disable female questions
                    female1.setVisibility(View.GONE);
                    female2.setVisibility(View.GONE);
                    female3.setVisibility(View.GONE);
                    male1.setVisibility(View.VISIBLE);
                    male2.setVisibility(View.VISIBLE);
                }else if(referralClientSex.getSpinner().getSelectedItemPosition() == 2){
                    questionLayout.setVisibility(View.VISIBLE);
                    //female disable male questions
                    male1.setVisibility(View.GONE);
                    male2.setVisibility(View.GONE);
                    female1.setVisibility(View.VISIBLE);
                    female2.setVisibility(View.VISIBLE);
                    female3.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updatePreTestForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePretestedForm(form);
            }
        });
    }

    private ArrayList<RadioGroup> riskRadioGroups() {
        ArrayList<RadioGroup> radioGroups = new ArrayList<RadioGroup>();
        radioGroups.add(hivRiskAssessment1);
        radioGroups.add(hivRiskAssessment2);
        radioGroups.add(hivRiskAssessment3);
        radioGroups.add(hivRiskAssessment4);
        radioGroups.add(hivRiskAssessment5);
        radioGroups.add(hivRiskAssessment6);
        return radioGroups;
    }

    private ArrayList<RadioGroup> knowledgeRadioGroups() {
        ArrayList<RadioGroup> radioGroups = new ArrayList<RadioGroup>();
        radioGroups.add(knowledgeRadio1);
        radioGroups.add(knowledgeRadio2);
        radioGroups.add(knowledgeRadio3);
        radioGroups.add(knowledgeRadio4);
        radioGroups.add(knowledgeRadio5);
        radioGroups.add(knowledgeRadio6);
        radioGroups.add(knowledgeRadio7);
        return radioGroups;
    }

    private ArrayList<RadioGroup> clinicalRadioGroups() {
        ArrayList<RadioGroup> radioGroups = new ArrayList<RadioGroup>();
        radioGroups.add(clinicalRadio1);
        radioGroups.add(clinicalRadio2);
        radioGroups.add(clinicalRadio3);
        radioGroups.add(clinicalRadio4);
        return radioGroups;
    }

    private ArrayList<RadioGroup> syndromicRadioGroups() {
        ArrayList<RadioGroup> radioGroups = new ArrayList<RadioGroup>();
        radioGroups.add(syndromicRadio1);
        radioGroups.add(syndromicRadio2);
        radioGroups.add(syndromicRadio3);
        radioGroups.add(syndromicRadio4);
        radioGroups.add(syndromicRadio5);
        return radioGroups;
    }

    private ArrayList<TextView> scoreList() {
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(referralHivRiskScore);
        textViews.add(referralClinicalScore);
        textViews.add(referralStiScore);
        return textViews;
    }

    private void submitPreTestForm() {
        if (validate()) {
            referralFormRepository = new ReferralFormRepository(context);
            final ClientForm ClientForm = new ClientForm();
            //set client ID once
            ClientForm.setClientIdentifier(UtilFuns.generateClientID(context, getActivity()));
            if (TRACED_CLIENT_ID != null){
            }
                long saves = referralFormRepository.saveReferralForm(ClientForm);
                if (saves == -1) {
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
                    clearForm();
                    Bundle bundle = new Bundle();
                    bundle.putInt(EXTRA_FORM_ID, (int)saves);
                    mListener.onSkipButtonClicked(SOCIAL_DEMOGRAPHICS, bundle);
                }

            //}
        }
    }

    private void updatePretestedForm(ClientForm ClientForm) {
        if (validate()) {
            referralFormRepository = new ReferralFormRepository(context);

            long saves = referralFormRepository.updateReferralPretest(ClientForm);
            if (saves == -1) {
                Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
            }
            clearForm();
            Bundle bundle = new Bundle();
            bundle.putInt(EXTRA_FORM_ID, ClientForm.getId());
            mListener.onSkipButtonClicked(SOCIAL_DEMOGRAPHICS, bundle);
        }
    }

//    private void saveAndClosePretestForm(){
//        if (validate()){
//            referralFormRepository = new ReferralFormRepository(context);
//            final ClientForm ClientForm = new ClientForm();
//            ClientForm.setPretest(getPreTestAsJson());
//            ClientForm.setFormDate(UtilFuns.getTodaysDate());
//            //set client ID once
//            ClientForm.setClientIdentifier(UtilFuns.generateClientID(context, getActivity()));
//
//            ClientForm.setSex(referralClientSex.getSpinner().getSelectedItemPosition());
//            new AlertDialog.Builder(getActivity())
//                    .setTitle(R.string.form_title)
//                    .setMessage(getString(R.string.save_and_close, "Save and Close"))
//                    .setPositiveButton(R.string.snack_Bar_action,
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    ClientForm.setProgress(5);
//                                    ClientForm.setStoppedAtPreTest(1);
//                                    if (TRACED_CLIENT_ID != null){
//                                        ClientForm.setIndexClientId(TRACED_CLIENT_ID);
//                                        ClientForm.setTraced(1);
//                                        ClientForm.setIndexClient(1);
//                                    }
//                                    long saves = referralFormRepository.saveReferralForm(ClientForm);
//                                    if (saves == -1) {
//                                        Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
//                                    }
//                                    else {
//                                        Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
//                                    }
//                                    Intent intent = new Intent(context, HivTestingActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                    dialog.dismiss();
//                                    clearForm();
//                                }
//                            }
//                    )
//                    .setNegativeButton(R.string.update_alert_cancel_button,
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    dialog.dismiss();
//                                }
//                            }
//                    )
//                    .create().show();
//        }
//
//    }
    private boolean validate() {
        if (referralClientSex.getSpinner().getSelectedItemPosition() == 0) {
            Toast.makeText(context, getString(R.string.drop_down_validate, "Sex"), Toast.LENGTH_SHORT).show();
            return false;
        }

        if ((hivRiskAssessment1.getCheckedRadioButtonId() == -1) || (hivRiskAssessment2.getCheckedRadioButtonId() == -1)||(hivRiskAssessment3.getCheckedRadioButtonId() == -1)
        || (hivRiskAssessment4.getCheckedRadioButtonId() == -1) || (hivRiskAssessment5.getCheckedRadioButtonId() == -1) || (hivRiskAssessment6.getCheckedRadioButtonId() == -1)
        ){
            Toast.makeText(context, getString(R.string.radio_validation, "Risk Assessment"), Toast.LENGTH_LONG).show();
            return false;
        }

        if ((clinicalRadio1.getCheckedRadioButtonId() == -1) || (clinicalRadio2.getCheckedRadioButtonId() == -1)||(clinicalRadio3.getCheckedRadioButtonId() == -1)
                || (clinicalRadio4.getCheckedRadioButtonId() == -1)
        ){
            Toast.makeText(context, getString(R.string.radio_validation, "TB Screening"), Toast.LENGTH_LONG).show();
            return false;
        }

        if ((knowledgeRadio1.getCheckedRadioButtonId() == -1) || (knowledgeRadio3.getCheckedRadioButtonId() == -1) || (knowledgeRadio4.getCheckedRadioButtonId() == -1) || (knowledgeRadio7.getCheckedRadioButtonId() == -1)){
            Toast.makeText(context, getString(R.string.radio_validation, "displayed knowledge assessment"), Toast.LENGTH_LONG).show();
            return false;
        }

        if (syndromicRadio5.getCheckedRadioButtonId() == -1){
            Toast.makeText(context, getString(R.string.radio_validation, "displayed syndromic assessment"), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void clearForm() {
        referralHivRiskScore.setText("");
        hivRiskAssessment1.clearCheck();
        hivRiskAssessment2.clearCheck();
        hivRiskAssessment3.clearCheck();
        hivRiskAssessment4.clearCheck();
        hivRiskAssessment5.clearCheck();
        hivRiskAssessment6.clearCheck();
        referralClientSex.getSpinner().setSelection(0);
    }

    private void assignValuesToFields(ClientForm referralForm, View view) {

    }

    private String getPreTestAsJson() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(getButtonsAsJson(knowledgeRadioGroups()));
        jsonArray.put(getButtonsAsJson((riskRadioGroups())));
        jsonArray.put(getButtonsAsJson(clinicalRadioGroups()));
        jsonArray.put(getButtonsAsJson(syndromicRadioGroups()));
        jsonArray.put(getScoreAsJson(scoreList()));
        return jsonArray.toString();
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

    private void assignValuesFromJson(ArrayList<RadioGroup> knowledge, ArrayList<RadioGroup> risk, ArrayList<RadioGroup> clinical,
                                      ArrayList<RadioGroup> syndromic, ArrayList<TextView> scores, String json, View pview) {


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
                            populatePretest(jsonObject, pview, keys);
                        } while (keys.hasNext());
                        break;
                    }
                    //risk
                    case 1:{
                        jsonObject = new JSONObject(jsonArray.get(1).toString());
                        Iterator<String> keys = jsonObject.keys();
                        do {
                            populatePretest(jsonObject, pview, keys);
                        } while (keys.hasNext());
                        break;
                    }
                    //clinical TB
                    case 2:{
                        jsonObject = new JSONObject(jsonArray.get(2).toString());
                        Iterator<String> keys = jsonObject.keys();
                        do {
                            populatePretest(jsonObject, pview, keys);
                        } while (keys.hasNext());
                        break;

                    }
                    //syndromic
                    case 3:{
                        jsonObject = new JSONObject(jsonArray.get(3).toString());
                        Iterator<String> keys = jsonObject.keys();
                        do {
                            populatePretest(jsonObject, pview, keys);
                        } while (keys.hasNext());
                        break;
                    }
                    //scores
                    case 4:
                        jsonObject = new JSONObject(jsonArray.get(4).toString());
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
        int riskScore = Integer.parseInt(referralHivRiskScore.getText().toString());
        int clinicalScore = Integer.parseInt(referralClinicalScore.getText().toString());
        int stiScore = Integer.parseInt(referralStiScore.getText().toString());
        final int totalScore = riskScore + clinicalScore + stiScore;



    }

    private void populatePretest(JSONObject jsonObject, View pview, Iterator<String> keys){
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

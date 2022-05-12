package org.webworks.datatool.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.satsuware.usefulviews.LabelledSpinner;

import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.Model.FingerPrint;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.FacilityRepository;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Repository.Repository;
import org.webworks.datatool.Utility.BindingMeths;
import org.webworks.datatool.Utility.GroupButton;
import org.webworks.datatool.Utility.UtilFuns;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import static android.Manifest.permission.CAMERA;

public class SocialDemoFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Context context;
    private EditText referralClientName;
    private EditText referralClientLastname, txt_image_code;
    private Button saveDemographForm, continueDemographForm, updateDemographForm;
    private LinearLayout prevTested, indexTest, referralRecencyLayout;
    private String PREFS_NAME;
    private String PREF_VERSION_CODE_KEY;
    private String PREF_USER_GUID;
    private String PREF_FACILITY_GUID;
    private String PREF_LAST_CODE;
    private boolean fromDob = false, estimatedDob;
    private String clientAge, facilityName;
    private int facilityId, clientFormId;
    private ReferralFormRepository referralFormRepository;
    private final String FINGER_PRINT_FORM = "Finger_Print";
    private final String TEST_RESULTS = "Test_Results";
    private final String EXTRA_FORM_ID = "FORM_ID";
    private boolean formFilled;
    private ClientForm form;
    private Repository repository;
    private TextInputLayout care_giver_layout;
    private ImageButton image_capture;
    private ImageView image_view;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;



    public SocialDemoFragment() {
        // Required empty public constructor
    }

    public static SocialDemoFragment newInstance() {
        SocialDemoFragment fragment = new SocialDemoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Bio Data");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_social_demo, container, false);
        initializeFields(view);
        setListeners();
        if (formFilled) {
            assignValuesToFields(form);
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
        getActivity().setTitle("Bio Data");
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
        image_capture = view.findViewById(R.id.btn_capture_image);
        image_view = view.findViewById(R.id.image_view);
        txt_image_code = view.findViewById(R.id.txt_image_code);

        FacilityRepository facilityRepository = new FacilityRepository(context);
        //facilityId = facilityRepository.getFacilityIdByGuid(facilityGuid);

        referralClientName = (EditText)view.findViewById(R.id.referral_client_name);
        referralClientLastname = (EditText)view.findViewById(R.id.referral_client_lastname);
        saveDemographForm = (Button)view.findViewById(R.id.btn_demo_save_form);
        continueDemographForm = (Button)view.findViewById(R.id.btn_demo_continue_form);
        updateDemographForm = (Button)view.findViewById(R.id.btn_demo_update_form);
        /*
        * Binding DropDown Data
        **/
        BindingMeths binding = new BindingMeths(context);

        //referralRecencyLayout.setVisibility(View.GONE);
        continueDemographForm.setVisibility(View.GONE);
        updateDemographForm.setVisibility(View.GONE);
    }

    private void setListeners() {

        saveDemographForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSocialDemoForm();
            }
        });

        continueDemographForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(validate()){
                   if (mListener != null) {
                       Bundle bundle = new Bundle();
                       bundle.putInt(EXTRA_FORM_ID, form.getId());
                       TestResultFragment testResultFragment = TestResultFragment.newInstance();
                       mListener.onContinueButtonClicked(TEST_RESULTS, testResultFragment, bundle);
                   }
               }
            }
        });
        updateDemographForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSocialDemoForm(form);
            }
        });

        image_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }else{
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });


    }

    private void assignValuesToFields(final ClientForm referralForm) {
        repository = new Repository(context);
        //Disable();
        referralClientName.setText(referralForm.getClientName());
        referralClientLastname.setText(referralForm.getClientLastname());


        saveDemographForm.setVisibility(View.GONE);
        continueDemographForm.setVisibility(View.GONE);

        if (referralForm.getClientName() != null){
            continueDemographForm.setVisibility(View.VISIBLE);
            updateDemographForm.setVisibility(View.VISIBLE);
        }else{
            saveDemographForm.setVisibility(View.VISIBLE);
        }

    }

    private void submitSocialDemoForm() {
        if (validate()) {
            referralFormRepository = new ReferralFormRepository(context);
            //long saved;

            final ClientForm ClientForm = new ClientForm();
            ClientForm.setClientIdentifier(UtilFuns.generateClientID(context, getActivity()));
            ClientForm.setClientName(referralClientName.getText().toString().trim());
            ClientForm.setClientLastname(referralClientLastname.getText().toString().trim());
            ClientForm.setEncodedImage(txt_image_code.getText() != null ? txt_image_code.getText().toString() : null);
                long saves = referralFormRepository.saveReferralForm(ClientForm);
                if (saves == -1) {
                    Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
                    clearForm();
                    Bundle bundle = new Bundle();
                    bundle.putInt(EXTRA_FORM_ID, (int)saves);
                    FingerPrintFragment fragment = FingerPrintFragment.newInstance();
                    mListener.onContinueButtonClicked(FINGER_PRINT_FORM, fragment, bundle);
                }
            //}
        }
    }

    private void updateSocialDemoForm(ClientForm ClientForm) {
        if (validate()) {
            referralFormRepository = new ReferralFormRepository(context);
            ClientForm.setClientName(referralClientName.getText().toString().trim());
            ClientForm.setClientLastname(referralClientLastname.getText().toString().trim());

            long saves = referralFormRepository.updateReferralSocialDemo(ClientForm);
            if (saves == -1) {
                Toast.makeText(context, R.string.save_error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, R.string.save_success, Toast.LENGTH_LONG).show();
                clearForm();
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_FORM_ID, ClientForm.getId());
                FingerPrintFragment fragment = FingerPrintFragment.newInstance();
                mListener.onContinueButtonClicked(FINGER_PRINT_FORM, fragment, bundle);
            }
        }
    }

    private boolean validate() {
        if (referralClientName.getText().toString().equals("")) {
            Toast.makeText(context, getString(R.string.validate_error, "Client firstname"), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(referralClientLastname.getText().toString().equals("")) {
            Toast.makeText(context, getString(R.string.validate_error, "Client lastname"), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearForm() {
        referralClientName.setText("");
        referralClientLastname.setText("");
    }

    public interface OnFragmentInteractionListener {
        void onContinueButtonClicked(String fragmentTag, Fragment fragment, Bundle bundle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            txt_image_code.setText(UtilFuns.encodeImage(photo));
            txt_image_code.setVisibility(View.GONE);
            image_view.setImageBitmap(photo);
        }
    }
}
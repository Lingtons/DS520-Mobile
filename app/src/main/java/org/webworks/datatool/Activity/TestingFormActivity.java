package org.webworks.datatool.Activity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.webworks.datatool.Fragment.FingerPrintFragment;
import org.webworks.datatool.Fragment.PostTestFragment;
import org.webworks.datatool.Fragment.PretestFragment;
import org.webworks.datatool.Fragment.ReferralFragment;
import org.webworks.datatool.Fragment.RiskStratificationFragment;
import org.webworks.datatool.Fragment.SocialDemoFragment;
import org.webworks.datatool.Fragment.TestResultFragment;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Session.SessionManager;

import java.util.HashMap;

public class TestingFormActivity extends SessionManager implements SocialDemoFragment.OnFragmentInteractionListener, TestResultFragment.OnFragmentInteractionListener,
        PretestFragment.OnFragmentInteractionListener, ReferralFragment.OnFragmentInteractionListener, PostTestFragment.OnFragmentInteractionListener,
        RiskStratificationFragment.OnFragmentInteractionListener, FingerPrintFragment.OnFragmentInteractionListener {
Context context;
    private HashMap<Integer, String> fragmentTagMap;
    private final String SOCIAL_DEMOGRAPHICS = "Social_Demographics";
//    private final String PRE_TEST_INFORMATION = "Pre_Test";
//    private final String TEST_RESULTS = "Test_Results";
//    private final String REFERRAL_FORM = "Referral_Form";
    private final String FINGER_PRINT_FORM = "Finger_Print";
//    private final String POST_TEST_INFORMATION = "Post_Test";
//    private final String RISK_STRATIFICATION = "Risk_Stratification";
//    private final String ENROL_FORM = "Enrol_Form";
    private final String EXTRA_FORM_ID = "FORM_ID";
//    private final String EXTRA_TRACED = "Traced";
//    private final String EXTRA_TRACED_CLIENT_ID = "TracedClientId";
    private final String FORM_PROGRESS = "FORM_PROGRESS";
    private int formId;
//    private String tracedClientId;
//    private int formProgress;
//    private ReferralFormRepository referralFormRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_form);

        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRA_FORM_ID) && intent.hasExtra(FORM_PROGRESS)) {
                formId = intent.getIntExtra(EXTRA_FORM_ID, 0);

                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_FORM_ID, formId);

                SocialDemoFragment socialDemoFragment = new SocialDemoFragment();
                socialDemoFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, socialDemoFragment, SOCIAL_DEMOGRAPHICS).commit();
            }
            else {
                SocialDemoFragment socialDemoFragment = new SocialDemoFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, socialDemoFragment, SOCIAL_DEMOGRAPHICS).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.previous) {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
            return true;
        }
        if (id == R.id.home) {
            finish();
            Intent intent = new Intent(context, TestingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void switchFragments(String fragmentTag, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragmentTag) {
            case SOCIAL_DEMOGRAPHICS:
                SocialDemoFragment socialDemoFragment = SocialDemoFragment.newInstance();
                socialDemoFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, socialDemoFragment, SOCIAL_DEMOGRAPHICS);
                break;
//            case PRE_TEST_INFORMATION:
//                PretestFragment pretestFragment = PretestFragment.newInstance();
//                pretestFragment.setArguments(bundle);
//                transaction.replace(R.id.fragment_container, pretestFragment, PRE_TEST_INFORMATION);
//                break;
//            case TEST_RESULTS:
//                TestResultFragment testResultFragment = TestResultFragment.newInstance();
//                testResultFragment.setArguments(bundle);
//                transaction.replace(R.id.fragment_container, testResultFragment, TEST_RESULTS);
//                break;
//            case REFERRAL_FORM:
//                ReferralFragment referralFragment = ReferralFragment.newInstance();
//                referralFragment.setArguments(bundle);
//                transaction.replace(R.id.fragment_container, referralFragment, REFERRAL_FORM);
//                break;
//            case POST_TEST_INFORMATION:
//                PostTestFragment postTestFragment = PostTestFragment.newInstance();
//                postTestFragment.setArguments(bundle);
//                transaction.replace(R.id.fragment_container, postTestFragment, POST_TEST_INFORMATION);
//                break;
            case FINGER_PRINT_FORM:
                FingerPrintFragment fingerPrintFragment = FingerPrintFragment.newInstance();
                fingerPrintFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container, fingerPrintFragment, FINGER_PRINT_FORM);
                break;

        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSkipButtonClicked(String fragmentTag, Bundle bundle) {
        switchFragments(fragmentTag, bundle);
    }

    public void onContinueButtonClicked(String fragmentTag, Fragment fragment, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

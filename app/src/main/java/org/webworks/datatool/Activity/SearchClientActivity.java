package org.webworks.datatool.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import org.webworks.datatool.Adapter.ClientAdapter;
import org.webworks.datatool.Adapter.SearchClientAdapter;
import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Session.SessionManager;
import org.webworks.datatool.Utility.ItemClickSupport;

import java.util.ArrayList;

public class SearchClientActivity extends SessionManager {

    Context context;
    ReferralFormRepository referralFormRepository;
    TextView textView;
    private final String EXTRA_FORM_ID = "FORM_ID";
    private final String FORM_PROGRESS = "FORM_PROGRESS";
    private ListView clientlistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_client);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleSearch(getIntent());
    }

    protected void onNewIntent(Intent intent) {
        handleSearch(intent);
    }

    /**
     * Method handles search intents
     * */
    private void handleSearch(Intent intent) {

        initializeViews();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            showForms(query);
        }
    }


    /**
     * Method initializes views
     * */
    public void initializeViews(){
        clientlistview = findViewById(R.id.search_list);
        textView = (TextView)findViewById(R.id.empty_list);
    }


    /**
     * Method loads the forms in a grid view with date headers
     * */
    public void showForms(String code) {
        referralFormRepository = new ReferralFormRepository(context);
        final ArrayList<ClientForm> forms = referralFormRepository.getAllReferralForm(code);

        if(forms.isEmpty()) {
            textView.setText(getResources().getString(R.string.client_dont_exist));
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            final SearchClientAdapter searchClientAdapter = new SearchClientAdapter(context, forms);
            clientlistview.setAdapter(searchClientAdapter);
            searchClientAdapter.notifyDataSetChanged();

            clientlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    ArrayList<Integer> formDetails = referralFormRepository.getReferralFormDetailById(forms.get(position).getId());
                    int uploaded = formDetails.get(0);
                    int progress = formDetails.get(1); //ranges from 1-5 for the forms
                    int referred = formDetails.get(2);

                    Intent intent = new Intent(context, TestingFormActivity.class);
                    intent.putExtra(EXTRA_FORM_ID, forms.get(position).getId());
                    intent.putExtra(FORM_PROGRESS, progress);
                    startActivity(intent);

                }
            });
                }

        }



    }

package org.webworks.datatool.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import org.webworks.datatool.Activity.TestingFormActivity;
import org.webworks.datatool.Adapter.ClientAdapter;
import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.Model.Contact;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Utility.ItemClickSupport;
import java.util.ArrayList;

public class HIVTestingFragment extends Fragment {
    RecyclerView recyclerView;
    private PullToLoadView pullToLoadClients;
    Context context;
    private ClientAdapter clientAdapter;
    TextView textView;
    ReferralFormRepository referralFormRepository;
    private OnFragmentInteractionListener mListener;
    private final String EXTRA_FORM_ID = "FORM_ID";
    private final String FORM_PROGRESS = "FORM_PROGRESS";
    private boolean isLoading = false;
    private boolean hasLoadedAll = false;
    private int nextPage;
    private ArrayList<ClientForm> forms;

    public HIVTestingFragment() {
        // Required empty public constructor
    }

    public static HIVTestingFragment newInstance() {
        HIVTestingFragment fragment = new HIVTestingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        referralFormRepository = new ReferralFormRepository(context);
        getActivity().setTitle("Entries");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hivtesting, container, false);
        initializeViews(view);
        assignValues();
        setClickListeners();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        getActivity().setTitle("Entries");
        super.onResume();
    }

    private void initializeViews(View view) {
        pullToLoadClients = view.findViewById(R.id.pullToLoadClients);
        textView = view.findViewById(R.id.empty_list);

        //recycler view
        recyclerView = pullToLoadClients.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    private void assignValues() {
        forms = referralFormRepository.getAllHTSForm();
        if(forms.isEmpty()) {
            textView.setText("You have no data");
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            clientAdapter = new ClientAdapter(context, forms);
            recyclerView.setAdapter(clientAdapter);
            initializeLoader();

            ItemClickSupport.addTo(recyclerView)
                    .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                                try {
                        ArrayList<Integer> formDetails = referralFormRepository.getReferralFormDetailById(forms.get(position).getId());
                        //formDetails[0]=uploaded, formDetails[1]=progress, formDetails[2]=referred
                        int uploaded = formDetails.get(0);
                        int progress = formDetails.get(1); //ranges from 1-5 for the forms
                        int referred = formDetails.get(2);

                        Intent intent = new Intent(context, TestingFormActivity.class);
                        intent.putExtra(EXTRA_FORM_ID, forms.get(position).getId());
                        intent.putExtra(FORM_PROGRESS, progress);
                        startActivity(intent);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                        }
                    });
        }
    }

    private void setClickListeners() {
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initializeLoader()
    {
        pullToLoadClients.isLoadMoreEnabled(true);
        pullToLoadClients.setPullCallback(new PullCallback() {

            //LOAD MORE DATA
            @Override
            public void onLoadMore() {
                loadData(nextPage);
            }

            //REFRESH AND TAKE US TO FIRST PAGE
            @Override
            public void onRefresh() {
                clientAdapter.clear();
                hasLoadedAll=false;
                loadData(0);
            }

            //IS LOADING
            @Override
            public boolean isLoading() {
                return isLoading;
            }

            //CURRENT PAGE LOADED
            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadedAll;
            }
        });

        pullToLoadClients.initLoad();
    }

    public void loadData(final int page)
    {
        isLoading=true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final ArrayList<ClientForm> newforms  = referralFormRepository.getAllHTSForm();

                if (newforms.size() > 0){
                    forms.clear();
                    forms.addAll(newforms);

                    pullToLoadClients.setComplete();
                    isLoading=false;
                    nextPage=page+1;
                    // notifying list adapter about data changes
                    // so that it renders the list view with updated data
                    clientAdapter.notifyDataSetChanged();
                }else {
                    textView.setVisibility(View.VISIBLE);
                    pullToLoadClients.setComplete();
                    isLoading=false;
                }

            }
        },3000);
    }
}

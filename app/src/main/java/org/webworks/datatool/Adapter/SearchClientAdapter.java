package org.webworks.datatool.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.R;
import org.webworks.datatool.Repository.FacilityRepository;

import java.util.ArrayList;

public class SearchClientAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ClientForm> clientForms;
    private TextView name, code, referredTo, toOrFrom;
    private CheckedTextView uploaded;

    public SearchClientAdapter(Context context, ArrayList<ClientForm> clientForms) {
        this.context = context;
        this.clientForms = clientForms;
    }

    @Override
    public int getCount() {
        return clientForms.size();
    }

    @Override
    public ClientForm getItem(int i) {
        return clientForms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return clientForms.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.grid_single_layout, null);
        init(view);
        assign(clientForms.get(position));
        return view;
    }

    private void init(View itemView) {
        name = itemView.findViewById(R.id.name);
        code = itemView.findViewById(R.id.code);
        referredTo = itemView.findViewById(R.id.referred);
        toOrFrom = itemView.findViewById(R.id.to_or_from);
        uploaded = itemView.findViewById(R.id.uploaded);
    }

    private void assign(ClientForm form){
        name.setText(form.getClientName() +" "+form.getClientLastname());
        code.setText(form.getClientIdentifier());

        FacilityRepository repository = new FacilityRepository(context);


    }
}

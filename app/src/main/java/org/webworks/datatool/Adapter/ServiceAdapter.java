package org.webworks.datatool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.webworks.datatool.Model.ServicesNeeded;
import org.webworks.datatool.R;

import java.util.ArrayList;

public class ServiceAdapter extends BaseAdapter {
    Context context;
    private ArrayList<ServicesNeeded> services = new ArrayList<>();
    private ArrayList<ServicesNeeded> s = new ArrayList<>();
    public static ArrayList<String> selectedServices;


    public ServiceAdapter(Context _context, ArrayList<ServicesNeeded> _services, ArrayList<ServicesNeeded> selectedService) {
        context = _context;
        this.services = _services;
        this.s = selectedService;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public ServicesNeeded getItem(int i) {
        return services.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ServicesNeeded servicesNeeded = getItem(i);
        for (int j = 0; j < s.size(); j++) {
            if (servicesNeeded.getService().equals(s.get(j).getService())) {
                servicesNeeded.setChecked(true);
            }
        }
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.checkbox_spinner_adapter, null);
        final CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkBox);
        final TextView textView = (TextView)view.findViewById(R.id.select);
        final EditText othersSpecify = (EditText)view.findViewById(R.id.enter_others);
        othersSpecify.setVisibility(View.GONE);
        textView.setPadding(5, 5, 0, 5);
        textView.setTextSize(16);

        if(i == 0) {
            checkBox.setVisibility(View.INVISIBLE);
            checkBox.setChecked(false);
            textView.setText(servicesNeeded.getService());
        }
        else {
            textView.setVisibility(View.INVISIBLE);
            checkBox.setText(servicesNeeded.getService());
            checkBox.setId(i);
            checkBox.setTextColor(Color.BLACK);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        if(compoundButton.getText().toString().equalsIgnoreCase("Others(Specify)")) {
                            othersSpecify.setVisibility(View.VISIBLE);
                            compoundButton.setVisibility(View.INVISIBLE);
                            //Todo: get text with text change listener
                            selectedServices.add(othersSpecify.getText().toString().trim());
                        }
                        if (selectedServices.contains(servicesNeeded.getService())) {
                        }
                        else {
                            selectedServices.add(servicesNeeded.getService());
                        }
                    }
                    else {
                        selectedServices.remove(servicesNeeded.getService());
                    }
                    servicesNeeded.setChecked(b);
                    servicesNeeded.setId(checkBox.getId());
                }
            });
            if (servicesNeeded.isChecked()) {
                checkBox.setChecked(true);
            }
        }
        return view;
    }
}


package org.webworks.datatool.Model;

import org.json.JSONArray;
import org.webworks.datatool.Adapter.ServiceAdapter;
import org.webworks.datatool.Adapter.ServicesNeededAdapter;
import org.webworks.datatool.Fragment.ReferralFragment;

import java.util.ArrayList;
import java.util.HashSet;



public class ServicesNeeded {
    private int Id;
    private String service;
    private boolean checked;

    public ServicesNeeded(String _service){
        this.service = _service;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        service = service;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getSelectedServices() {
        ArrayList<String> list = ServicesNeededAdapter.selectedServices;
        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= list.size(); i++) {
            int n = i + 1;
            if (i < list.size()) {
                builder.append(n);
                builder.append("=>");
                builder.append(list.get(i));
                if (i < list.size() - 1) {
                    builder.append(", ");
                }
            }
        }
        return builder.toString();
    }

    public String getSelectedInnerServices() {
        ArrayList<String> list = ReferralFragment.selectedServices;
        if (list != null){
            HashSet<String> hashSet = new HashSet<>();
            hashSet.addAll(list);
            list.clear();
            list.addAll(hashSet);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i <= list.size(); i++) {
                int n = i + 1;
                if (i < list.size()) {
                    builder.append(n);
                    builder.append("=>");
                    builder.append(list.get(i));
                    if (i < list.size() - 1) {
                        builder.append(", ");
                    }
                }
            }
            return builder.toString();
        }
        return null;

    }

    public JSONArray getSelectedServicesJson() {
        ArrayList<String> list = ServicesNeededAdapter.selectedServices;
        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        return new JSONArray(list);
    }

    public String getInnerSelectedServices() {
        ArrayList<String> list = ServiceAdapter.selectedServices;
        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= list.size(); i++) {
            int n = i + 1;
            if (i < list.size()) {
                builder.append(n);
                builder.append("=>");
                builder.append(list.get(i));
                if (i < list.size() - 1) {
                    builder.append(", ");
                }
            }
        }
        return builder.toString();
    }
}

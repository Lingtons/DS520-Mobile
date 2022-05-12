package org.webworks.datatool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import org.webworks.datatool.Model.Facility;

import java.util.ArrayList;

public class FacilityAdapter extends BaseAdapter {

    private Context _context;
    private ArrayList<Facility> facilities;

    public FacilityAdapter(Context context, ArrayList<Facility> _facilities) {
        _context = context;
        this.facilities = _facilities;
    }
    @Override
    public int getCount() {
        return facilities.size();
    }

    @Override
    public Facility getItem(int i) {
        return facilities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView=new TextView(_context);
        textView.setText(getItem(i).getFacilityName());
        textView.setPadding(5, 5, 0, 5);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}

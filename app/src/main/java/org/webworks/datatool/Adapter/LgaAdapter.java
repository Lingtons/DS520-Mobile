package org.webworks.datatool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.webworks.datatool.Model.Lga;

import java.util.ArrayList;


public class LgaAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Lga> lgas;

    public LgaAdapter(Context _context, ArrayList<Lga> _lgas) {
        context = _context;
        this.lgas = _lgas;
    }

    @Override
    public int getCount() {
        return lgas.size();
    }

    @Override
    public Lga getItem(int i) {
        return lgas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(context);
        Lga lga = getItem(i);
        textView.setText(lga.getLga_name());
        textView.setPadding(5, 5, 0, 5);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}

package org.webworks.datatool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Johnbosco on 21/08/2017.
 */

public class UtilAdapter extends BaseAdapter {
    private Context _context;
    private String[] utility;

    public UtilAdapter(Context context, String[] _utility) {
        _context = context;
        this.utility = _utility;
    }

    @Override
    public int getCount() {
        return utility.length;
    }

    @Override
    public String getItem(int i) {
        return utility[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView=new TextView(_context);
        textView.setText(getItem(i));
        textView.setPadding(5, 5, 0, 5);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}

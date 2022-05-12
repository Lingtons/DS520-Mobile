package org.webworks.datatool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by UMB on 07/09/2017.
 */

public class ObjectAdapter extends BaseAdapter {

    private Context _context;
    private ArrayList<Object> _objectArrayList;

    public ObjectAdapter(Context context, ArrayList<Object> objectArrayList) {
        _context = context;
        this._objectArrayList = objectArrayList;
    }

    @Override
    public int getCount() {
        return _objectArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return _objectArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView=new TextView(_context);
        textView.setText(getItem(position).toString());
        textView.setPadding(5, 5, 0, 5);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}

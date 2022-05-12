package org.webworks.datatool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.webworks.datatool.Model.State;

import java.util.ArrayList;


public class StateAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<State> states;
    public StateAdapter(Context context, ArrayList<State> states){
        _context=context;
        this.states=states;
    }
    @Override
    public int getCount() {
        return states.size();
    }

    @Override
    public State getItem(int i) {
        return states.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView=new TextView(_context);
        State state=getItem(i);
        textView.setText(state.getState_name());
        textView.setPadding(5, 5, 0, 5);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}

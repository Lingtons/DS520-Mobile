package org.webworks.datatool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.webworks.datatool.Model.TestingPointParent;

import java.util.ArrayList;


public class TestingPointParentAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<TestingPointParent> testingPointParents;

    public TestingPointParentAdapter(Context context, ArrayList<TestingPointParent> testingPointParents){
        _context=context;
        this.testingPointParents=testingPointParents;
    }

    @Override
    public int getCount() {
        return testingPointParents.size();
    }

    @Override
    public TestingPointParent getItem(int i) {
        return testingPointParents.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(_context);
        TestingPointParent testingPointParent = getItem(i);
        textView.setText(testingPointParent.getParentName());
        textView.setPadding(5, 5, 0, 5);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}

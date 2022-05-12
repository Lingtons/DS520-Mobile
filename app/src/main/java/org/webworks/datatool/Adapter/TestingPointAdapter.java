package org.webworks.datatool.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.webworks.datatool.Model.TestingPoint;

import java.util.ArrayList;

public class TestingPointAdapter extends BaseAdapter {
    private Context _context;
    private ArrayList<TestingPoint> testingPoints;

    public TestingPointAdapter(Context context, ArrayList<TestingPoint> testingPoints){
        _context=context;
        this.testingPoints=testingPoints;
    }

    @Override
    public int getCount() {
        return testingPoints.size();
    }

    @Override
    public TestingPoint getItem(int i) {
        return testingPoints.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(_context);
        TestingPoint testingPoint = getItem(i);
        textView.setText(testingPoint.getTestingPointName());
        textView.setPadding(5, 5, 0, 5);
        textView.setTextSize(16);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}

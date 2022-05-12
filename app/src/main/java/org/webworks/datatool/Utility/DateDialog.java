package org.webworks.datatool.Utility;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Johnbosco on 05/08/2017.
 * This class handles all the date dialog
 * sets the date on the attached view when the user selects a date
 */

public class DateDialog implements View.OnFocusChangeListener {

    Context context;

    public DateDialog(Context _context) {
        context = _context;
    }

    @Override
    public void onFocusChange(final View v, boolean hasFocus) {
        if (hasFocus) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    int realmonth = month + 1;
                    String d = day + "/" + realmonth + "/" + year;
                    ((EditText) v).setText(d);
                }
            }, yy, mm, dd);
            datePicker.show();
        }
    }
}

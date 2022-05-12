package org.webworks.datatool.Utility;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import androidx.annotation.Nullable;

import com.satsuware.usefulviews.LabelledSpinner;



public class GroupButton implements CompoundButton.OnCheckedChangeListener {

    RadioButton other;
    LinearLayout parent;
    boolean visibility;
    EditText[] editTexts;
    LabelledSpinner[] labelledSpinners;

    public GroupButton(RadioButton _other){
        this.other = _other;
        this.parent = null;//set this to a new linear layout to avoid null exception when setting the visibility
    }

    public GroupButton(RadioButton _other, LinearLayout _parent, boolean _visibility){
        this.other = _other;
        this.parent = _parent;
        this.visibility = _visibility;
    }

    public GroupButton(RadioButton _other, LinearLayout _parent, boolean _visibility, @Nullable EditText[] _editTexts, @Nullable LabelledSpinner[] _labelledSpinners){
        this.other = _other;
        this.parent = _parent;
        this.visibility = _visibility;
        this.editTexts = _editTexts;
        this.labelledSpinners = _labelledSpinners;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        other.setChecked(!isChecked);
        if (visibility){
            parent.setVisibility(View.VISIBLE);
        }
        else {
            parent.setVisibility(View.GONE);
            try {
                for (EditText e : editTexts) {
                    e.setText("");
                }
                for (LabelledSpinner l : labelledSpinners) {
                    l.getSpinner().setSelection(0);
                }
            }
            catch (Exception e){}
        }
    }
}

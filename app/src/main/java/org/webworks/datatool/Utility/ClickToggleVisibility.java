package org.webworks.datatool.Utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TableLayout;

/**
 * Created by Johnbosco on 21/08/2017.
 */

public class ClickToggleVisibility implements View.OnClickListener {

    private TableLayout _tableLayout;
    private FrameLayout _iconUp;
    private FrameLayout _iconDown;
    private Context _context;

    public ClickToggleVisibility(Context context, TableLayout tableLayout) {
        _context = context;
        this._tableLayout = tableLayout;
        this._iconUp = null;
        this._iconDown = null;
    }

    public ClickToggleVisibility(Context context, TableLayout tableLayout, FrameLayout iconUp, FrameLayout iconDown) {
        _context = context;
        this._tableLayout = tableLayout;
        this._iconUp = iconUp;
        this._iconDown = iconDown;
    }

    @Override
    public void onClick(View v) {
        int visibility = _tableLayout.getVisibility();
        switch (visibility) {
            case View.GONE:
                _tableLayout.setVisibility(View.VISIBLE);
                _iconUp.setVisibility(View.VISIBLE);
                _iconDown.setVisibility(View.GONE);
                break;
            case View.VISIBLE:
                _tableLayout.setVisibility(View.GONE);
                _iconDown.setVisibility(View.VISIBLE);
                _iconUp.setVisibility(View.GONE);
                break;
            case View.INVISIBLE:
                _tableLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void animateOpenTable(final TableLayout layout) {
        layout.animate()
                .translationY(_tableLayout.getHeight())
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void animateCloseTable(final TableLayout layout) {
        TranslateAnimation animate = new TranslateAnimation(0,0,layout.getHeight(),0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layout.startAnimation(animate);
        /*layout.animate()
                .translationY(_tableLayout.getHeight())
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layout.setVisibility(View.GONE);
            }
        });*/
    }
}

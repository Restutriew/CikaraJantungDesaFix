package com.cikarastudio.cikarajantungdesafix.template.kima.textreadmore;

import static android.graphics.Color.parseColor;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class MySpannable extends ClickableSpan {

    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setUnderlineText(true);
        ds.setColor(parseColor("#9F9F9F"));
    }

    @Override
    public void onClick(View widget) {


    }
}
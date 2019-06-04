package com.learningmyway.me.workouttracker;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

public final class ViewConstructionHelper
{
    private ViewConstructionHelper() {};

    public static TextView makeTextView(Context context)
    {
        return makeTextView(context, "");
    }

    public static TextView makeTextView(Context context, String text)
    {
        return makeTextView(context, text, false);
    }

    public static TextView makeTextView(Context context, String text, boolean bold)
    {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTypeface(null, bold ? Typeface.BOLD : Typeface.NORMAL);
        tv.setPaddingRelative(15, 5, 15, 5);
        tv.setGravity(TEXT_ALIGNMENT_CENTER);

        return tv;
    }
}

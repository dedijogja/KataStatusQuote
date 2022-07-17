package com.sutinidevlab.help;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class KotakLayout extends RelativeLayout {

    public KotakLayout(Context context) {
        super(context);
    }

    public KotakLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KotakLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KotakLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        int ukuran = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(ukuran, ukuran);
    }

}

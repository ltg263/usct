package com.frico.usct.widget.nineimg;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.GridView;

public class NoScrollGridView extends GridView {

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public NoScrollGridView(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public NoScrollGridView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollGridView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

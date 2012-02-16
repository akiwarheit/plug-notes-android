package com.plug.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class PLUGEditText extends EditText {

    /** variables for our custom text editor with awesome underline churva */
  	private Rect mRect;
  	private Paint mPaint;
  
  	/* Custom EditText with line underneath the text */
  	public PLUGEditText(Context context, AttributeSet attrs) {
  	  super(context, attrs);
  	  
  	  mRect = new Rect();
  	  mPaint = new Paint();
  	  mPaint.setStyle(Paint.Style.STROKE);
  	  mPaint.setColor(Color.YELLOW);
  	}
  	
  	@Override
  	protected void onDraw(Canvas canvas) {
  	  int count = getLineCount();
  	  Rect r = mRect;
  	  Paint paint = mPaint;
  
  	  /* Drawing a line underneath the rows */
  	  for (int i = 0; i < count; i++) {
  			int baseline = getLineBounds(i, r);
  			canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
  	  }
  	  super.onDraw(canvas);
		}
  } 


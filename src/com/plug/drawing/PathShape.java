package com.plug.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.MotionEvent;

public class PathShape extends Shape {
  
  private int mX;
  private int mY;
  private Path mPath;
  
  public PathShape() {
    mPath = new Path();
  }
  
  @Override
  public String getName() {
    return "Curve";
  }
  
  @Override
  public void draw(Canvas canvas, Paint paint, Region region) {
    
    if (mPath.isEmpty()) {
      
      Rect rect = getRectWithDefaultMargins(region);
      
      int x = rect.left;
      int y = rect.bottom;
      
      int xPile = (rect.left + rect.right) / 6;
      int yPile = (rect.top + rect.bottom) / 6;
      
      mPath.moveTo(x, y);
      
      mX = x;
      mY = y;
      
      x = 2 * xPile;
      y = 4 * yPile;
      
      mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
      
      mX = x;
      mY = y;
      
      x = 3 * xPile;
      y = 4 * yPile;
      
      mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
      
      mX = x;
      mY = y;
      
      x = 4 * xPile;
      y = 2 * yPile;
      
      mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
      
      mX = x;
      mY = y;
      
      x = rect.right;
      y = rect.top;
      
      mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
      
      mX = x;
      mY = y;
      
      mPath.lineTo(mX, mY);
      
      draw(canvas, paint);
      reset();
    }
  }
  
  @Override
  public void draw(Canvas canvas, Paint paint) {
    canvas.drawPath(mPath, paint);
  }
  
  @Override
  public void onTouchEvent(int x, int y, int action) {
    
    switch (action) {
      case MotionEvent.ACTION_DOWN: {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        break;
      }
      case MotionEvent.ACTION_MOVE: {
        mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
        mX = x;
        mY = y;
        break;
      }
      case MotionEvent.ACTION_UP: {
        mPath.lineTo(mX, mY);
      }
    }
  }
  
  @Override
  public void reset() {
    mX = mY = 0;
    mPath.reset();
  }
}

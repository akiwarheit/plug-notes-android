package com.plug.actions;

import android.content.Context;

import com.plug.doodle.DrawingActivity;

public abstract class Action {
  public abstract void doAction(Context context,
      DrawingActivity.PaintView paintView);
}

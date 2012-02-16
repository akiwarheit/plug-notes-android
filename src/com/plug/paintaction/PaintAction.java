package com.plug.paintaction;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
//import com.plug.drawing.Shape;

public class PaintAction {

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint mPaint;
//    private Shape mShape;

    public PaintAction(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /*public PaintAction(Paint mPaint, Shape mShape, Canvas canvas) {
        this.canvas = canvas;
        this.mPaint = mPaint;
        this.mShape = mShape;
    }*/

    public PaintAction() {
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

//    public Shape getmShape() {
//        return mShape;
//    }
//
//    public void setmShape(Shape mShape) {
//        this.mShape = mShape;
//    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}

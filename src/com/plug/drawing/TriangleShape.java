//package com.plug.drawing;
//
//import android.graphics.*;
//
//public class TriangleShape extends RectShape {
//
//    public TriangleShape(){
//        super();
//    }
//
//    @Override
//    public String getName() {
//        return "Triangle";
//    }
//
//    @Override
//    public void draw(Canvas canvas, Paint paint) {
//
//        Path path = new Path();
//
//        int middle = (_rect.right + _rect.left) /2;
//
//        path.moveTo(middle, _rect.top);
//        path.lineTo(_rect.right, _rect.bottom);
//        path.lineTo(_rect.left, _rect.bottom);
//        path.lineTo(middle, _rect.top);
//        path.close();
//
//        canvas.drawPath(path, paint);
//    }
//}

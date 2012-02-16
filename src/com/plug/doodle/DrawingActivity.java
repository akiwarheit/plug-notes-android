package com.plug.doodle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Stack;

import keendy.projects.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.plug.actions.OnShakeAction;
import com.plug.colorpicker.ColorPickerDialog;
import com.plug.drawing.PathShape;
import com.plug.drawing.Shape;
import com.plug.image.handler.Image;
import com.plug.manager.ShakeManager;
import com.plug.paintaction.PaintAction;

public class DrawingActivity extends Activity
        implements ColorPickerDialog.OnColorChangedListener,
        ShakeManager.ShakeEventListener {

    private static final int HISTORY_CAPACITY = 20;
    private boolean saved = false;
    private PaintView _paintView;
    private ShakeManager _shakeManager;
    private Stack<PaintAction> _paintActions;
    private int _currentAction;
    private int initialColor = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _paintActions = new Stack<PaintAction>();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        mPaint.setColor(0xFFFF0000);

        initPaintViewLayout();
        registerForContextMenu(_paintView);

        _shakeManager = new ShakeManager(this);
        _shakeManager.addListener(this);

        Bitmap bitmap = _paintView.getDrawableBitmapCopy();
        _paintActions.push(new PaintAction(bitmap));
        _currentAction = _paintActions.size();
    }

    private void initPaintViewLayout() {
        setContentView(R.layout.doodle_area);
        LinearLayout layout = (LinearLayout) findViewById(R.id.paint_view_layout);
        _paintView = new PaintView(this);
        layout.addView(_paintView);
        
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.BLACK);
        mPaint.setMaskFilter(null);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);
 
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (_shakeManager != null) {
            _shakeManager.onPause();
        }
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.BLACK);
        mPaint.setMaskFilter(null);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _shakeManager.onResume();
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.BLACK);
        mPaint.setMaskFilter(null);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);
    }

    private Paint mPaint;

    public void colorChanged(int color) {
        mPaint.setColor(color);
  }

    @Override
    public void onShakeEvent() {
        new OnShakeAction().doAction(this, _paintView);
    }

    public class PaintView extends View {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Shape mShape;
        private Paint mBitmapPaint;

        public PaintView(Context c) {
            super(c);

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();

            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(0xFFFFFFFF);
            mShape = new PathShape();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }
     

        public void onClear() {
            mCanvas.drawColor(0xFFFFFFFF);
            invalidate();
            addActionToHistory();
        }

        private void addActionToHistory() {

            if (_paintActions.size() >= HISTORY_CAPACITY) {
                _paintActions.get(0).getBitmap().recycle();
                _paintActions.remove(0);
            }

            _paintActions.push(new PaintAction(Bitmap.createBitmap(mBitmap)));
            _currentAction = _paintActions.size();
        }

        public Bitmap getDrawableBitmapCopy() {
            return Bitmap.createBitmap(mBitmap);
        }

        public void setDrawableBitmap(Bitmap bitmap) {
            mCanvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
            invalidate();
            addActionToHistory();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFFFFFFF);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            mShape.draw(canvas, mPaint);
        }

        public void repaintAction(PaintAction paintAction) {
            mCanvas.drawBitmap(paintAction.getBitmap(), 0, 0, mBitmapPaint);
            invalidate();
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            int action = event.getAction();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {

                    mX = x;
                    mY = y;

                    mShape.onTouchEvent((int) x, (int) y, action);
                    invalidate();

                    break;
                }
                case MotionEvent.ACTION_MOVE: {

                    float dx = Math.abs(x - mX);
                    float dy = Math.abs(y - mY);

                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {

                        mX = x;
                        mY = y;

                        mShape.onTouchEvent((int) x, (int) y, action);
                    }
                    invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    mShape.onTouchEvent((int) x, (int) y, action);
                    mShape.draw(mCanvas, mPaint);
                    mShape.reset();
                    invalidate();

                    if (_currentAction < _paintActions.size()) {
                        for (int i = 0; i < _paintActions.size() - _currentAction + 1; i++) {
                            _paintActions.removeElementAt(_paintActions.size() - 1);
                        }
                    }
                    addActionToHistory();
                    break;
                }
            }
            return true;
        }
    }

    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();  
	    inflater.inflate(R.menu.drawing_menu, menu); 
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case R.id.doodle_openfile:
            	if (resultCode == RESULT_OK) {
            	  Bitmap bitmap = null;
      	          Uri selectedImageUri = data.getData();
      	          WindowManager win = getWindowManager();
      	          bitmap = Image.rescaleBitmap(getApplicationContext(), selectedImageUri, win);
      	          _paintView.setDrawableBitmap(bitmap);
            	}
            	break;
            
        }
    }
    
    @Override
	public void onBackPressed() {
		
		if(!isEmpty(_paintView)){
			new AlertDialog.Builder(this)
	        .setTitle( "Save Doodle" )
	        .setMessage( "Do you want to save changes?" )
	        .setPositiveButton("YES", new OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	            	saveDoodle(_paintView);
	            	saved = true;
	            	finish();	           
	            }
	        })
	        .setNegativeButton("NO", new OnClickListener() {
	            public void onClick(DialogInterface arg0, int arg1) {
	                finish();	             
	            }
	        }).show();
			}
			if(saved || isEmpty(_paintView)){
				this.finish();
				saved = false;
			}
			
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
          
            case R.id.doodle_clear: 
                _paintView.onClear();
                break;
            
            case R.id.doodle_openfile:
           	 Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), R.id.doodle_openfile);
                break;
                
            case R.id.doodle_undo:
            	this.undo();
            	break;
            	
            case R.id.doodle_redo:
            	this.redo();
            	break;
            	
            case R.id.doodle_grid:
            	 ColorPickerDialog dialog = new ColorPickerDialog(DrawingActivity.this,DrawingActivity.this,mPaint.getColor());
            	 dialog.show();
            	 break;

            case R.id.doodle_save:
            	saveDoodle(_paintView);
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDoodle(PaintView v) {
    	
    		v.setDrawingCacheEnabled(true);
    		v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    		v.layout(0, 0, v.getWidth(), v.getHeight());
    		v.buildDrawingCache(true);

    		Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
    		v.setDrawingCacheEnabled(false);
    		String filename = String.valueOf(System.currentTimeMillis());
    		
    		if (bm != null) {
    		    try {
    		        String path = Environment.getExternalStorageDirectory().toString();
    		        File imageFolder = new File(path+"/DODOL/");
    		        if(!imageFolder.exists())
    		        	imageFolder.mkdir();
    		        
    		        OutputStream fOut = null;
    		        File file = new File(imageFolder.getAbsolutePath(), filename+".jpg");
    		        fOut = new FileOutputStream(file);
    		        bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
    		        fOut.flush();
    		        fOut.close();
    		        Log.e("ImagePath", "Image Path : " + MediaStore.Images.Media.insertImage
    		        		( getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName()));
    		    }
    		    catch (Exception e) {
    		        e.printStackTrace();
    		    }
    		    
    		    Toast.makeText(this, "Saved successfully.", Toast.LENGTH_LONG).show();
    		}
		
	}
    private boolean isEmpty(View v){
		v.setDrawingCacheEnabled(true);
		v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.buildDrawingCache(true);

		Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
		v.setDrawingCacheEnabled(false);
		if(bm != null){
			
			return false;
		} else {
			
			return true;		
		}
	}

	private void redo() {
        if (_currentAction < _paintActions.size()) {
            _paintView.repaintAction(_paintActions.get(_currentAction));
            _currentAction++;
        }
    }

    private void undo() {
        if (_currentAction > 1) {
            _currentAction--;
            _paintView.repaintAction(_paintActions.get(_currentAction - 1));
        }
    }
}

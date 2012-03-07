package com.plug.note;

import keendy.projects.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.plug.utils.FileManager;

public class OCRTask extends AsyncTask<Void, Integer, String> {
  
  private ProgressDialog progress;
  private Context context;
  private Bitmap imageToProcess;
  private OCRCallback callback;
  private TessBaseAPI tesseract;
  
  public OCRTask(Context context, Bitmap bitmap, OCRCallback callback) {
    this.context = context;
    this.imageToProcess = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    progress = new ProgressDialog(context);
    this.callback = callback;
  }
  
  @Override
  protected void onPreExecute() {
    FileManager manager = new FileManager(context);
    manager.writeRawToSD(FileManager.TESSERACT_PATH + "eng.traineddata",
        "tessdata/eng.traineddata");
    this.progress.setIndeterminate(true);
    this.progress.setIndeterminateDrawable(context.getResources().getDrawable(
        R.anim.spinner_loading));
    this.progress.setMessage("Recognizing using Tesseract, bro.");
    this.progress.setProgress(0);
    this.progress.show();
    tesseract = new TessBaseAPI();
    tesseract.setDebug(true);
    tesseract.init(FileManager.STORAGE_PATH, "eng");
    tesseract.setImage(imageToProcess);
  }
  
  @Override
  protected String doInBackground(Void... params) {
    String result = tesseract.getUTF8Text();
    return result;
  }
  
  protected void onPostExecute(String finish) {
    if (progress.isShowing())
      progress.dismiss();
    
    callback.onFinishRecognition(finish);
    
    tesseract.end();
  }
  
}

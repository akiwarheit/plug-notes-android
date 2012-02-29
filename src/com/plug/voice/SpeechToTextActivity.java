//package com.plug.voice;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import keendy.projects.R;
//import roboguice.activity.RoboActivity;
//import roboguice.inject.InjectView;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.os.Bundle;
//import android.speech.RecognizerIntent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//
//public class SpeechToTextActivity extends RoboActivity implements OnClickListener{
//	
//	private static final int REQUEST_CODE = 1;
//	@InjectView(R.id.wordsList) private ListView wordsRecognized;
//	@InjectView(R.id.speak_button)private Button speakBttn;
//	
//	public void onCreate(Bundle bundle){
//		super.onCreate(bundle);
//		setContentView(R.layout.voice_record);
//		
//		speakBttn.setOnClickListener(this);
//		
//		PackageManager pm = getPackageManager();
//    List<ResolveInfo> activities = pm.queryIntentActivities(
//            new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
//    if (activities.size() == 0)
//    {
//        speakBttn.setEnabled(false);
//        speakBttn.setText("Recognizer not present");
//    }
//		
//		
//	}
//	
//	 @Override
//   protected void onActivityResult(int requestCode, int resultCode, Intent data)
//   {
//       if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
//       {
//          //recognition results
//           ArrayList<String> matches = data.getStringArrayListExtra(
//                   RecognizerIntent.EXTRA_RESULTS);
//           wordsRecognized.setAdapter(new ArrayAdapter<String>(this,
//          		 android.R.layout.simple_list_item_1,
//                   matches));
//       }
//       super.onActivityResult(requestCode, resultCode, data);
//   }
//
//	@Override
//	public void onClick(View arg0) {
//		//prompts a user for speech and sends it to a speech recognizer
//		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//    startActivityForResult(intent, REQUEST_CODE);
//		
//	}
//
//	
//	
//}

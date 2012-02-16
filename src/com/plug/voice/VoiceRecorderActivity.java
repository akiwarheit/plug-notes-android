package com.plug.voice;

import java.io.File;
import java.io.IOException;

import keendy.projects.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class VoiceRecorderActivity extends RoboActivity {
	
	@InjectView(R.id.start_record)  private Button startRecord;
    @InjectView(R.id.stop_record)  private Button stopRecord;
	public MediaRecorder mrec = null;
	private static final String TAG = "SoundRecordingDemo";
	File audiofile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_record_audio);

		mrec = new MediaRecorder();

		startRecord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startRecord.setEnabled(false);
					stopRecord.setEnabled(true);
					stopRecord.requestFocus();
					startRecord();
				} catch (Exception ee) {
					Log.e(TAG,"Caught io exception" + ee.getMessage());
				}
			}
		});

		stopRecord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startRecord.setEnabled(true);
				stopRecord.setEnabled(false);
				startRecord.requestFocus();
				stopRecord();
				processAudioFile();
			}
		});

		stopRecord.setEnabled(false);
		startRecord.setEnabled(true);

	}

	protected void processAudioFile() {
		ContentValues values = new ContentValues();
		long current = System.currentTimeMillis();

		values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
		values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
		values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
		ContentResolver contentResolver = getContentResolver();

		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);

		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	}

	protected void startRecord() throws IOException {
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
		mrec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		if (audiofile == null) {
			File sampleDir = Environment.getExternalStorageDirectory();

			try {
				audiofile = File.createTempFile("ibm", ".3gp", sampleDir);
			} catch (IOException e) {
				Log.e(TAG, "sdcard access error");
				return;
			}
		}

		mrec.setOutputFile(audiofile.getAbsolutePath());

		mrec.prepare();
		mrec.start();

	}

	protected void stopRecord() {
		mrec.stop();
		mrec.release();
	}
}

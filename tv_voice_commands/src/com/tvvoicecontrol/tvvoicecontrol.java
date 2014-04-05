package com.tvvoicecontrol;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


public class tvvoicecontrol extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	private ListView mList;
	private String url = "http://192.168.1.43/";  //url for webserver


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button speakButton = (Button)findViewById(R.id.speech_button);
        Button volumeUpButton = (Button)findViewById(R.id.volume_up_button);
        volumeUpButton.setOnClickListener(this);
        Button volumeDownButton = (Button)findViewById(R.id.volume_down_button);
        volumeDownButton.setOnClickListener(this);
        Button inputButton = (Button)findViewById(R.id.input_button);
        inputButton.setOnClickListener(this);
        Button powerOnButton = (Button)findViewById(R.id.power_on_button);
        powerOnButton.setOnClickListener(this);
        Button powerOffButton = (Button)findViewById(R.id.power_off_button);
        powerOffButton.setOnClickListener(this);
        Button muteButton = (Button)findViewById(R.id.mute_button);
        muteButton.setOnClickListener(this);
        
        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(
          new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
          speakButton.setOnClickListener(this);
        } else {
          speakButton.setEnabled(false);
          speakButton.setText("Recognizer not present");
        }

    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

        if (v.getId() == R.id.speech_button) {
            startVoiceRecognitionActivity();
        }
        if (v.getId() == R.id.volume_up_button) {
        	execute_get_request("volume up");
        }
        if (v.getId() == R.id.volume_down_button) {
            execute_get_request("volume down");
        }
        if (v.getId() == R.id.input_button){
        	choose_input();
        }
        if (v.getId() == R.id.power_on_button){
        	execute_get_request("power on");
        }
        if (v.getId() == R.id.power_off_button){
        	execute_get_request("power off");
        }
        if (v.getId() == R.id.mute_button){
        	execute_get_request("mute");
        }
	}
	
	
	private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, getClass().getPackage().getName());

        // Display an hint to the user about what he should say.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");

        // Given an hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Specify how many results you want to receive. The results will be sorted
        // where the first result is the one with higher confidence.
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }
	
	//voice recognition result processor
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            TextView tv = (TextView)findViewById(R.id.textview);
            tv.setText(matches.get(0));
            execute_get_request(matches.get(0));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    //send the command to the webserver
    private void execute_get_request(String url_extension){
    	Log.d(url_extension,url_extension);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String query = url + "oops";
		try {
			query = url + URLEncoder.encode(url_extension, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        HttpGet httpGet = new HttpGet(query);
        try {
			httpClient.execute(httpGet, localContext);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //select the input--HDMI, Cable, etc.
    public void choose_input(){
    	//create the dialog to select a note
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose a note");
		final String[] input_choices = {"Cable", "Computer", "AV1", "AV2", "HDMI1"};
		builder.setItems(input_choices, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	//fetch the item w/ id of ids[item]
		    	execute_get_request("input " + input_choices[item]);
		    	//Log.d("response", "input " + input_choices[item]);
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
    }
}
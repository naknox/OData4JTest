package com.example.odata4jtest1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/*
 * MainActivity should start the application.
 */

//A client is any program which initiates requests for such services
//A server is a provider of a resource or service (sometimes a dedicated computer, sometimes just a program)

public class MainActivity extends Activity implements OnClickListener {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tv = (TextView)findViewById(R.id.movieNameFeed);
		
		
		LoadTask task = new LoadTask(tv, this); //create a new LoadTask with our pre-existing textview
		AsyncTask<String, Void, String> async = task.execute((String[]) null); 
		//AsyncTask enables proper and easy use of the UI thread.  This class allows to perform background 
		//operations and publish results on the UI thread without having to manipulate threads and/or handlers.
		//AsyncTasks should ideally be used for short operations (a few seconds at the most.)
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
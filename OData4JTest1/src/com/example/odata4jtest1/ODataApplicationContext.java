package com.example.odata4jtest1;

import android.app.Application;

public class ODataApplicationContext extends Application {

	private LocalDataHelper _localDataHelper;
	@Override
	public void onCreate()
	{
		super.onCreate();

		// Initialize the singletons so their instances
		// are bound to the application process.
		initSingletons();
		
		
	}

	protected void initSingletons()
	{
		_localDataHelper = new LocalDataHelper(this);
	}

	public LocalDataHelper getLocalDataHelper(){
		return _localDataHelper;
	}
}

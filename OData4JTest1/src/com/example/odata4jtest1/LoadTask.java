package com.example.odata4jtest1;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

/*
 * This is LoadTask. It is used to make sure that the loading from the outside DB to the SQLite Database takes place 
 * in the background thread of the application. (This prevents the UI from freezing.)
 */

class LoadTask extends AsyncTask<String, Void, String>{

	TextView tv;
	Context context;

	LoadTask(TextView tv, Context context){
		this.tv = tv; //get the textview to edit it later
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		Uri contentURI = Uri.parse("content://com.example.test1.provider/Movies"); //this creates a new Uri from a properly formatted string.
		Cursor c = this.context.getContentResolver().query(contentURI, null, null, null, null); //an application accesses the data in a ContentProvider
		//using a ContentResolver. This gets the info from the CP from the ContentResolver using query (with no filters), and then returns
		//a Cursor that can then be used to navigate said info.
		
		StringBuilder sb = new StringBuilder();
		while(c.moveToNext()){ //while the cursor has rows !!
			for(int colNdx =0; colNdx < c.getColumnCount(); colNdx++){ //for all columns
				sb.append(c.getColumnName(colNdx)); //get the name
				sb.append(" => ");
				sb.append(c.getString(colNdx)); //get the thing in the name 
				
				//(what if it is not a string?) unexpected behavior! it probably just calls 'to string'
				//there are other gets for other types of primitive types
				
				if(colNdx + 1 != c.getColumnCount()){
					sb.append(", ");
				}
			}
			
			sb.append("\n");
		}
		
		c.close(); //close the Cursor to free up resources
		
		return sb.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		this.tv.setText(result); //update the MainApplication's textview
	}
}
package com.example.store_luong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static String TAG = "Store";
	EditText etResponse;
	TextView tvIsConnected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Change

		// get reference to the views
		etResponse = (EditText) findViewById(R.id.etResponse);
		tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

		// check if you are connected or not
		if (isConnected()) {
			tvIsConnected.setBackgroundColor(0xFF00CC00);
			tvIsConnected.setText("You are conncted");
		} else {
			tvIsConnected.setText("You are NOT conncted");
		}

		// call AsynTask to perform network operation on separate thread
		String url = "https://raw.githubusercontent.com/luongbeta2/Store_Luong/master/assets/extend_app/app_english_test.txt";
		new HttpAsyncTask().execute(url);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static String GET(String urlStr) {
		String result = "";
		try {

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// read the response
			InputStream is = conn.getInputStream();
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			StringBuilder stringBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null) {
				stringBuilder.append(inputStr);
			}
			
			Log.e(TAG, stringBuilder.toString());
			
			// Create a JSON object that we can use from the String
			JSONArray jsonArray = new JSONArray(stringBuilder.toString());
			Log.e(TAG, "jsonArray size: " + jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				int stt = jsonObject.getInt("stt");
				String name = jsonObject.getString("name");
				String description = jsonObject.getString("description");
				String package_name = jsonObject.getString("package_name");
				String image_url = jsonObject.getString("image_url");
				
				
				Log.e(TAG, "stt: " + stt);
				Log.e(TAG, "name: " + name);
				Log.e(TAG, "description: " + description);
				Log.e(TAG, "package_name: " + package_name);
				Log.e(TAG, "image_url: " + image_url);
				Log.e(TAG, "------\n\n");
			}

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
			etResponse.setText(result);
		}
	}

}

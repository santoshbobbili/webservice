package com.example.wp.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.wp.Activity.WpMainView;
import com.example.wp.Data.WpPOJO;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class WpIntentService extends IntentService {

	public static List<String[]> responseDataArray;

	private static String url = "http://www.washingtonpost.com/wp-srv/simulation/simulation_test.json";

	public WpIntentService(String name) {
		super(name);
	}

	public WpIntentService() {
		super(" ");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			findAllItems();
			Intent dataFetchedIndicator = new Intent("DATA_FETCHED_INDICATOR");
			sendBroadcast(dataFetchedIndicator);
		}
	}

	public static JSONObject requestWebService(String serviceUrl) {

		HttpURLConnection urlConnection = null;
		try {
			// create connection
			URL urlToRequest = new URL(serviceUrl);
			urlConnection = (HttpURLConnection) urlToRequest.openConnection();
			urlConnection.setConnectTimeout(10000);
			urlConnection.setReadTimeout(10000);

			// handle issues
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				// handle unauthorized (if service requires user login)
			} else if (statusCode != HttpURLConnection.HTTP_OK) {
				// handle any other errors, like 404, 500,..
			}

			// create JSON object from content
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			return new JSONObject(getResponseText(in));

		} catch (MalformedURLException e) {
			// URL is invalid
		} catch (SocketTimeoutException e) {
			// data retrieval or connection timed out
		} catch (IOException e) {
			// could not read response body
			// (could not create input stream)
		} catch (JSONException e) {
			// response body is no valid JSON string
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		return null;
	}

	private static String getResponseText(InputStream inStream) {
		return new Scanner(inStream).useDelimiter("\\A").next();
	}

	public void findAllItems() {
		JSONObject serviceResult = requestWebService(url);
		try {
			JSONArray items = serviceResult.getJSONArray("posts");

			for (int i = 0; i < items.length(); i++) {
				JSONObject obj = items.getJSONObject(i);
				WpMainView.rowItems.add(new WpPOJO(obj.getString("title"), obj.getString("content"), obj.getString("id"),obj.getString("date")));
			}

		} catch (JSONException e) {
			e.printStackTrace();
			// handle exception
		}

	}
}

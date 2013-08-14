package com.example.yahoodealsapp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.Menu;
import android.view.MenuInflater;

import android.view.View;

import android.view.Window;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class YahooDealsMain extends Activity {
	// All static variables
	static final String URL = "http://api.androidhive.info/music/music.xml";
	// XML node keys
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_THUMB_URL = "thumb_url";
	ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	ListView list;
    YahooDealsAdapter adapter;
    static View stationsContainer;
    static View stationsContainer_rows;

    
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.yahoo_deals_main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.yahoo_deals_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the search-able activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setQueryHint("Search Deals");
	    //searchView.setSubmitButtonEnabled(true);
	    return super.onCreateOptionsMenu(menu);
    }
    
    double LATITUDE = 41.869179;
    double LONGITUDE = -87.663452;
    
    
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#10F0F0F0")));
		//actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
		setContentView(R.layout.activity_yahoo_deals_main);
		
		/* The following is for the network connectivity Error
		 */ 
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}

		/* ********************************************************/

		
		  /******** GETTING THE GEO LOCATION AND PERFORM ADDRESS SEARCH ****/
		
		
		TextView myAddress = (TextView) findViewById(R.id.myAddress);
		
			
		
		Geocoder geocoder = new Geocoder(getBaseContext(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,
					LONGITUDE, 1);

            if (addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder(
                        "Address:\n");
                for (int i = 0; i < returnedAddress
                        .getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(
                            returnedAddress.getAddressLine(i)).append("\n");
                }
                myAddress.setText(strReturnedAddress.toString());
            } else {
                myAddress.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
		/*******************GEO LOCATION SEARCH ENDS**************/
		
		

		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromUrl(URL); // getting XML from URL
		Document doc = parser.getDomElement(xml); // getting DOM element
		
		NodeList nl = doc.getElementsByTagName(KEY_SONG);
		// looping through all the nodes from XML to create an HashMap
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			// adding each child node to HashMap key => value
			map.put(KEY_ID, parser.getValue(e, KEY_ID));
			map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
			map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
			map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
			map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

			// adding HashList to ArrayList
			songsList.add(map);
		}
		

		list=(ListView)findViewById(R.id.list);
		
		// Getting adapter by passing xml data ArrayList
        adapter=new YahooDealsAdapter(this, songsList);        
        list.setAdapter(adapter);
        
        

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//FETCHES THE DATA...
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.artist)).getText().toString();
				String cost = ((TextView) view.findViewById(R.id.title)).getText().toString();
				String description = ((TextView) view.findViewById(R.id.duration)).getText().toString();
				String url = ((TextView) view.findViewById(R.id.url)).getText().toString();	
				ImageView img=(ImageView)view.findViewById(R.id.list_image);			
				
				//CREATES AND INTENT TO SEND TO THE SELECTED DEALS ACTIVITY..
				// Starting new intent
				Intent in = new Intent(getApplicationContext(), SelectedDeal.class);
				in.putExtra(KEY_ARTIST, name);
				in.putExtra(KEY_TITLE, cost);
				in.putExtra(KEY_DURATION, description);
				in.putExtra(KEY_THUMB_URL, url);
				img.setDrawingCacheEnabled(true);
				Bitmap b = img.getDrawingCache();
				/*ByteArrayOutputStream bs = new ByteArrayOutputStream();
				b.compress(Bitmap.CompressFormat.PNG, 50, bs);*/
				in.putExtra("selectedImage", b);
				
				//UPDATE THE DATA ON CLICK TRIAL ! ! COMMENTED ... NOW ! !
				
				/*
				songsList.clear();
				XMLParser parser = new XMLParser();
				String xml = parser.getXmlFromUrl(URL); // getting XML from URL
				Document doc = parser.getDomElement(xml); // getting DOM element
				
				NodeList nl = doc.getElementsByTagName(KEY_SONG);
				// looping through all the nodes from XML to create an HashMap
				for (int i = 0; i < nl.getLength(); i++) {
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					Element e = (Element) nl.item(i);
					// adding each child node to HashMap key => value
					map.put(KEY_ID, parser.getValue(e, KEY_ID));
					map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
					map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
					map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
					map.put(KEY_THUMB_URL, "http://api.androidhive.info/music/images/adele.png");

					// adding HashList to ArrayList
					songsList.add(map);
				}
				
	
				list=(ListView)findViewById(R.id.list);
				adapter.notifyDataSetInvalidated();
				//adapter.notifyDataSetChanged();
				((BaseAdapter)list.getAdapter()).notifyDataSetChanged(); 
		       	*/
				startActivity(in);
				
			}		

		});		
}
}
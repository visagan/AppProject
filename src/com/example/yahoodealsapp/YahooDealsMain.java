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
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
//import android.support.v4.widget.SearchViewCompatIcs.MySearchView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
public class YahooDealsMain extends Activity implements SearchView.OnQueryTextListener {
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
    TextView myAddress;

    
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.yahoo_deals_main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/
    
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            
            myAddress.setText(query);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        
        handleIntent(intent);
    }

    SearchView searchView;
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void setupSearchView(MenuItem searchItem) {
    	 
        if (isAlwaysExpanded()) {
            searchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }
 
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
 
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            searchView.setSearchableInfo(info);
        }
 
        searchView.setOnQueryTextListener(this);
    }
 
    public boolean onQueryTextChange(String newText) {
        myAddress.setText("Query = " + newText);
        return false;
    }
 
    public boolean onQueryTextSubmit(String query) {
    	myAddress.setText("Query = " + query + " : submitted");
        return false;
    }
 
    public boolean onClose() {
    	myAddress.setText("Closed!");
        return false;
    }
 
    protected boolean isAlwaysExpanded() {
        return false;
    }

    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.yahoo_deals_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the search-able activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setQueryHint("Search Deals");
	    setupSearchView(searchItem);
	    //searchView.setSubmitButtonEnabled(true);
	    return true;
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

		handleIntent(getIntent());
		
		
		  /******** GETTING THE GEO LOCATION AND PERFORM ADDRESS SEARCH ****/
		
		myAddress = (TextView) findViewById(R.id.myAddress);
		LocationManager currentLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = currentLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// Check if enabled and if not send user to the GSP settings
				// Better solution would be to display a dialog and suggesting to 
				// go to the settings
				if (!enabled) {
				  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				  startActivity(intent);
				} 
		
		Location location = currentLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);	
		
		if(location != null){
			Geocoder geocoder = new Geocoder(getBaseContext(), Locale.ENGLISH);
	        try {
	            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1);

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
		}
		String Address;
		final LocationListener locationListener = new LocationListener() {
	        public void onLocationChanged(Location location) {
	            LONGITUDE = location.getLongitude();
	            LATITUDE = location.getLatitude();
	            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.ENGLISH);
		        try {
		            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
							location.getLongitude(), 1);

		            if (addresses.size() > 0) {
		                Address returnedAddress = addresses.get(0);
		                StringBuilder strReturnedAddress = new StringBuilder(
		                        "Address:\n");
		                for (int i = 0; i < returnedAddress
		                        .getMaxAddressLineIndex(); i++) {
		                    strReturnedAddress.append(
		                            returnedAddress.getAddressLine(i)).append("\n");
		                }
		                setAddress(strReturnedAddress.toString());
		            } else {
		            	setAddress("No Address returned!");
		            }
		        } catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();

		        }
	        }

	        public void onProviderDisabled(String arg0) {
	            // TODO Auto-generated method stub

	        }

	        public void onProviderEnabled(String arg0) {
	            // TODO Auto-generated method stub

	        }

	        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	            // TODO Auto-generated method stub

	        }
	    };

	    currentLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
	    
	    
	    
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
	
	public void setAddress(String address){
		myAddress.setText(address);
	}
}
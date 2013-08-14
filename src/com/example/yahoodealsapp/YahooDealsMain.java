package com.example.yahoodealsapp;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class YahooDealsMain extends Activity implements OnItemClickListener {
	
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	// All static variables
	static final String URL = "http://api.androidhive.info/music/music.xml";
	// XML node keys
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_THUMB_URL = "thumb_url";
	
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
    
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33F0F0F0")));
		//actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
		setContentView(R.layout.activity_yahoo_deals_main);
		/* The following is for the network connectivity Error
		 */ 
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}

		/* ********************************************************/

		
		  
		
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromUrl(URL); // getting XML from URL
		Document doc = parser.getDomElement(xml); // getting DOM element
		
		NodeList nl = doc.getElementsByTagName(KEY_SONG);
		// looping through all song nodes <song>
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
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.artist)).getText().toString();
				String cost = ((TextView) view.findViewById(R.id.title)).getText().toString();
				String description = ((TextView) view.findViewById(R.id.duration)).getText().toString();
				String url = ((TextView) view.findViewById(R.id.url)).getText().toString();
				
				ImageView img=(ImageView)view.findViewById(R.id.list_image);
				
				
				
				
				/*//ANIMATION FOR SLIDE OUT AND SLIDE IN
				stationsContainer = findViewById(R.id.yahooMain);
				//for(int i=0;i<list.getChildCount();i++){
					
					final ViewGroup row = (ViewGroup)list.getChildAt(position); 
				    stationsContainer_rows = row.findViewById(R.id.row_deals);
				    Animation an = AnimationUtils.loadAnimation(row.getContext(), R.anim.slide_out_right);
				    an.setDuration(500);
				    //stationsContainer_rows.animate().rotationYBy(720);
				    
				    stationsContainer_rows.startAnimation(an);
				    an.setAnimationListener(new AnimationListener() {

				        public void onAnimationStart(Animation animation) {
				            // TODO Auto-generated method stub

				        }

				        public void onAnimationRepeat(Animation animation) {
				            // TODO Auto-generated method stub

				        }

				        public void onAnimationEnd(Animation animation) {
				        	//stationsContainer.setVisibility(View.GONE);
				        }
				    });
				//}
*/				
				    
				  //ANIMATION ENDS  
				    
				
				
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
				startActivity(in);
				
				/*//ANIMATION FOR SELECTED DEAL TO SLIDE IN..
				YahooDealsMain.this.overridePendingTransition(
						R.anim.slide_in_right,
						R.anim.slide_out_left
    				);*/
				
			}		

		});		
	
       
        
	/*gestureDetector = new GestureDetector(new MyGestureDetector());
    View mainview = (View) findViewById(R.id.list);
    
    // Set the touch listener for the main view to be our custom gesture listener
    mainview.setOnTouchListener(new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }
            return false;
        }
    });*/
}
	
	
	
	 class MyGestureDetector extends SimpleOnGestureListener {
	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		        	Intent intent = new Intent(YahooDealsMain.this.getBaseContext(), SelectedDeal.class);
	        	
	            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
	                return false;
	            }
	            
	            // right to left swipe
	            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {	
	            	startActivity(intent);
	    				
	    				YahooDealsMain.this.overridePendingTransition(
							R.anim.slide_in_right,
							R.anim.slide_out_left
	    				);
	    			// right to left swipe
	            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	    				startActivity(intent);
	    				YahooDealsMain.this.overridePendingTransition(
							R.anim.slide_in_left, 
							R.anim.slide_out_right
	    				);
	            }

	            return false;
	        }
	        
	        // It is necessary to return true from onDown for the onFling event to register
	        @Override
	        public boolean onDown(MotionEvent e) {
		        	return true;
	        }
	    }



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
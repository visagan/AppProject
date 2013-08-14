package com.example.yahoodealsapp;





import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

public class SelectedDeal extends Activity {
	// XML node keys
		static final String KEY_SONG = "song"; // parent node
		static final String KEY_ID = "id";
		static final String KEY_TITLE = "title";
		static final String KEY_ARTIST = "artist";
		static final String KEY_DURATION = "duration";
		static final String KEY_THUMB_URL = "thumb_url";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_deal);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get XML values from previous intent
        String name = in.getStringExtra(KEY_TITLE);
        String cost = in.getStringExtra(KEY_ARTIST);
        String description = in.getStringExtra(KEY_DURATION);
        String url = in.getStringExtra(KEY_THUMB_URL);
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.artist);
        TextView lblCost = (TextView) findViewById(R.id.title);
        TextView lblDesc = (TextView) findViewById(R.id.duration);
       
        
        //Bitmap bitmap = (Bitmap)this.getIntent().getParcelableExtra("selectedImage");  
        ImageView image = (ImageView)findViewById(R.id.list_image_selected);
        //image.setImageBitmap(bitmap);
        lblName.setText(name);
        lblCost.setText(cost);
        lblDesc.setText(description);
        
        Bitmap bitmap=ImageLoader.memoryCache.get(url);
        if(bitmap!=null)
            image.setImageBitmap(bitmap);
        
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selected_deal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
	  SelectedDeal.this.finish();
	  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	  YahooDealsMain.stationsContainer.setVisibility(View.VISIBLE);
	}
}

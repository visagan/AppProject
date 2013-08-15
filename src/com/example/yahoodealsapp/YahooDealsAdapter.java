package com.example.yahoodealsapp;

import java.util.ArrayList;
import java.util.HashMap;




import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YahooDealsAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public YahooDealsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        //Gets the View from the layout...
        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        TextView url = (TextView)vi.findViewById(R.id.url);
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        //Decorates the views from the values put in the intent..
        // Setting all values in listview
        title.setText(song.get(Constants.KEY_DEAL));
        artist.setText(song.get(Constants.KEY_DEALER));
        duration.setText(song.get(Constants.KEY_DURATION));
        imageLoader.DisplayImage(song.get(Constants.KEY_THUMB_URL), thumb_image);
        url.setText(song.get(Constants.KEY_THUMB_URL));
        url.setVisibility(View.INVISIBLE);
        return vi;
    }
}
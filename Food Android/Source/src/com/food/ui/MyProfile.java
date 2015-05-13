package com.food.ui;

import java.io.IOException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.food.R;
import com.food.custom.CustomFragment;
import com.food.utils.JSONParser;

/**
 * The Class MyFriendsList is the Fragment class that is launched when the user
 * clicks on My Friends option in Left navigation drawer. It simply display a dummy list of Friends. You
 * need to write actual implementation for loading and displaying Friends.
 */
public class MyProfile extends CustomFragment
{

	public String username, profile_name;
	TextView name;
	public Bitmap myBitmap;
	JSONObject user = new JSONObject();
	ImageView image1;
	public MyProfile(String username){
		this.username = username;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.my_profile, null);
		initializeUIElements(v);
		loadProfile();
		
		return v;
	}
	
	private void initializeUIElements(View v) {
		
		image1 = (ImageView) v.findViewById(R.id.img1);
		name = (TextView) v.findViewById(R.id.name);
	}
	private void loadProfile()
	{
			
		new AsyncTask<String, String, JSONObject>(){
			
		    ProgressDialog loadingBar;
		     @Override
            protected void onPreExecute(){
            	loadingBar = ProgressDialog.show(getActivity(), "", "Loading..", true);
			}
			 
			 @Override
			    protected JSONObject doInBackground(String... args) {
				    JSONParser jParser = new JSONParser();
				    // Getting JSON from URL
			        JSONObject json = jParser.getJSONFromUrl("http://indiainme.com/api_getUser.php?username="+username);
			          try {
			        	     JSONArray  dishArray = json.getJSONArray("user");
			        	     user = dishArray.getJSONObject(0);
			        	     profile_name = user.getString("name");
			        	     
			        	     String img_src1 = "http://www.indiainme.com/" + user.getString("profile_image") + "." + user.getString("ext");
							 URL url = new URL(img_src1);
						     myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
						     myBitmap = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
			            } catch (JSONException e) {
			                e.printStackTrace();
			            } catch (IOException e) {
					        e.printStackTrace();
					        Log.e("Exception",e.getMessage());
					        return null;
					    }
			        return user;
			    }
			 
			 @Override
		        protected void onPostExecute(JSONObject dish) {
				   setHasOptionsMenu(true);
				   image1.setImageBitmap(myBitmap); 
				   name.setText(profile_name);
				   loadingBar.dismiss();
				}
		}.execute();
		
	}
}

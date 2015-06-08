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
public class PostDetail extends CustomFragment
{

	public String profile_name;
	int postId;
	TextView name;
	public Bitmap myBitmap;
	JSONObject post = new JSONObject();
	JSONObject user = new JSONObject();
	ImageView image1;
	
	public PostDetail(int postId){
		this.postId = postId;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.post_detail, null);
		initializeUIElements(v);
		loadProfile();
		
		return v;
	}
	
	private void initializeUIElements(View v) {
		
		image1 = (ImageView) v.findViewById(R.id.user_profile);
		name = (TextView) v.findViewById(R.id.userName);
		
	}
	private void loadProfile()
	{
			
		new AsyncTask<String, String, JSONObject>(){
			
		    ProgressDialog loadingBar;
		     @Override
            protected void onPreExecute(){
            	loadingBar = ProgressDialog.show(getActivity(), "", "Loading Post..", true);
			}
			 
			 @Override
			protected JSONObject doInBackground(String... args) {
				    JSONParser jParser = new JSONParser();
				    System.out.println("postID is:" + postId);
				    // Getting JSON from URL
			        JSONObject json = jParser.getJSONFromUrl("http://indiainme.com/api_getPost.php?postId="+postId);
			          try {
			        	     JSONArray  postArray = json.getJSONArray("post");
			        	     post = postArray.getJSONObject(0);
			        	     String username = post.getString("username");
			        	     
			        	     JSONObject user_json = jParser.getJSONFromUrl("http://www.indiainme.com/api_getUser.php?username="+username);
		                     JSONArray  userArray = user_json.getJSONArray("user");
					         user = userArray.getJSONObject(0);
					         String img_src1 = "http://www.indiainme.com/img/profile_image/" + user.getString("id_user") + "." + user.getString("ext");
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
			        return post;
			    }
			 
			 @Override
		    protected void onPostExecute(JSONObject dish) {
				 try {
						
					 setHasOptionsMenu(true);
					 image1.setImageBitmap(myBitmap); 
					 name.setText(post.getString("name"));
				 } catch (JSONException e) {
					   // TODO Auto-generated catch block
					   e.printStackTrace();
				   }
					 
					 loadingBar.dismiss();
				}
		}.execute();
		
	}
	
	
}


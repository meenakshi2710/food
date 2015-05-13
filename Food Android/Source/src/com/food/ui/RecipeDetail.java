package com.food.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.food.R;
import com.food.custom.CustomFragment;
import com.food.utils.JSONParser;

/**
 * The Class RecipeDetail is the Fragment class that is launched when the user
 * select a recipe from Recipe List and it simply shows dummy recipe detail text
 * and images. You can customize this to display actual images and text.
 */
public class RecipeDetail extends CustomFragment
{
    private String dishId;
    TextView rDishName, rUserName, rDescription;
    Button rDesc, rProc, rIngr; 
    LinearLayout recipe_desc;
    ImageView image1, image2, image3, image4, image5, image6;
    String img_src1 = null, img_src2 = null, img_src3 = null, img_src4 = null, img_src5 = null, img_src6 = null; 
    JSONObject dish = new JSONObject();
	
    public RecipeDetail(String dishId){
		this.dishId = dishId;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.recipe_detail, null);
		
		initializeUIElements(v);
		loadDescription();
		return v;
	}

	private void initializeUIElements(View v) {
		
		rDishName = (TextView) v.findViewById(R.id.dishName);
		rUserName = (TextView) v.findViewById(R.id.userName);
		rDescription = (TextView) v.findViewById(R.id.description);
		
		recipe_desc = (LinearLayout) v.findViewById(R.id.layout_desc);
		
		rDesc = (Button) v.findViewById(R.id.desc);
		rDesc.setOnClickListener(this);
		
		rProc = (Button) v.findViewById(R.id.proc);
		rProc.setOnClickListener(this);
		
		rIngr = (Button) v.findViewById(R.id.ingr);
		rIngr.setOnClickListener(this);
		
		image1 = (ImageView) v.findViewById(R.id.image1);
		image2 = (ImageView) v.findViewById(R.id.image2);
		image3 = (ImageView) v.findViewById(R.id.image3);
		image4 = (ImageView) v.findViewById(R.id.image4);
		image5 = (ImageView) v.findViewById(R.id.image5);
		image6 = (ImageView) v.findViewById(R.id.image6);
		
		rDesc.setEnabled(false);
		rDesc.setTextColor(getResources().getColor(R.color.gray_light));
	}
	
	public void onClick(View v) {
	   if (v == rDesc) {
			rDesc.setEnabled(false);
			rDesc.setTextColor(getResources().getColor(R.color.gray_light));
			rProc.setEnabled(true);
			rIngr.setEnabled(true);
			rProc.setTextColor(getResources().getColor(R.color.black));
			rIngr.setTextColor(getResources().getColor(R.color.black));
			try {
				reloadDescription();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (v == rProc) {
        	rDesc.setEnabled(true);
        	rDesc.setTextColor(getResources().getColor(R.color.black));
        	rProc.setEnabled(false);
        	rProc.setTextColor(getResources().getColor(R.color.gray_light));
        	rIngr.setEnabled(true);
        	rIngr.setTextColor(getResources().getColor(R.color.black));
        	try {
				loadRecipe();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
        } else{
        	rDesc.setEnabled(true);
        	rDesc.setTextColor(getResources().getColor(R.color.black));
        	rProc.setEnabled(true);
        	rProc.setTextColor(getResources().getColor(R.color.black));
        	rIngr.setEnabled(false);
        	rIngr.setTextColor(getResources().getColor(R.color.gray_light));
        	try {
				loadIngredients();
			} catch (JSONException e) {
				rDescription.setText("This information is not available.");
		    }
        }
    }
	
	private void loadDescription()
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
			        JSONObject json = jParser.getJSONFromUrl("http://indiainme.com/api_getDish.php?dishId="+dishId);
			          try {
			        	     JSONArray  dishArray = json.getJSONArray("dish");
			        	     dish = dishArray.getJSONObject(0);
			                    
			            } catch (JSONException e) {
			                e.printStackTrace();
			            }
			        return dish;
			    }
			 
			 @Override
		        protected void onPostExecute(JSONObject dish) {
				   setHasOptionsMenu(true);
				   try {
					rDishName.setText(dish.getString("dishName"));
					rUserName.setText("by " + dish.getString("userName"));
					rDescription.setText(dish.getString("shortDescription"));
					loadImages();
					
				   } catch (JSONException e) {
					   // TODO Auto-generated catch block
					   e.printStackTrace();
				   }
				   loadingBar.dismiss();
				}
		}.execute();
		
	}
	
	private void reloadDescription() throws JSONException
	{
		rDescription.setText(dish.getString("shortDescription"));
	}
	private void loadRecipe() throws JSONException
	{
		rDescription.setText(dish.getString("recipe"));
	}
	private void loadIngredients() throws JSONException
	{
		rDescription.setText(dish.getString("ingredients"));
	}
	
	
	
	
	private void loadImages() throws JSONException {
		
		img_src1 = "http://www.indiainme.com/" + dish.getString("imagePrefix1") + "." + dish.getString("extImage1");
		
		new AsyncTask<Void, Void, ArrayList<Bitmap>>(){
			
			
			 @Override
	            protected void onPreExecute(){
	                
				}
			 @Override
			    protected ArrayList<Bitmap> doInBackground(Void... args) {
				 try {
					    ArrayList<Bitmap> images = new ArrayList<Bitmap>();
					    URL url = new URL(img_src1);
				        Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				        images.add(myBitmap);
				        return images;
				    } catch (IOException e) {
				        e.printStackTrace();
				        Log.e("Exception",e.getMessage());
				        return null;
				    }
			 
				 
			    }
			 protected void onPostExecute(ArrayList<Bitmap> images){
				 image1.setImageBitmap(images.get(0)); 
				 if(images.size() > 1 && images.get(1) != null) {
					 image2.setImageBitmap(images.get(1));   
				 }
				 if(images.size() > 2 && images.get(2) != null) {
					 image3.setImageBitmap(images.get(2));   
				 } 
				 if(images.size() > 3 && images.get(3) != null) {
					 image4.setImageBitmap(images.get(3));   
				 } 
				 if(images.size() > 4 && images.get(4) != null) {
					 image5.setImageBitmap(images.get(4));   
				 } 
				 if(images.size() > 5 && images.get(5) != null) {
					 image6.setImageBitmap(images.get(5));   
				 } 
				 
			}
			 
		}.execute();
		
		
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.compose, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
}

/* if( dish.getString("imagePrefix2") !=null) {
String img_src2 = "http://www.indiainme.com/" + dish.getString("imagePrefix2") + "." + dish.getString("extImage2");
} 

if( dish.getString("imagePrefix2") !=null) {
String img_src3 = "http://www.indiainme.com/" + dish.getString("imagePrefix3") + "." + dish.getString("extImage3");
}
*/
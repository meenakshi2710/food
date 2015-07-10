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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.food.R;
import com.food.custom.CustomFragment;
import com.food.utils.AppAlerts;
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
    Button rDesc, rProc, rIngr, rLike; 
    ImageButton  rBookmark;
    LinearLayout recipe_desc;
    ImageView rUserImage, image1, image2, image3, image4, image5, image6;
    String img_src1 = null, img_src2 = null, img_src3 = null, img_src4 = null, img_src5 = null, img_src6 = null; 
    Boolean isLiked;
    JSONObject dish = new JSONObject();
    JSONObject user = new JSONObject();
    JSONObject status = new JSONObject();
    public Bitmap myBitmap;
    public String username;
    
    public RecipeDetail(String dishId, String username){
		this.dishId = dishId;
		this.username = username;
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
		rUserImage = (ImageView) v.findViewById(R.id.user_profile);
		
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
		rDesc.setTextColor(getResources().getColor(R.color.purple_highlight));
		
		rLike = (Button) v.findViewById(R.id.btn_like);
		rLike.setOnClickListener(this);
		
		rBookmark = (ImageButton) v.findViewById(R.id.btn_bookmark);
		rBookmark.setOnClickListener(this);
		
	}
	
	public void onClick(View v) {
	   switch(v.getId()){
		   
		   case R.id.desc:
				rDesc.setEnabled(false);
				rDesc.setTextColor(getResources().getColor(R.color.purple_highlight));
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
				break;
			case R.id.proc:
	        	rDesc.setEnabled(true);
	        	rDesc.setTextColor(getResources().getColor(R.color.black));
	        	rProc.setEnabled(false);
	        	rProc.setTextColor(getResources().getColor(R.color.purple_highlight));
	        	rIngr.setEnabled(true);
	        	rIngr.setTextColor(getResources().getColor(R.color.black));
	        	try {
					loadRecipe();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
				}
	        	break;
			case R.id.ingr:
	        	rDesc.setEnabled(true);
	        	rDesc.setTextColor(getResources().getColor(R.color.black));
	        	rProc.setEnabled(true);
	        	rProc.setTextColor(getResources().getColor(R.color.black));
	        	rIngr.setEnabled(false);
	        	rIngr.setTextColor(getResources().getColor(R.color.purple_highlight));
	        	try {
					loadIngredients();
				} catch (JSONException e) {
					rDescription.setText("This information is not available.");
			    }
	        	break;
			case R.id.btn_like:
	        	if (username != null) {
	        		if (!isLiked) {
	        			System.out.println("Liking recipe");
	        			likeRecipe();
	        		} else {
	        			System.out.println("Unliking recipe");
	        			unlikeRecipe();
	        		}
	        	} else {
	    			new AppAlerts().showErrorDialog(getActivity(), "Not Signed In..", "Please sign in to the app first." );
	    		}
	        	break;
			case R.id.btn_bookmark:
				if (username != null) {
	        		bookmarkRecipe();
	        	} else {
	    			new AppAlerts().showErrorDialog(getActivity(), "Not Signed In..", "Please sign in to the app first." );
	    		}
	        	break;
        }
    }
	
	public void likeRecipe(){
		AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>(){
			
			  @Override
			  protected void onPreExecute(){
			  }
			
			  @Override
			  protected JSONObject doInBackground(Void... params) {
			      JSONParser jParser = new JSONParser();
			      JSONObject json = jParser.getJSONFromUrl("http://www.indiainme.com/api_likeDish.php?dishId="+dishId+"&username="+username+"&like=true");
				  try {
					  JSONArray  status = json.getJSONArray("status");
					  // looping through Result
					  JSONObject c = status.getJSONObject(0);
			          return c;
					                
				  } catch (JSONException e) {
					  e.printStackTrace();
				  } 
						    
				  return null;
			  }
			
			  protected void onPostExecute(JSONObject result) {
			      int value;
				  try {
					   value = result.getInt("value");
					   switch(value){
				             case 0: // change image to unlike
				            	     isLiked = true;
				            	     rLike.setBackgroundResource(R.drawable.btn_unlike); 
					        		 break;
				             case 1: //TODO inform user that he already likes it - although we should not encounter this scenario.
				                     //lbl.setText(like_count + " likes, you already like it.");
				    		         break;
				        }
			
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			  }
		};
	    task.execute((Void[])null);
			
	}
	
	public void unlikeRecipe(){
		AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>(){
			
			  @Override
			  protected void onPreExecute(){
			  }
			
			  @Override
			  protected JSONObject doInBackground(Void... params) {
			      JSONParser jParser = new JSONParser();
			      JSONObject json = jParser.getJSONFromUrl("http://www.indiainme.com/api_likeDish.php?dishId="+dishId+"&username="+username+"&like=false");
				  try {
					  JSONArray  status = json.getJSONArray("status");
					  // looping through Result
					  JSONObject c = status.getJSONObject(0);
			          return c;
					                
				  } catch (JSONException e) {
					  e.printStackTrace();
				  } 
						    
				  return null;
			  }
			
			  protected void onPostExecute(JSONObject result) {
			      int value;
				  try {
					   value = result.getInt("value");
					   switch(value){
				             case 2: // change image to like
				            	     isLiked = false;
				            	     rLike.setBackgroundResource(R.drawable.btn_like); 
					        		 break;
				        }
			
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			  }
		};
	    task.execute((Void[])null);
			
	}
	
	public void bookmarkRecipe(){
		AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>(){
			
			@Override
		    protected void onPreExecute(){
			}
			
		    @Override
			protected JSONObject doInBackground(Void... params) {
			     JSONParser jParser = new JSONParser();
			     JSONObject json = jParser.getJSONFromUrl("http://www.indiainme.com/api_Bookmark.php?type=0&id="+dishId+"&username="+username+"&bookmark=true");
				 try {
					  JSONArray  status = json.getJSONArray("status");
					  // looping through Result
					  JSONObject c = status.getJSONObject(0);
			          return c;
				} catch (JSONException e) {
					  e.printStackTrace();
				} 
						    
				return null;
			}
			
			protected void onPostExecute(JSONObject result) {
			     //TODO change image to unlike
			     int value;
				 try {
					  value = result.getInt("value");
					  switch(value){
				           case 0: //TODO change image to unlike
				        	       //System.out.println("Bookmarked");
				        	       //MainActivity a = (MainActivity) getActivity();
				        	       //a.fragments[7] = null;
				        	   
					        	   break;
				           case 1: //System.out.println("Bookmarked");
				        	       //TODO inform user that he already likes it - although we should not encounter this scenario.
				                   //lbl.setText(like_count + " likes, you already like it.");
				    		       break;
				    	   default:
				    		       // System.out.println("something went wrong");
				    		       break;
				      }
			     } catch (JSONException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				}
		    }
		};
		task.execute((Void[])null);
			
	}
	
	private void loadDescription() {
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
			        JSONObject json = jParser.getJSONFromUrl("http://indiainme.com/api_getDish.php?dishId="+dishId+"&username="+username);
			          try {
			        	     JSONArray  dishArray = json.getJSONArray("dish");
			        	     dish = dishArray.getJSONObject(0);
			        	     String username = dish.getString("username");
			        	     
			        	     JSONArray  statusArray = json.getJSONArray("status");
			        	     status = statusArray.getJSONObject(0);
			                    
			        	     JSONObject user_json = jParser.getJSONFromUrl("http://www.indiainme.com/api_getUser.php?username="+username);
		                     JSONArray  userArray = user_json.getJSONArray("user");
					         user = userArray.getJSONObject(0);
					         String img_src1 = "http://www.indiainme.com/img/profile_image/" + user.getString("id_user") + "." + user.getString("ext");
					         URL url = new URL(img_src1);
								
			            	 myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							 myBitmap = Bitmap.createScaledBitmap(myBitmap, 100, 75, true);	
			        	     
			            } catch (JSONException e) {
			                e.printStackTrace();
			            } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        return dish;
			    }
			 
			 @Override
		    protected void onPostExecute(JSONObject dish) {
				   setHasOptionsMenu(true);
				   try {
						rDishName.setText(dish.getString("dishName"));
						rUserName.setText("by " + dish.getString("name"));
						rDescription.setMovementMethod(new ScrollingMovementMethod());
						rDescription.setText(dish.getString("shortDescription"));
						rUserImage.setImageBitmap(myBitmap);
						loadImages();
						switch(status.getInt("liked")){
						case -1:
							//rLike.setText("Like");
							rLike.setBackgroundResource(R.drawable.btn_like);
							isLiked = false;
						case 0:
							rLike.setBackgroundResource(R.drawable.btn_like);
							isLiked = false;
						case 1:
							//rLike.setText("Liked");
							rLike.setBackgroundResource(R.drawable.btn_unlike);
							isLiked = true;
						} 
						
					
				   } catch (JSONException e) {
					   // TODO Auto-generated catch block
					   e.printStackTrace();
				   }
				   loadingBar.dismiss();
				}
		}.execute();
		
	}
	
	private void reloadDescription() throws JSONException {
		rDescription.setText(dish.getString("shortDescription"));
	}
	private void loadRecipe() throws JSONException {
		rDescription.setText(dish.getString("recipe"));
	}
	private void loadIngredients() throws JSONException {
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
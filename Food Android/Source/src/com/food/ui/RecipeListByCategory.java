package com.food.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.food.DetailActivity;
import com.food.R;
import com.food.Search;
import com.food.custom.CustomFragment;
import com.food.model.Data;
import com.food.utils.JSONParser;
import com.food.utils.RecipeUtil;

/**
 * The Class RecipeList is the Fragment class that is launched when the user
 * clicks on Recipe option in Left navigation drawer or when user select a
 * Category from Category list. It simply display a dummy list of Recipes. You
 * need to write actual implementation for loading and displaying Recipes.
 */
public class RecipeListByCategory extends CustomFragment
{

	/** The Activity list. */
	private ArrayList<Data> recipeList;
	JSONObject user = new JSONObject();
	public String username, profile_name;
	TextView name;
	public Bitmap myBitmap;
	public String categoryName;
	public int categoryId;

	private View v;
    
	public RecipeListByCategory(String categoryName, int categoryId){
		this.categoryName = categoryName;
		this.categoryId = categoryId;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.recipe_list, null);

		loadRecipeList();
		
		return v;
	}

	/**
	 * This method currently loads a dummy list of Recipes. You can write the
	 * actual implementation of loading Recipes.
	 */
	private void loadRecipeList()
	{
		new AsyncTask<String, String, ArrayList<Data>>(){
			
		    ProgressDialog loadingBar;
			 
			 @Override
            protected void onPreExecute(){
            	loadingBar = ProgressDialog.show(getActivity(), "", "Loading "+ categoryName, true);
            	getActivity().getActionBar().setTitle("Recipes > " + categoryName);
            	recipeList = new ArrayList<Data>();
			}
			 
			 @Override
			    protected ArrayList<Data> doInBackground(String... args) {
			        
			        JSONParser jParser = new JSONParser();
			        
			        // Getting JSON from URL
			        JSONObject json = jParser.getJSONFromUrl("http://indiainme.com/api_getDishesByCategory.php?categoryId="+categoryId);
			          try {
			        	  JSONArray  dishes = json.getJSONArray("dishes");
			        	    // looping through All Recipes
			                for (int i = 0; i < dishes.length(); i++) {
			                    JSONObject c = dishes.getJSONObject(i);

			                    String dishId = c.getString("dishId");
			                    String dishName = c.getString("dishName");
			                    String name = c.getString("name");
			                    int categoryId = RecipeUtil.setCategoryImage(c.getInt("category"));
			                    String img_src1 = "http://www.indiainme.com/" + c.getString("imagePrefix1") + "." + c.getString("extImage1");
			            		URL url = new URL(img_src1);
								myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
								myBitmap = Bitmap.createScaledBitmap(myBitmap, 125, 75, true);
								
			                    recipeList.add(new Data(dishId, dishName, "by " + name, categoryId));
			                 }
			            } catch (JSONException e) {
			            	Toast.makeText(getActivity(), "No " + categoryName + " found! Please contribute here :)", Toast.LENGTH_LONG).show();
		                    e.printStackTrace();
		                    return null;
			            } catch (IOException e) {
					        e.printStackTrace();
					        Log.e("Exception",e.getMessage());
					        return null;
					    }
			        
			        return recipeList;
			    }
			 
			 @Override
		        protected void onPostExecute(ArrayList<Data> listt) {
				   loadingBar.dismiss();
				   ListView list = (ListView) v.findViewById(R.id.list);
				   list.setAdapter(new RecipeAdapter());
					list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
								long arg3)
						{
							Intent myIntent = new Intent(getActivity(), DetailActivity.class);
							myIntent.putExtra("detail", true);
							myIntent.putExtra("dishId", recipeList.get(pos).getId());
							startActivity(myIntent);
							
						}
					});

					setHasOptionsMenu(true);
		        }
			 
		}.execute();
		
	}
	

	/**
	 * The Class RecipeAdapter is the adapter class for Recipes ListView. The
	 * currently implementation of this adapter simply display static dummy
	 * contents. You need to write the code for displaying actual contents.
	 */
	private class RecipeAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return recipeList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Data getItem(int arg0)
		{
			return recipeList.get(arg0);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int pos, View v, ViewGroup arg2)
		{
			if (v == null)
				v = LayoutInflater.from(getActivity()).inflate(
						R.layout.recipe_item, null);

			Data c = getItem(pos);
			TextView lbl = (TextView) v.findViewById(R.id.recipe_name);
			lbl.setText(c.getName());

			lbl = (TextView) v.findViewById(R.id.user_name);
			lbl.setText(c.getDesc());

			ImageView categoryImg = (ImageView) v.findViewById(R.id.category);
			categoryImg.setImageResource(c.getImage1());

			ImageView recipeImg = (ImageView) v.findViewById(R.id.img1);
			recipeImg.setImageBitmap(c.getImage3());
			
			return v;
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.search, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_search)
		{
			startActivity(new Intent(getActivity(), Search.class));
		}
		return super.onOptionsItemSelected(item);
	}
}

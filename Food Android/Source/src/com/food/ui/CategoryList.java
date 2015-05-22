package com.food.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.food.DetailActivity;
import com.food.R;
import com.food.Search;
import com.food.custom.CustomFragment;
import com.food.model.Data;
import com.food.utils.JSONParser;
import com.food.utils.RecipeUtil;

/**
 * The Class CategoryList is the Fragment class that is launched when the user
 * clicks on Categories option in Left navigation drawer. It simply display a
 * dummy list of Categories. You need to write actual implementation for loading
 * and displaying Categories.
 */
public class CategoryList extends CustomFragment
{

	/** The Category list. */
	private ArrayList<Data> catList;
	int i;
	String[] categoryNames = {"Snacks", "Breakfasts", "Appetizers", "Side Dishes", "Main Dishes", "Desserts", "Bevarages", "Veggies"};
	int[] categoryImages = {R.drawable.cat2, R.drawable.cat3, R.drawable.cat1, R.drawable.cat6, R.drawable.cat5, R.drawable.cat4, R.drawable.cat2, R.drawable.cat1};
	private String[] categories= {"Snacks", "Breakfasts", "Appetizers", "Side Dishes", "Main Dishes", "Desserts", "Bevarages", "Veggies" };
	View v;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.recipe_list, null);

		loadCategoryList();
		return v;
	}

	/**
	 * This method currently loads a dummy list of Categories. You can write the
	 * actual implementation of loading Categories.
	 */
	private void loadCategoryList()
	{
			
			new AsyncTask<String, String, Void>(){
			
				ProgressDialog loadingBar;
			 
				 @Override
	            protected void onPreExecute(){
	            	loadingBar = ProgressDialog.show(getActivity(), "", "Loading categories...", true);
	            	catList = new ArrayList<Data>();
				}
				 
				 @Override
				    protected Void doInBackground(String... args) {
				        
				        JSONParser jParser = new JSONParser();
				        
				        for (i=0; i<8; i++) {
				    		
				          // Getting JSON from URL
				          JSONObject json = jParser.getJSONFromUrl("http://indiainme.com/api_getDishesCountByCategory.php?categoryId="+i);
				          try {
				        	  JSONArray  dishes = json.getJSONArray("count");
				        	    // looping through All Recipes
				                JSONObject c = dishes.getJSONObject(0);
				                catList.add(new Data(categoryNames[i], c.getString("total"), categoryImages[i]));
				                 
				            } catch (JSONException e) {
				                e.printStackTrace();
				            }
				        }
						return null; 
				        
				        
				    }
				 
	
				@Override
			        protected void onPostExecute(Void params) {
					   loadingBar.dismiss();
					   ListView list = (ListView) v.findViewById(R.id.list);
					   list.setAdapter(new CategoryAdapter());
					   list.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
									long arg3)
							{
								Intent myIntent = new Intent(getActivity(), DetailActivity.class);
								myIntent.putExtra("category", true);
								myIntent.putExtra("categoryName", categories[pos]);
								myIntent.putExtra("categoryId", pos);
								startActivity(myIntent);
							}
						});
					   setHasOptionsMenu(true);
			        }
				 
			}.execute();
		
	}

	/**
	 * The Class CategoryAdapter is the adapter class for Categories ListView. The
	 * currently implementation of this adapter simply display static dummy
	 * contents. You need to write the code for displaying actual contents.
	 */
	private class CategoryAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return catList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Data getItem(int arg0)
		{
			return catList.get(arg0);
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
						R.layout.cat_item, null);

			Data c = getItem(pos);
			TextView lbl = (TextView) v.findViewById(R.id.lbl1);
			lbl.setText(c.getName());

			lbl = (TextView) v.findViewById(R.id.lbl2);
			lbl.setText(c.getDesc());

			ImageView img = (ImageView) v.findViewById(R.id.img1);
			img.setImageResource(c.getImage1());
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

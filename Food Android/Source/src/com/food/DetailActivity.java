package com.food;

import android.os.Bundle;
import android.view.MenuItem;

import com.food.custom.CustomActivity;
import com.food.ui.RecipeDetail;
import com.food.ui.RecipeList;

/**
 * The DetailActivity is the activity class that shows either the Recipe detail
 * fragment or the recipe list fragment based on the parameter passed in intent.
 * This activity is only created to show Back button on ActionBar.
 */
public class DetailActivity extends CustomActivity
{

	/* (non-Javadoc)
	 * @see com.food.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Recipes");

		addFragment();
	}

	/**
	 * Attach the appropriate fragment with activity based on the 'detail'
	 * parameter in Intent.
	 */
	private void addFragment()
	{
		if (getIntent().hasExtra("detail"))
		{
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new RecipeDetail()).commit();
		}
		else
		{
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, new RecipeList()).commit();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

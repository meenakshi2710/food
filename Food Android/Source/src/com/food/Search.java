package com.food;

import java.util.List;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.food.custom.CustomActivity;

/**
 * The main Search screen, launched when user click on Search button on Top
 * Action bar. Write your code inside onQueryTextSubmit/onQueryTextChange
 * method(s) to perform required work. The current implementation simply search
 * for Applications installed in the phone.
 */
public class Search extends CustomActivity
{

	/* (non-Javadoc)
	 * @see com.food.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.search_exp, menu);

		setupSearchView(menu);
		return true;
	}

	/**
	 * Setup the up search view for ActionBar search. The current implementation simply search
 * for Applications installed in the phone.
	 * 
	 * @param menu
	 *            the ActionBar Menu
	 */
	private void setupSearchView(Menu menu)
	{
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		searchView.setIconifiedByDefault(false);
		searchView.setQueryHint(getString(R.string.search));
		searchView.requestFocusFromTouch();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query)
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText)
			{
				// TODO Auto-generated method stub
				return false;
			}
		});

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (searchManager != null)
		{
			List<SearchableInfo> searchables = searchManager
					.getSearchablesInGlobalSearch();

			SearchableInfo info = searchManager
					.getSearchableInfo(getComponentName());
			for (SearchableInfo inf : searchables)
			{
				if (inf.getSuggestAuthority() != null
						&& inf.getSuggestAuthority().startsWith("applications"))
				{
					info = inf;
				}
			}
			searchView.setSearchableInfo(info);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
}

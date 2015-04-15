package com.food.ui;

import java.util.ArrayList;

import android.content.Intent;
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

/**
 * The Class RecipeList is the Fragment class that is launched when the user
 * clicks on Recipe option in Left navigation drawer or when user select a
 * Category from Category list. It simply display a dummy list of Recipes. You
 * need to write actual implementation for loading and displaying Recipes.
 */
public class RecipeList extends CustomFragment
{

	/** The Activity list. */
	private ArrayList<Data> recipeList;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.recipe_list, null);

		loadRecipeList();
		ListView list = (ListView) v.findViewById(R.id.list);
		list.setAdapter(new RecipeAdapter());
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3)
			{
				startActivity(new Intent(getActivity(), DetailActivity.class)
						.putExtra("detail", true));
			}
		});

		setHasOptionsMenu(true);
		return v;
	}

	/**
	 * This method currently loads a dummy list of Recipes. You can write the
	 * actual implementation of loading Recipes.
	 */
	private void loadRecipeList()
	{
		ArrayList<Data> pList = new ArrayList<Data>();
		pList.add(new Data("Mandarian Sorbet", "by John Doe", R.drawable.img1));
		pList.add(new Data("Blackberry Sorbet", "by Stansil Kirilov",
				R.drawable.img2));
		pList.add(new Data("Mango Sorbet", "by John Doe", R.drawable.img3));
		pList.add(new Data("Strawberry Sorbet", "by Steve Thomas",
				R.drawable.img4));
		pList.add(new Data("Apple Sorbet", "by Mark Kratzman", R.drawable.img5));
		pList.add(new Data("Sweet Sorbet", "by Selene Setty", R.drawable.img6));

		recipeList = new ArrayList<Data>(pList);
		recipeList.addAll(pList);
		recipeList.addAll(pList);
		recipeList.addAll(pList);
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
			TextView lbl = (TextView) v.findViewById(R.id.lbl1);
			lbl.setText(c.getTitle1());

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

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
 * The Class CategoryList is the Fragment class that is launched when the user
 * clicks on Categories option in Left navigation drawer. It simply display a
 * dummy list of Categories. You need to write actual implementation for loading
 * and displaying Categories.
 */
public class CategoryList extends CustomFragment
{

	/** The Category list. */
	private ArrayList<Data> catList;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.recipe_list, null);

		loadCategoryList();
		ListView list = (ListView) v.findViewById(R.id.list);
		list.setAdapter(new CategoryAdapter());
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3)
			{
				startActivity(new Intent(getActivity(), DetailActivity.class));
			}
		});

		setHasOptionsMenu(true);
		return v;
	}

	/**
	 * This method currently loads a dummy list of Categories. You can write the
	 * actual implementation of loading Categories.
	 */
	private void loadCategoryList()
	{
		ArrayList<Data> pList = new ArrayList<Data>();
		pList.add(new Data("Appetizer", "69", R.drawable.cat1));
		pList.add(new Data("Bevarages", "42", R.drawable.cat2));
		pList.add(new Data("Breakfasts", "87", R.drawable.cat3));
		pList.add(new Data("Desserts", "34", R.drawable.cat4));
		pList.add(new Data("Main Dishes", "94", R.drawable.cat5));
		pList.add(new Data("Side Dishes", "12", R.drawable.cat6));
		pList.add(new Data("Veggies", "12", R.drawable.cat1));
		pList.add(new Data("Snacks", "12", R.drawable.cat2));

		catList = new ArrayList<Data>(pList);
		//catList.addAll(pList);
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

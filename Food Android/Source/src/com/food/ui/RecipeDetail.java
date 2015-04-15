package com.food.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.food.R;
import com.food.custom.CustomFragment;

/**
 * The Class RecipeDetail is the Fragment class that is launched when the user
 * select a recipe from Recipe List and it simply shows dummy recipe detail text
 * and images. You can customize this to display actual images and text.
 */
public class RecipeDetail extends CustomFragment
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.recipe_detail, null);

		setHasOptionsMenu(true);
		setTouchNClick(v.findViewById(R.id.btn));
		return v;
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

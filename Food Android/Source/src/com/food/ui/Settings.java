package com.food.ui;

import java.util.List;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.food.Login;
import com.food.R;
import com.food.R.id;
import com.food.R.layout;
import com.food.custom.CustomActivity;
import com.food.custom.CustomFragment;

/**
 * The main Search screen, launched when user click on Search button on Top
 * Action bar. Write your code inside onQueryTextSubmit/onQueryTextChange
 * method(s) to perform required work. The current implementation simply search
 * for Applications installed in the phone.
 */
public class Settings extends CustomFragment implements OnClickListener
{

	private Button btnLogout;
	/* (non-Javadoc)
	 * @see com.food.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.settings, null); 
		
		btnLogout = (Button) v.findViewById(R.id.logout);
		btnLogout.setOnClickListener(this);
		
		return v;
	}	
	@Override
	public void onClick(View v)
	{
		super.onClick(v);

		if (v == btnLogout) {
		   startActivity(new Intent(getActivity(), Login.class));
		}
	}

}

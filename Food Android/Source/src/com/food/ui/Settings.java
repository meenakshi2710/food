package com.food.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.food.Login;
import com.food.MainActivity;
import com.food.R;
import com.food.custom.CustomFragment;

/**
 * The main Search screen, launched when user click on Search button on Top
 * Action bar. Write your code inside onQueryTextSubmit/onQueryTextChange
 * method(s) to perform required work. The current implementation simply search
 * for Applications installed in the phone.
 */
public class Settings extends CustomFragment implements OnClickListener
{

	final String LOG_OUT = "event_logout", username;
	private Button btnLogout;
	MainActivity activity;
	TextView userInfo;
	
	public Settings(MainActivity activity, String username) {
		this.activity = activity;
		this.username = username;
	}
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
		
		userInfo = (TextView) v.findViewById(R.id.user_info);
		userInfo.setText("You are currently signed in as " + username);
		
		return v;
	}	
	@Override
	public void onClick(View v)
	{
		super.onClick(v);

		if (v == btnLogout) {
			
			NewMusicList f = (NewMusicList) this.activity.fragments[9];
			if (f != null)
				f.stopPlaying();
			
			startActivity(new Intent(getActivity(), Login.class));
			
		}
	}

}

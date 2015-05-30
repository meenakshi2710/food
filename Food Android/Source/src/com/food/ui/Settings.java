package com.food.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.food.Login;
import com.food.MainActivity;
import com.food.R;
import com.food.custom.CustomFragment;
import com.food.model.Music;

/**
 * The main Search screen, launched when user click on Search button on Top
 * Action bar. Write your code inside onQueryTextSubmit/onQueryTextChange
 * method(s) to perform required work. The current implementation simply search
 * for Applications installed in the phone.
 */
public class Settings extends CustomFragment implements OnClickListener
{

	final String LOG_OUT = "event_logout";
	private Button btnLogout;
	MainActivity activity;
	private MediaPlayer player;
	
	public Settings(MainActivity activity, MediaPlayer player) {
		this.activity = activity;
		this.player = player;
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

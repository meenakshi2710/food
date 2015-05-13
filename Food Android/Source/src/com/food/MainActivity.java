package com.food;


import java.util.ArrayList;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.food.custom.CustomActivity;
import com.food.model.Data;
import com.food.model.Music;
import com.food.ui.CategoryList;
import com.food.ui.ChatterList;
import com.food.ui.LeftNavAdapter;
import com.food.ui.MusicList;
import com.food.ui.MyProfile;
import com.food.ui.RecipeList;
import com.food.ui.Settings;

/**
 * The Class MainActivity is the base activity class of the application. This
 * activity is launched after the Login and it holds all the Fragments used in
 * the app. It also creates the Navigation Drawer on left side.
 */
public class MainActivity extends CustomActivity
{
    public String username;
	public MediaPlayer player = new MediaPlayer();
	public int playing ;
	private String[] titles = new String[10];
	public Fragment[] fragments = new Fragment[10];
	Music[] oMusic = new Music[12];
	
		/** The drawer layout. */
	private DrawerLayout drawerLayout;

	/** ListView for left side drawer. */
	private View drawerLeft;

	/** The drawer toggle. */
	private ActionBarDrawerToggle drawerToggle;

	/** The adapter for left navigation ListView. */
	private LeftNavAdapter adapter;

	Fragment f = null;
	
	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

		setupContainer();
		setupDrawer();
		setupMusicList();
		
		username = getIntent().getStringExtra("USERNAME");
		
	}
	
	/*
	@Override
	protected void onDestroy() {
	  // Unregister since the activity is not visible
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	  System.out.println("kill main activity called");
	  super.onDestroy();
	}
	
	// handler for received Intents for logout event 
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
		  System.out.println("kill main activity called");
		  MainActivity.this.finish();
		  finish();
	  }
	};
	*/
	
	/**
	 * Setup the drawer layout. This method also includes the method calls for
	 * setting up the Left side drawer.
	 */
	private void setupDrawer()
	{
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view)
			{
				setActionBarTitle();
			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
				getActionBar().setTitle("Menu");
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		drawerLayout.closeDrawers();

		setupLeftNavDrawer();
	}

	/**
	 * Setup the left navigation drawer/slider. You can add your logic to load
	 * the contents to be displayed on the left side drawer. You can also setup
	 * the Header and Footer contents of left drawer if you need them.
	 */
	private void setupLeftNavDrawer()
	{
		drawerLeft = findViewById(R.id.left);

		ListView leeftList = (ListView) findViewById(R.id.left_drawer);

		adapter = new LeftNavAdapter(this, getDummyLeftNavItems());
		leeftList.setAdapter(adapter);
		leeftList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3)
			{
				drawerLayout.closeDrawers();
				launchFragment(pos);
			}
		});
		//drawerLayout.openDrawer(drawerLeft);

		setTouchNClick(R.id.recipes);
		setTouchNClick(R.id.music);
		setTouchNClick(R.id.chatter);
		setTouchNClick(R.id.logout);
	}
	
	public Fragment getFragment(int pos){
		return fragments[pos];
	}
	/**
	 * This method returns a list of dummy items for left navigation slider. You
	 * can write or replace this method with the actual implementation for list
	 * items.
	 * 
	 * @return the dummy items
	 */
	private ArrayList<Data> getDummyLeftNavItems()
	{

		ArrayList<Data> al = new ArrayList<Data>();
		al.add(new Data("Home", R.drawable.ic_nav1, R.drawable.ic_nav1_sel));
		al.add(new Data("Compose", R.drawable.ic_nav2, R.drawable.ic_nav2_sel));
		al.add(new Data("Recipes", R.drawable.ic_nav3, R.drawable.ic_nav3_sel));
		al.add(new Data("Browse Categories", R.drawable.ic_nav4, R.drawable.ic_nav4_sel));
		al.add(new Data("My Profile", R.drawable.ic_nav5, R.drawable.ic_nav5_sel));
		al.add(new Data("My Friends", R.drawable.ic_nav5, R.drawable.ic_nav5_sel));
		al.add(new Data("My Favorites", R.drawable.ic_nav5, R.drawable.ic_nav5_sel));
		al.add(new Data("Bookmarks", R.drawable.ic_nav5, R.drawable.ic_nav5_sel));
		al.add(new Data("Settings", R.drawable.ic_nav6, R.drawable.ic_nav6_sel));
		return al;
	}

	/**
	 * This method can be used to attach Fragment on activity view for a
	 * particular tab position. You can customize this method as per your need.
	 * 
	 * @param pos
	 *            the position of tab selected.
	 */
	private void launchFragment(int pos)
	{
		Fragment f = null;
		String title = null;
		if (fragments.length == 0 || fragments[pos] == null) {
	    	fragments[pos ] = createNewFragment(pos);
		}
	    if (titles.length == 0 || titles[pos] == null) {
	    	titles[pos] = setTitleFragment(pos);
	    }
	    f = fragments[pos];
	    title = titles[pos];
	    
	    System.out.println(" ### view is : " + fragments[pos]);
		
	    if (f != null)
		{
			while (getSupportFragmentManager().getBackStackEntryCount() > 0)
			{
				getSupportFragmentManager().popBackStackImmediate();
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, f).addToBackStack(title)
					.commit();
			//getSupportFragmentManager().saveFragmentInstanceState(f);
			if (adapter != null)
				adapter.setSelection(pos);
		}
	    
	}
	
	private String setTitleFragment(int pos) {
		switch(pos) {
		   case 0:
			   enableAllTabs();
			   return "New Posts";
		   case 1:
			   enableAllTabs();
			   return "Compose";
		   case 2:
			   setCurrTab(R.id.recipes);
			   return "Recipes";
		   case 3:
			   enableAllTabs();
			   return "Browse Categories";
		   case 4:
			   enableAllTabs();
			   return "Profile";
		   case 5:
			   enableAllTabs();
			   return "Friends";
		   case 6:
			   enableAllTabs();
			   return "My Favorites";
		   case 7:
			   enableAllTabs();
			   return "Bookmarks";
		   case 8:
			   enableAllTabs();
			   return "Settings";
		   case 9:
			   setCurrTab(R.id.music);
			   return "Music";
		   default: 
			   enableAllTabs();
			   return "Home";
		}
	}
	
	private Fragment createNewFragment(int pos) {
		
		switch(pos){
		   case 0:
			     return new ChatterList();
		   case 1:
				 return new RecipeList();
		   case 2:
				 return new RecipeList();
		   case 3:
				 return new CategoryList();
		   case 4:
				 return new MyProfile(username);
		   case 5:
				 return new RecipeList();
		   case 6:
				 return new RecipeList();
		   case 7:
				 return new RecipeList();
		   case 8:
				 return new Settings(this, player);
		   case 9:
				 return new MusicList(this, player, oMusic);
		   default: 
			    return new RecipeList();
		}	
	}

	/**
	 * Setup the container fragment for drawer layout. The current
	 * implementation of this method simply calls launchFragment method for tab
	 * position 2 . You can customize
	 * this method as per your need to display specific content.
	 */
	private void setupContainer()
	{
		getSupportFragmentManager().addOnBackStackChangedListener(
				new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged()
					{
						setActionBarTitle();
					}
				});
		launchFragment(2);
	}
	private void enableAllTabs(){
		findViewById(R.id.music).setEnabled(true);
		findViewById(R.id.chatter).setEnabled(true);
		findViewById(R.id.recipes).setEnabled(true);
	}
	
	private void setCurrTab(int res){
		enableAllTabs();
		findViewById(res).setEnabled(false);
	}

	/**
	 * Set the action bar title text.
	 */
	private void setActionBarTitle()
	{
		if (drawerLayout.isDrawerOpen(drawerLeft))
		{
			getActionBar().setTitle(R.string.app_name);
			return;
		}
		if (getSupportFragmentManager().getBackStackEntryCount() == 0)
			return;
		String title = getSupportFragmentManager().getBackStackEntryAt(
				getSupportFragmentManager().getBackStackEntryCount() - 1)
				.getName();
		getActionBar().setTitle(title);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		drawerToggle.onConfigurationChanged(newConfig);
	}

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (drawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (getSupportFragmentManager().getBackStackEntryCount() > 1)
			{
				getSupportFragmentManager().popBackStackImmediate();
			}
			else
				finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/* (non-Javadoc)
	 * @see com.food.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);

		switch (v.getId()) {
		   //case R.id.logout:
		   //  startActivity(new Intent(this, Login.class));
		  //	 finish();
		  //	 break;
		   case R.id.chatter:
		     launchFragment(0);
		     setCurrTab(R.id.chatter); 
		     break;
		   case R.id.recipes:
			 System.out.println("tapped on recipes from tab");  
			 launchFragment(2);
			 setCurrTab(R.id.recipes); 
			 break;
		   case R.id.music:
			 launchFragment(9);
			 setCurrTab(R.id.music); 
	   	     break;
	   	   default:
	   		enableAllTabs();
		}
	}
        
     private void setupMusicList() {
    	
 		oMusic[0] = new Music(0, "Trystin Playlist", "", "", R.drawable.cat1);
 		oMusic[1] = new Music(1, "Bollywood Hungama", "http://www.live365.com/play/bollywoodhungama", "", R.drawable.cat2);
 		oMusic[2] = new Music(2, "Dubai 101.6", "http://5303.live.streamtheworld.com/ARNCITY_SC", "", R.drawable.cat3);
 		oMusic[3] = new Music(3, "Desi Music Mix", "http://s1.desimusicmix.com:8014/;?icy=http", "", R.drawable.cat4);
 		oMusic[4] = new Music(4, "Bombay Beats", "http://123.176.41.8:8056/;?icy=http", "", R.drawable.cat6);
 		oMusic[5] = new Music(5, "Radio City IndiPop", "http://208.85.2.106:9910/;?icy=http", "", R.drawable.cat6);
 		oMusic[6] = new Music(6, "Punjabi USA", "http://198.178.123.5:7016/;?icy=http", "", R.drawable.cat5);
 		oMusic[7] = new Music(7, "Bollywood Hits", "http://50.7.77.115:8174/;?icy=http", "", R.drawable.cat1);
 		oMusic[8] = new Music(8, "Bollywood Tashan", "http://viadj.viastreaming.net:7090/;?icy=http", "", R.drawable.cat2);
 		oMusic[9] = new Music(9, "Hindi Evergreen", "http://50.7.77.114:8296/;?icy=http", "", R.drawable.cat3);
 		oMusic[10] = new Music(10, "Spice Box", "http://96.30.15.163:8039/;?icy=http", "", R.drawable.cat4);
 		oMusic[11] = new Music(11, "Radio Teentaal", "http://195.154.176.33:8000/;?icy=http", "", R.drawable.cat5);
 		
 	}
     
}

package com.food.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.food.Login;
import com.food.R;

public class LaunchFragment {

	
	public void launch(Activity a, View v){
		switch (v.getId()) {
		   case R.id.logout:
			   Intent i = new Intent().setClass(a.getApplication(), Login.class);  
			   
			   // Launch the new activity and add the additional flags to the intent
			   a.getApplication().startActivity(i);
			   //startActivity(new Intent(this, Login.class));
			   a.finish();
			 break;
		   /*case R.id.chatter:
		     launchFragment(0);
		     setCurrTab(R.id.chatter); 
		     break;
		   case R.id.recipes:
			 launchFragment(2);
			 setCurrTab(R.id.recipes); 
			 break;
		   case R.id.music:
			 launchFragment(9);
			 setCurrTab(R.id.music); 
	   	     break;
	   	   default:
	   		enableAllTabs();
	   		*/
		}
	}
}

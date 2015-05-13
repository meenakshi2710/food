package com.food.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import android.os.AsyncTask;

public class TrystinMusic {
		
	public ArrayList<String> tList = new ArrayList<String>();
	public String toPlay;
	
	public TrystinMusic() {
		this.tList = getPlaylist();
	}
	public ArrayList<String> getPlaylist() {
	 	AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
			@Override
            protected Void doInBackground(Void... params) {
            
				URL url;
		 		try {
		 			url = new URL("http://www.indiainme.com/music/music.txt");
		 				             
		 		    // read text returned by server
		 		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		 				             
		 			String line;
		 			while ((line = in.readLine()) != null) {
		 				tList.add("http://www.indiainme.com/music/"+line);
		 		    }
		 		    in.close();	            
		 
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	       return null;
		}
        };
        
        task.execute((Void[])null);
        return tList;
	}
	public String randomSong(ArrayList<String> tList){
		int idx = new Random().nextInt(tList.size());
		toPlay = tList.get(idx);
		return toPlay;
	}
		
	
	
}


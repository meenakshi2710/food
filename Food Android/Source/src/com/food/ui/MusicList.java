package com.food.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.food.MainActivity;
import com.food.R;
import com.food.Search;
import com.food.custom.CustomFragment;
import com.food.model.Data;
import com.food.model.Music;
/**
 * The Class MusicList is the Fragment class that is launched when the user
 * clicks on Music option. It simply display a
 * dummy list of Radio stations. You need to write actual implementation for loading
 * and displaying radio stations.
 */
public class MusicList extends CustomFragment implements OnClickListener, MediaPlayer.OnErrorListener
{

	private ConnectivityManager connMgr;
	
	/** The Category list. */
	private ArrayList<Data> musicList;
    private Button buttonPlay;
    private Button buttonStopPlay;
    private MediaPlayer player;
    private boolean isPlaying = false;
    MainActivity activity;
    View v;
    int curPlaying = -1;
    ListView list =  null;
    Context context;
    int poss;
    Music[] oMusic = new Music[12];
    ProgressDialog mediaPlayerLoadingBar;
    
    public MusicList(Context context, MediaPlayer player) {
		connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		this.player = player;
		this.context = context;
		mediaPlayerLoadingBar = new ProgressDialog(this.context);
		oMusic[0] = new Music(0, "Bombay Beats", "http://123.176.41.8:8056/;?icy=http", "", R.drawable.cat1);
		oMusic[1] = new Music(1, "Bollywood Hungama", "http://www.live365.com/play/bollywoodhungama", "", R.drawable.cat2);
		oMusic[2] = new Music(2, "Dubai 101.6", "http://5303.live.streamtheworld.com/ARNCITY_SC", "", R.drawable.cat3);
		oMusic[3] = new Music(3, "Desi Music Mix", "http://s1.desimusicmix.com:8014/;?icy=http", "", R.drawable.cat4);
		oMusic[4] = new Music(4, "Punjabi USA", "http://198.178.123.5:7016/;?icy=http", "", R.drawable.cat5);
		oMusic[5] = new Music(5, "Radio City IndiPop", "http://208.85.2.106:9910/;?icy=http", "", R.drawable.cat6);
		oMusic[6] = new Music(6, "Bollywood Hits", "http://50.7.77.115:8174/;?icy=http", "", R.drawable.cat1);
		oMusic[7] = new Music(7, "Bollywood Tashan", "http://viadj.viastreaming.net:7090/;?icy=http", "", R.drawable.cat2);
		oMusic[8] = new Music(8, "Hindi Evergreen", "http://50.7.77.114:8296/;?icy=http", "", R.drawable.cat3);
		oMusic[9] = new Music(9, "Spice Box", "http://96.30.15.163:8039/;?icy=http", "", R.drawable.cat4);
		oMusic[10] = new Music(10, "Radio Teentaal", "http://195.154.176.33:8000/;?icy=http", "", R.drawable.cat5);
		oMusic[11] = new Music(11, "Bombay Beats", "http://123.176.41.8:8056/;?icy=http", "", R.drawable.cat6);
		
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(player.isPlaying()){
			buttonPlay.setEnabled(false);
	        buttonStopPlay.setEnabled(true);
	        isPlaying = true;
            System.out.println("## playing : " + curPlaying );
	    } 
    }
    
    /* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.music_list, null); 
		
		loadMusicList();
		list = (ListView) v.findViewById(R.id.list);
		list.setAdapter(new MusicAdapter());
		setHasOptionsMenu(true);
		initializeUIElements(v);
		
		list.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
								long arg3)	{
				list.setSelection(pos);
				startPlaying(pos);
			}
		});
		return v;
	}
	
	private void initializeUIElements(View v) {

        buttonPlay = (Button) v.findViewById(R.id.btn_select);
        buttonPlay.setOnClickListener(this);

        buttonStopPlay = (Button) v.findViewById(R.id.btn_stop);
        buttonStopPlay.setEnabled(false);
        buttonStopPlay.setOnClickListener(this);
        
    }

	public void onClick(View v) {
		if(!isInternetConnected()){
			Toast.makeText(getActivity(), "No Internet Connection!",Toast.LENGTH_LONG).show();
			startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
		}

		if (v == buttonPlay) {
			player = new MediaPlayer();
			startPlaying(0);
        	activity.playing = 0;
        } else if (v == buttonStopPlay) {
            stopPlaying();
        } 
    }
	
	private void startPlaying(int pos) {
		
		if(isPlaying){
			stopPlaying(); 
			player = new MediaPlayer();
		}
	
        poss = pos;
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute(){
            	buttonPlay.setText("Connecting..");
            	mediaPlayerLoadingBar = ProgressDialog.show(context, "", "Connecting, Please wait...", true);
                
                try {
                	initializeMediaPlayer(poss);
            		buttonStopPlay.setEnabled(true);
                    buttonPlay.setEnabled(false);
                    
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
            	
            	try {
                	player.prepare();
        			player.start();
        	        
        	        isPlaying = true;
        	        buttonPlay.setText("Playing " + oMusic[poss].getTitle());
        	        curPlaying = poss;
        	        
        		} catch (IllegalStateException e) {
        			e.printStackTrace();
        		} catch (IOException e) {
        			e.printStackTrace();
        		} catch(Exception e) {
        			e.printStackTrace();
        		}
            	return null;
            }

            protected void onPostExecute(Void result){
            	
            	mediaPlayerLoadingBar.dismiss();
            	if(isPlaying){
                	Toast.makeText(getActivity(),"Now Playing - " + oMusic[poss].getTitle(),Toast.LENGTH_LONG).show();
            	} else {
                	Toast.makeText(getActivity(),"Sorry, please try again..",Toast.LENGTH_LONG).show();
            	}
        }

        };
        
        task.execute((Void[])null);
    
    }
        
    private void stopPlaying() {
    	if (player.isPlaying()) {
        	System.out.println("stopping now....");
        	player.stop();
            player.release();
        }

        buttonPlay.setEnabled(true);
        buttonPlay.setText("Play");
        buttonStopPlay.setEnabled(false);
        isPlaying = false;
    }
    
    private void initializeMediaPlayer(int pos) {
    	String URL = oMusic[pos].getURL();
        
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
        	System.out.println("set url complete");
            player.setDataSource(URL);
        } catch (IllegalArgumentException e) {
        	System.out.println("caught 01");
        	e.printStackTrace();
        } catch (IllegalStateException e) {
        	System.out.println("caught 02");
        	e.printStackTrace();
        } catch (IOException e) {
        	System.out.println("caught 03");
        	e.printStackTrace();
        }
        
    }

    
	/**
	 * This method currently loads a dummy list of Categories. You can write the
	 * actual implementation of loading Categories.
	 */
	private void loadMusicList()
	{
		ArrayList<Data> pList = new ArrayList<Data>();
		pList.add(new Data(oMusic[0].getTitle(), "69", oMusic[0].getImage1()));
		pList.add(new Data(oMusic[1].getTitle(), "87", oMusic[1].getImage1()));
		pList.add(new Data(oMusic[2].getTitle(), "34", oMusic[2].getImage1()));
		pList.add(new Data(oMusic[3].getTitle(), "94", oMusic[3].getImage1()));
		pList.add(new Data(oMusic[4].getTitle(), "42", oMusic[4].getImage1()));
		pList.add(new Data(oMusic[5].getTitle(), "42", oMusic[5].getImage1()));
		pList.add(new Data(oMusic[6].getTitle(), "42", oMusic[6].getImage1()));
		pList.add(new Data(oMusic[7].getTitle(), "42", oMusic[7].getImage1()));
		pList.add(new Data(oMusic[8].getTitle(), "42", oMusic[8].getImage1()));
		pList.add(new Data(oMusic[9].getTitle(), "42", oMusic[9].getImage1()));
		pList.add(new Data(oMusic[10].getTitle(), "42", oMusic[10].getImage1()));
		pList.add(new Data(oMusic[11].getTitle(), "94", oMusic[11].getImage1()));

		musicList = new ArrayList<Data>(pList);
		
	}

	/**
	 * The Class CategoryAdapter is the adapter class for Music ListView. The
	 * currently implementation of this adapter simply display static dummy
	 * contents. You need to write the code for displaying actual contents.
	 */
	private class MusicAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return musicList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Data getItem(int arg0)
		{
			return musicList.get(arg0);
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
						R.layout.music_item, null);

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
	@Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what){
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                	System.out.println("unknown media playback error");
                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                	System.out.println("server connection died");
                default:
                	System.out.println("generic audio playback error");
                    break;
            }

            switch (extra){
                case MediaPlayer.MEDIA_ERROR_IO:
                	System.out.println("IO media error");
                    break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                	System.out.println("media error, malformed");
                    break;
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                	System.out.println("unsupported media content");
                    break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                	System.out.println("media timeout error");
                    break;
                default:
                	System.out.println("unknown playback error");
                    break;
            }
        return true;
    }
	public boolean isInternetConnected() {
	     
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		boolean isWifiConn = networkInfo.isConnected();
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = networkInfo.isConnected();
		return isWifiConn;
		//Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);
	}
	
	
}

/*  
 * 
 *  // club bollywood - NO
        	    //URL = "http://pub9am.radiotunes.com/radiotunes_clubbollywood";
         		//URL = "http://rtclubbollywood.radio.net/";
            	//URL = "http://www.radiotunes.com/clubbollywood";
            	//URL = "http://tunein.com/radio/RadioTunes-Club-Bollywood-s163461/";
         		// URL = "http://www.onlineradios.in/#club-bollywood";
        	
//        	case 5: // Bollywood bhangra - NO
//        		URL = "http://216.235.85.23/play?now=22";
//        		break;
           
//        	case 7: // Radio City Bangalore - NO
//        		URL ="prclive1.listenon.in:9960/;";
//        		break;
//        	case 8: // Radio Mirchi 98.3 - NO
//        		URL = "http://www.onlineradios.in/#radio-mirchi-98-3-fm";
//        		break;
 *  
 *  
 *  startPlaying() code without use of threads
 * 	initializeMediaPlayer(pos);
	buttonStopPlay.setEnabled(true);
    buttonPlay.setEnabled(false);
    
    class MyThread implements Runnable {

    	   public MyThread(int pos) {
    	       // store parameter for later user
    	   }

    	   public void run() {
    	   }
    	}
    Runnable r = new MyThread(pos);
    new Thread(r).start();
     
    try {
    	player.prepare();
		player.start();
        
        isPlaying = true;
        buttonPlay.setText("Playing " + pos);
        
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch(Exception e) {// might take long! (for buffering, etc)
		Toast.makeText(getActivity(),
				   "Sorry, please try again..",
				   Toast.LENGTH_LONG).show();
	}
	
 */	

/*
switch(pos){
	case 0:  // works
		URL ="http://123.176.41.8:8056/;?icy=http";
		break;
	case 1: // Bollywood Hungama - works
		URL = "http://www.live365.com/play/bollywoodhungama";
		break;
	case 2: //  Dubai 101.6 - works
		URL = "http://5303.live.streamtheworld.com/ARNCITY_SC";
		break;
    case 3: // Desi Music mix - works
    	URL = "http://s1.desimusicmix.com:8014/;?icy=http";
    	break;
    case 4: // Punjabi Radio USA - works copy of case 0?
    	URL = "http://198.178.123.5:7016/;?icy=http";
    	break;
    case 5: // Radio City IndiPop - NO
    	URL = "http://208.85.2.106:9910/;?icy=http";
    	break;
    case 6: // Bollywood Hits - works
    	URL = "http://50.7.77.115:8174/;?icy=http";
    	break;
	case 7: // Bollywood Tashan - works
		URL = "http://viadj.viastreaming.net:7090/;?icy=http";
		break;
	case 8: // Hindi Evergreen - works
		URL = "http://50.7.77.114:8296/;?icy=http";
		break;
	case 9: // Spice Box - works
		URL = "http://96.30.15.163:8039/;?icy=http";
		break;
	case 10: // Radio Teentaal - works
		URL = "http://195.154.176.33:8000/;?icy=http";
		break;
		
	default: // works
		URL ="http://123.176.41.8:8056/;?icy=http";
		break;
}
*/

package com.food.ui;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.food.R;
import com.food.Search;
import com.food.custom.CustomFragment;
import com.food.model.Data;
import com.food.model.Music;
import com.food.utils.TrystinMusic;
/**
 * The Class MusicList is the Fragment class that is launched when the user
 * clicks on Music option.
 */
public class MusicList extends CustomFragment implements OnClickListener
{

	private ConnectivityManager connMgr;
	TrystinMusic tRadio = new TrystinMusic();
 	
	/** The Category list. */
	private ArrayList<Data> musicList;
    private Button buttonPlay;
    private Button buttonStopPlay;
    private MediaPlayer player;
    private boolean isPlaying = false;
    View v;
    int curPlaying, toPlay;
    ListView list =  null;
    Context context;
    ProgressDialog mediaPlayerLoadingBar;
    Music[] oMusic;
    
    public MusicList(Context context, MediaPlayer player, Music[] oMusic) {
		connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		this.player = player;
		this.context = context;
		mediaPlayerLoadingBar = new ProgressDialog(this.context);
		this.oMusic = oMusic;
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(" ### activity is : " + getActivity());
       
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
        
        if(player.isPlaying()){
			buttonPlay.setEnabled(false);
	        buttonStopPlay.setEnabled(true);
	        isPlaying = true;
	        buttonPlay.setText("Playing " + oMusic[toPlay].getTitle());
            Toast.makeText(getActivity(),"Playing - " + oMusic[curPlaying].getTitle(),Toast.LENGTH_LONG).show();
	    } 
        
    }

	public void onClick(View v) {
		if(!isInternetConnected()){
			Toast.makeText(getActivity(), "No Internet Connection!",Toast.LENGTH_LONG).show();
			startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
		}
		if (v == buttonPlay) {
			player = new MediaPlayer();
			startPlaying(0);
        } else if (v == buttonStopPlay) {
            stopPlaying();
        } 
    }
	
	private void startPlaying(int pos) {
		
		if(isPlaying){
			stopPlaying(); 
			player = new MediaPlayer();
		}
	
		toPlay = pos;
		
		if (toPlay == 0){
			while(tRadio.tList.size() == 0) {
	     		try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	     	}
	    	
	     	String currURL = tRadio.randomSong(tRadio.tList);
		    tRadio(toPlay, currURL);
		    
         } else {
			playRadio();
		}
		
	}
	private void tRadio(final int toPlay, final String currURL){
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute(){
            	buttonStopPlay.setEnabled(true);
                buttonPlay.setEnabled(false);
            	try {
                	String URL = currURL;
                	player = new MediaPlayer();
                	player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(URL);
                              		
            		} catch (IllegalArgumentException e) {
	                	e.printStackTrace();
            		} catch (IllegalStateException e) {
	                	e.printStackTrace();
            		} catch (IOException e) {
	                	e.printStackTrace();
            		}
            }

            @Override
            protected Void doInBackground(Void... params) {
            	
            	try {
                	if(!player.isPlaying()) {
                		player.prepare();
                		player.start();
            	        isPlaying = true;
                	}
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
            	
            	if(isPlaying){
                	curPlaying = toPlay;
                	buttonPlay.setText("Playing " + oMusic[toPlay].getTitle());
                } else {
                	Toast.makeText(getActivity(),"Sorry, please try another station..",Toast.LENGTH_LONG).show();
                	try{
                		player.stop();
                		player.release();
                		isPlaying = false;
                	} catch(Exception ex) {
                	}
                	
                }
            
            }
            

        };
        
        task.execute((Void[])null);
        
        player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {
				player.stop();
				player.release();
				String currURL = tRadio.randomSong(tRadio.tList);
				System.out.println("next song selected: " + currURL);
	    		tRadio(toPlay, currURL);
	    		isPlaying = false;
				
			}
        });
        
	}
	
	
	private void playRadio(){

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute(){
            	buttonPlay.setText("Connecting..");
            	mediaPlayerLoadingBar = ProgressDialog.show(context, "", "Connecting, Please wait...", true);
                
                try {
                	player = new MediaPlayer();
                	initializeMediaPlayer(toPlay);
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
                	Toast.makeText(getActivity(),"Now Playing - " + oMusic[toPlay].getTitle(),Toast.LENGTH_LONG).show();
                	curPlaying = toPlay;
                	System.out.println("curPlaying is now set to : " + curPlaying);
                	buttonPlay.setText("Playing " + oMusic[toPlay].getTitle());
            	} else {
                	Toast.makeText(getActivity(),"Sorry, please try another station..",Toast.LENGTH_LONG).show();
            	}
        }

        };
        task.execute((Void[])null);
        
	}
        
    public void stopPlaying() {
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
		pList.add(new Data(oMusic[0].getTitle(), "69 listeners", oMusic[0].getImage1()));
		pList.add(new Data(oMusic[1].getTitle(), "87 listeners", oMusic[1].getImage1()));
		pList.add(new Data(oMusic[2].getTitle(), "34 listeners", oMusic[2].getImage1()));
		pList.add(new Data(oMusic[3].getTitle(), "94 listeners", oMusic[3].getImage1()));
		pList.add(new Data(oMusic[4].getTitle(), "42 listeners", oMusic[4].getImage1()));
		pList.add(new Data(oMusic[5].getTitle(), "51 listeners", oMusic[5].getImage1()));
		pList.add(new Data(oMusic[6].getTitle(), "39 listeners", oMusic[6].getImage1()));
		pList.add(new Data(oMusic[7].getTitle(), "42 listeners", oMusic[7].getImage1()));
		pList.add(new Data(oMusic[8].getTitle(), "21 listeners", oMusic[8].getImage1()));
		pList.add(new Data(oMusic[9].getTitle(), "42 listeners", oMusic[9].getImage1()));
		pList.add(new Data(oMusic[10].getTitle(), "80 listeners", oMusic[10].getImage1()));
		pList.add(new Data(oMusic[11].getTitle(), "94 listeners", oMusic[11].getImage1()));
		pList.add(new Data(oMusic[12].getTitle(), "94 listeners", oMusic[11].getImage1()));
		
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
	
	public boolean isInternetConnected() {
	     
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		boolean isWifiConn = networkInfo.isConnected();
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = networkInfo.isConnected();
		return isWifiConn;
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
     
*/
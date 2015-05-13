package com.food.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class SignInActivity {

	ProgressDialog loadingBar;
	String username, password;
	private int byGetOrPost = 0; 
	private int status = 0;
	private Context context;
	
	//flag 0 means get and 1 means post.(By default it is get.)
	public SignInActivity(Context context, int flag, String username, String password) {
	      this.context = context;
	      byGetOrPost = flag;
	      this.username = username;
	      this.password = password;
	   }

    public int checkLogin() {
		
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>(){

            @Override
            protected void onPreExecute(){
            	loadingBar = ProgressDialog.show(context, "", "Connecting, Please wait...", true);
            }

            @Override
            protected String doInBackground(String... args) {
            	if(byGetOrPost == 1){ //means by Get Method
                    try{
                       String link = "http://indiainme.com/iosLogin.php?username="+username+"&password="+password;URL url = new URL(link);
                       HttpClient client = new DefaultHttpClient();
                       HttpGet request = new HttpGet();
                       request.setURI(new URI(link));
                       HttpResponse response = client.execute(request);
                       BufferedReader in = new BufferedReader
                      (new InputStreamReader(response.getEntity().getContent()));

                      StringBuffer sb = new StringBuffer("");
                      String line="";
                      while ((line = in.readLine()) != null) {
                         sb.append(line);
                         break;
                       }
                       in.close();
                       
                       return sb.toString();
                 }catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                 }
                 }
                 else{
                    try{
                       String link="http://indiainme.com/iosLogin.php";
                       String data  = URLEncoder.encode("username", "UTF-8") 
                       + "=" + URLEncoder.encode(username, "UTF-8");
                       data += "&" + URLEncoder.encode("password", "UTF-8") 
                       + "=" + URLEncoder.encode(password, "UTF-8");
                       URL url = new URL(link);
                       URLConnection conn = url.openConnection(); 
                       conn.setDoOutput(true); 
                       OutputStreamWriter wr = new OutputStreamWriter
                       (conn.getOutputStream()); 
                       wr.write( data ); 
                       wr.flush(); 
                       BufferedReader reader = new BufferedReader
                       (new InputStreamReader(conn.getInputStream()));
                       StringBuilder sb = new StringBuilder();
                       String line = null;
                       // Read Server Response
                       while((line = reader.readLine()) != null)
                       {
                    	  System.out.println("line : " + line);
                    	  sb.append(line);
                          break;
                       }
                       String result = sb.toString();
                       if(result.contains("{\"success\":1}")){
                   		status = 1;
                   	   } else if(result.contains("Invalid Username/Password")){
                   	    status = 2;
                   	   }	
                       return result;
                       
                    }catch(Exception e){
                       return new String("Exception: " + e.getMessage());
                    }
                 }
              }

            @Override
            protected void onPostExecute(String result){
            	loadingBar.dismiss();
            }
            

        };
        task.execute();
        
        while(status == 0) {
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	 		
	 	}
        return status;
	} 
    
    
}

package com.food;

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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.food.custom.CustomActivity;
import com.food.utils.AppAlerts;
/**
 * The Class Login is an Activity class that shows the login screen to users.
 * The current implementation simply start the MainActivity. You can write your
 * own logic for actual login and for login using Facebook and Twitter.
 */
public class Login extends CustomActivity
{
	private EditText usernameField,passwordField;
	private Button mSubmit;
	ProgressDialog loadingBar;
	String username, password;
	private int byGetOrPost = 0; 
	private int status = 0;
	 /* (non-Javadoc)
	 * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		usernameField = (EditText)findViewById(R.id.username);
	    passwordField = (EditText)findViewById(R.id.password);
	    mSubmit = (Button)findViewById(R.id.btnLogin);
	    
		setTouchNClick(R.id.btnLogin);
		setTouchNClick(R.id.btnFb);
		setTouchNClick(R.id.btnTw);
		setTouchNClick(R.id.btnReg);
		
		
	}

	/* (non-Javadoc)
	 * @see com.chatt.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId()) {
		   case R.id.btnLogin:
			   username = usernameField.getText().toString();
			   password = passwordField.getText().toString();
			   // Check for empty data in the form
               if (username.trim().length() > 0 && password.trim().length() > 0) {
            	   //status = new SignInActivity(this, 1).checkLogin();
            	   checkLogin();
            	   switch(status)  {
            	   
	            	   case 1: 
	            		   // login user
	            		   Intent intent = new Intent(this, MainActivity.class);
	            		   intent.putExtra("USERNAME", username.trim());
	            		   startActivity(intent);
	            		   finish();
	                       //Toast.makeText(getApplicationContext(), "Welcome to Trystin", Toast.LENGTH_LONG).show();
	                       break;
	            	   case 2:
	            		   // username/password incorrect
	            		   new AppAlerts().showErrorDialog(this, "Login Error..", "Please enter username/password correctly." );
	            		   break;
            	   }
            	} else {
            		new AppAlerts().showErrorDialog(this, "Login Error..", "Username/Password cannot be empty!" );
         		}
		       break;
		   case R.id.btnReg:
			  startActivity(new Intent(this, Register.class));
			  finish();
	          break;
		   case R.id.btnFb:
			   new AppAlerts().showErrorDialog(this, "Login Error..", "This feautre is not available yet!" );
        	   break;
		   default:
			  startActivity(new Intent(this, MainActivity.class));
			  finish();
			  break;
		 }
	}
	
	private void checkLogin() {
		
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>(){

            @Override
            protected void onPreExecute(){
            	loadingBar = ProgressDialog.show(Login.this, "", "Signing in..", true);
            }

            @Override
            protected String doInBackground(String... args) {
            	if(byGetOrPost == 1){ //means by Get Method
                    try{
                       String link = "http://indiainme.com/iosLogin.php?username="+username+"&password="+password;
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
	}
    
}


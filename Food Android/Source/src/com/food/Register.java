package com.food;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
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
public class Register extends CustomActivity
{
	private EditText usernameField,passwordField, nameField, emailField;
	private Button mSubmit;
	String name, email, username, password;
	ProgressDialog loadingBar;
	private int signin_status = 0, signup_status = 0;
	private int byGetOrPost = 0;
	
	HttpPost httppost1;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient1;
    ProgressDialog dialog=null;
    
	/* (non-Javadoc)
	 * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		nameField = (EditText)findViewById(R.id.name);
	    emailField = (EditText)findViewById(R.id.email);
	    usernameField = (EditText)findViewById(R.id.username);
	    passwordField = (EditText)findViewById(R.id.password);
	    mSubmit = (Button)findViewById(R.id.btnSignUp);
	    
		setTouchNClick(R.id.btnSignUp);
		setTouchNClick(R.id.btnFb);
		setTouchNClick(R.id.btnTw);
		setTouchNClick(R.id.btnSignIn);
	}

	/* (non-Javadoc)
	 * @see com.chatt.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId()) {
		   case R.id.btnSignIn:
			  startActivity(new Intent(this, Login.class));
			  finish();
		      break;
		   case R.id.btnSignUp:
			   name = nameField.getText().toString();
			   email = emailField.getText().toString();
			   username = usernameField.getText().toString();
			   password = passwordField.getText().toString();
			   if(username.trim().length() > 0 && password.trim().length() > 0) {
				   if(name.trim().length() > 0 && email.trim().length() > 0) {
	        			// sign up user
	        			 signup();  
	        			 switch(signup_status)  {
	  	        	   
			        	   case 1: 
			        		   // login user
		                       startActivity(new Intent(this, MainActivity.class));
		                       finish();
		                       Toast.makeText(getApplicationContext(), "Welcome to Trystin", Toast.LENGTH_LONG).show();
		                       break;
			        	   case 2:
			        		   new AppAlerts().showErrorDialog(this, "SignUp Error", "Account already exists. Please Sign In" );
			        		   break;
			        	   default:
			        		   new AppAlerts().showErrorDialog(this, "SignUp Error", "an error occured. Please try again.." );
			        		   break;
	        			 }
	        		   } else {
	        			   new AppAlerts().showErrorDialog(this, "SignUp Error..", "All fields are required!" );
	        		   }
				   
			      } else {
       			       new AppAlerts().showErrorDialog(this, "SignUp Error..", "All fields are required!" );
       		      } 
				  break;
			   }
	}
    
    void signup(){
    	AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>(){

            @Override
            protected void onPreExecute(){
            	loadingBar = ProgressDialog.show(Register.this, "", "Signing up, Please wait...", true);
            }

            @Override
            protected String doInBackground(String... args) {
                    try{
                       String link="http://indiainme.com/iosSignup.php";
                       String data  = URLEncoder.encode("name", "UTF-8") 
                       + "=" + URLEncoder.encode(name, "UTF-8");
                       data += "&" + URLEncoder.encode("email", "UTF-8") 
                       + "=" + URLEncoder.encode(email, "UTF-8");
                       data += "&" + URLEncoder.encode("username", "UTF-8") 
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
                   		signup_status = 1;
                   	   } else if(result.contains("Username Exist.")){
                   	    signup_status = 2;
                   	   }	
                       return result;
                       
                    }catch(Exception e){
                       return new String("Exception: " + e.getMessage());
                    }
                 
              }

            @Override
            protected void onPostExecute(String result){
            	loadingBar.dismiss();
            }
            

        };
        task.execute();
        
        while(signup_status == 0) {
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	 		
	 	}
    }
    
    
}
/* 
 * 
  //SignInActivity signin = new SignInActivity(this, 1, username, password);
				   //status = signin.checkLogin();
				   checkLogin();
				   switch(signin_status)  {
	        	   
		        	   case 1: 
		        		   // login user
		                   startActivity(new Intent(this, MainActivity.class));
		                   finish();
		                   Toast.makeText(getApplicationContext(), "Welcome to Trystin", Toast.LENGTH_LONG).show();
		                   break;
		        	   case 2:
		        		   if(name.trim().length() > 0 && email.trim().length() > 0) {
		        			// sign up user
		        			 signup();  
		        			 switch(signup_status)  {
		  	        	   
				        	   case 1: 
				        		   // login user
			                       startActivity(new Intent(this, MainActivity.class));
			                       finish();
			                       Toast.makeText(getApplicationContext(), "Welcome to Trystin", Toast.LENGTH_LONG).show();
			                       
				        		   break;
				        	   case 2:
				        		   new AppAlerts().showErrorDialog(this, "SignUp Error..", "username already exists." );
				        		   break;
				        	   default:
				        		   new AppAlerts().showErrorDialog(this, "SignUp Error..", "user already exists." );
				        		   break;
		        			 }
		        		   } else {
		        			   new AppAlerts().showErrorDialog(this, "SignUp Error..", "All fields are required!" );
		        		   }
		        		   break;
	    	       }
			   }
			   else {
				   new AppAlerts().showErrorDialog(this, "SignUp Error..", "Fields cannot be empty" );
	         	}
			   break;
		   
		   default:
			   startActivity(new Intent(this, MainActivity.class));
			   finish();
			   break;
		}
		*/

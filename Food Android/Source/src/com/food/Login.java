package com.food;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.food.custom.CustomActivity;
/**
 * The Class Login is an Activity class that shows the login screen to users.
 * The current implementation simply start the MainActivity. You can write your
 * own logic for actual login and for login using Facebook and Twitter.
 */
public class Login extends CustomActivity
{
	private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/login.php";
	private EditText usernameField,passwordField;
	private TextView status,role,method;
	private Button mSubmit;
	
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
			   String username = usernameField.getText().toString();
			   String password = passwordField.getText().toString();
			   System.out.println("result: " + new SignInActivity(this, 1).execute(username,password));
			   startActivity(new Intent(this, MainActivity.class));
				 Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
			 
			   finish();	 
			   //new AttemptLogin().execute();
		       break;
		   case R.id.btnReg:
			  startActivity(new Intent(this, Register.class));
			  finish();
	          break;
		   default:
			  startActivity(new Intent(this, MainActivity.class));
			  finish();
			  break;
		 }
	}
	class AttemptLogin extends AsyncTask<String, String, String> {

		 /**
        * Before starting background thread Show Progress Dialog
        * */
		boolean failure = false;
		
       @Override
       protected void onPreExecute() {
       }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
           int success;
           String username = usernameField.getText().toString();
           String password = passwordField.getText().toString();
           try {
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("username", username));
               params.add(new BasicNameValuePair("password", password));

               Log.d("request!", "starting");
               // getting product details by making HTTP request
               HttpClient client = new DefaultHttpClient();
               HttpGet request = new HttpGet();
               request.setURI(new URI(LOGIN_URL));
               HttpResponse response = client.execute(request);
               Log.d("Login attempt", response.toString());

               // if  success
                Intent i = new Intent(Login.this, MainActivity.class);
               	finish();
   				startActivity(i);
               	
           } catch (Exception e) {
               e.printStackTrace();
           }

           return null;
			
		}
		/**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(String file_url) {
       }
		
	}
		 

}


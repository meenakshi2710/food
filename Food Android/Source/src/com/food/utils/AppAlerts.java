package com.food.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AppAlerts {

	public void showErrorDialog(Context context, String title, String message){
		// Prompt user to enter credentials
 	   AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton("Ok",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

            }
        });
	}
}

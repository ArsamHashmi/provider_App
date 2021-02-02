package com.axiomindustries.provider002;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkNotificationListenerPermission();
	}

	private void checkNotificationListenerPermission(){

		if (!Settings.Secure.getString(this.getContentResolver(),
				"enabled_notification_listeners").contains(getApplicationContext().getPackageName())) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			alertDialogBuilder.setTitle("Permission required!");
			alertDialogBuilder
					.setMessage("Application needs Notifications listener permission")
					.setCancelable(false)
					.setPositiveButton("Open Settings", (dialog, id) ->
							startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)));

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
	}
}

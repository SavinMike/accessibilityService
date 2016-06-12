
package com.prototype.applauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.prototype.applauncher.SettingsActivity;
import com.prototype.applauncher.service.AppLauncherService;

import java.util.ArrayList;
import java.util.List;

public class OutgoingReceiver extends BroadcastReceiver
{

	public List<String> mAllowingNumbers = new ArrayList<>();
	private SharedPreferences mSharedPreferences;

	public OutgoingReceiver()
	{
		mAllowingNumbers.add("88002000000");
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (mSharedPreferences == null)
		{
			mSharedPreferences = context.getSharedPreferences(SettingsActivity.TAG, Context.MODE_PRIVATE);
		}
		String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		if (!mAllowingNumbers.contains(number) && mSharedPreferences.getBoolean(SettingsActivity.KEY_LOCK_CALLS, false))
		{
			setResultData(null);
			SettingsActivity.startActivity(context);
		}
		Log.d(AppLauncherService.TAG, "Outgoing: " + number);
	}
}
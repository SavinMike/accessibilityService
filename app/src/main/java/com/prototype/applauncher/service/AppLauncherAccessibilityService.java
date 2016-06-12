package com.prototype.applauncher.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.prototype.applauncher.SettingsActivity;
import com.prototype.applauncher.Utils;

/**
 * Date: 12.06.2016
 * Time: 12:20
 *
 * @author Savin Mikhail
 */
public class AppLauncherAccessibilityService extends AccessibilityService
{
	public static final String TAG = "AppLauncherAccessibili";

	public static String getServiceId(Context context)
	{
		return String.format("%1$s/.service.%2$s", context.getPackageName(), AppLauncherAccessibilityService.class.getSimpleName());
	}

	private SharedPreferences mSharedPreferences;

	@Override
	protected void attachBaseContext(final Context base)
	{
		super.attachBaseContext(base);
		mSharedPreferences = getSharedPreferences(SettingsActivity.TAG, MODE_PRIVATE);
	}

	@Override
	public void onAccessibilityEvent(final AccessibilityEvent event)
	{
		if (mSharedPreferences.getBoolean(SettingsActivity.KEY_LOCK_APPLICATIONS, false))
		{
			if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
			{
				if (event.getPackageName() == null || !Utils.getsAvalibleAppsPackages(getApplicationContext()).contains(event.getPackageName().toString()))
				{
					Log.d(TAG, event.toString());
					SettingsActivity.startActivity(getApplicationContext());
				}
			}
		}
	}

	@Override
	public void onInterrupt()
	{

	}

}

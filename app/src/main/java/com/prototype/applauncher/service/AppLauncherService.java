package com.prototype.applauncher.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.prototype.applauncher.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 12.06.2016
 * Time: 12:20
 *
 * @author Savin Mikhail
 */
public class AppLauncherService extends AccessibilityService
{
	public static final String TAG = "AppLauncherService";

	public static String getServiceId(Context context)
	{
		return String.format("%1$s/.service.%2$s", context.getPackageName(), AppLauncherService.class.getSimpleName());
	}

	private List<String> mAllowedPackageNames = new ArrayList<>();
	private SharedPreferences mSharedPreferences;

	@Override
	protected void attachBaseContext(final Context base)
	{
		super.attachBaseContext(base);
		mAllowedPackageNames.add(getPackageName());
		mAllowedPackageNames.add(getCallerPackageName());
		mAllowedPackageNames.add("com.android.incallui");
		mAllowedPackageNames.add(getLauncherPackageName());
		mSharedPreferences = getSharedPreferences(SettingsActivity.TAG, MODE_PRIVATE);
	}

	private String getLauncherPackageName()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
		String packageName = resolveInfo.activityInfo.packageName;
		Log.d(TAG, packageName);
		return packageName;
	}

	private String getCallerPackageName()
	{
		Intent intent = new Intent(Intent.ACTION_DIAL);
		ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
		String packageName = resolveInfo.activityInfo.packageName;
		Log.d(TAG, "Caller: " + packageName);
		return packageName;
	}

	@Override
	public void onAccessibilityEvent(final AccessibilityEvent event)
	{
		if (mSharedPreferences.getBoolean(SettingsActivity.KEY_LOCK_APPLICATIONS, false))
		{
			if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
			{
				if (event.getPackageName() == null || !mAllowedPackageNames.contains(event.getPackageName().toString()))
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

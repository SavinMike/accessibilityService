package com.prototype.applauncher;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.CompoundButton;

import com.prototype.applauncher.service.ApplicationLauncherService;

import java.util.List;

public class SettingsActivity extends AppCompatActivity
{
	public static final String TAG = "com.prototype.applauncher.SettingsActivity";
	public static final String KEY_LOCK_APPLICATIONS = TAG + ".key_LOCK_APPLICATIONS";
	public static final String KEY_LOCK_CALLS = TAG + ".key_LOCK_CALLS";
	public static final String KEY_ACTION_KILL = TAG + ".key_ACTION_KILL";
	public static final String KEY_ID = TAG + ".key_ID";

	public static void startActivity(Context context)
	{
		Intent intent = new Intent(context, SettingsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(final Context context, final Intent intent)
		{
			if (SettingsActivity.this.hashCode() != intent.getIntExtra(KEY_ID, -1))
			{
				finish();
			}
		}
	};

	private SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		removeOthersSettingsActivity();

		registerReceiver(mBroadcastReceiver, new IntentFilter(KEY_ACTION_KILL));
		mSharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
		SwitchCompat lockApps = (SwitchCompat) findViewById(R.id.activity_main_switch_lock_applications);
		if (lockApps != null)
		{
			lockApps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
				{
					mSharedPreferences.edit().putBoolean(KEY_LOCK_APPLICATIONS, isChecked).apply();
				}
			});
			lockApps.setChecked(mSharedPreferences.getBoolean(KEY_LOCK_APPLICATIONS, false));
		}

		SwitchCompat lockCalls = (SwitchCompat) findViewById(R.id.activity_main_switch_lock_calls);
		if (lockCalls != null)
		{
			lockCalls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked)
				{
					mSharedPreferences.edit().putBoolean(KEY_LOCK_CALLS, isChecked).apply();
				}
			});

			lockCalls.setChecked(mSharedPreferences.getBoolean(KEY_LOCK_CALLS, false));
		}

//		if (!isAccessibilityEnabled(this, AppLauncherAccessibilityService.getServiceId(this)))
//		{
//			if (isSystemApp(getPackageName()))
//			{
//				Settings.Secure.putString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, AppLauncherAccessibilityService.getServiceId(this));
//				Settings.Secure.putString(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, "1");
//				Log.d(AppLauncherAccessibilityService.TAG, "IS USER APP");
//			}
//			else
//			{
//				startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
//			}
//		}

		startService(new Intent(this, ApplicationLauncherService.class));
	}

	private void removeOthersSettingsActivity()
	{
		Intent intent = new Intent(KEY_ACTION_KILL);
		intent.putExtra(KEY_ID, hashCode());
		sendBroadcast(intent);
	}

	@Override
	protected void onDestroy()
	{
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	@SuppressLint("PackageManagerGetSignatures")
	public boolean isSystemApp(String packageName)
	{
		PackageManager packageManager = getPackageManager();
		try
		{
			PackageInfo targetPkgInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			PackageInfo sys = packageManager.getPackageInfo("android", PackageManager.GET_SIGNATURES);
			return (targetPkgInfo != null && targetPkgInfo.signatures != null && sys.signatures[0]
					.equals(targetPkgInfo.signatures[0]));
		}
		catch (PackageManager.NameNotFoundException e)
		{
			return false;
		}
	}

	public static boolean isAccessibilityEnabled(Context context, String id)
	{

		AccessibilityManager am = (AccessibilityManager) context
				.getSystemService(Context.ACCESSIBILITY_SERVICE);

		List<AccessibilityServiceInfo> runningServices = am
				.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
		for (AccessibilityServiceInfo service : runningServices)
		{
			if (id.equals(service.getId()))
			{
				return true;
			}
		}

		return false;
	}
}

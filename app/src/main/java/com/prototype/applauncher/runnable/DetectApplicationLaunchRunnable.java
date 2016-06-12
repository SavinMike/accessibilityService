package com.prototype.applauncher.runnable;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.prototype.applauncher.SettingsActivity;
import com.prototype.applauncher.Utils;
import com.prototype.applauncher.service.AppLauncherAccessibilityService;

import java.util.List;
import java.util.TimerTask;

public class DetectApplicationLaunchRunnable extends TimerTask
{

	private final List<String> mAvalibleAppsPackages;
	private ActivityManager mActivityManager;
	private Handler mHandler;
	private final Context mContext;
	private SharedPreferences mSharedPreferences;

	public DetectApplicationLaunchRunnable(Context context)
	{
		mSharedPreferences = context.getSharedPreferences(SettingsActivity.TAG, Context.MODE_PRIVATE);
		mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		mHandler = new Handler(Looper.getMainLooper());
		mAvalibleAppsPackages = Utils.getsAvalibleAppsPackages(context);
		mContext = context;
	}

	@Override
	public void run()
	{
		if (mSharedPreferences.getBoolean(SettingsActivity.KEY_LOCK_APPLICATIONS, false))
		{
			String topApplication;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH)
			{
				topApplication = getActivePackagesCompat();
			}
			else
			{
				topApplication = getActivePackages();
			}

			if (topApplication != null)
			{
				if (!mAvalibleAppsPackages.contains(topApplication))
				{
					Log.d(AppLauncherAccessibilityService.TAG, topApplication);
					mHandler.post(new Runnable()
					{
						@Override
						public void run()
						{
							SettingsActivity.startActivity(mContext);
						}
					});
				}
			}
		}
	}

	@Nullable
	private String getActivePackages()
	{
		List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(mContext);
		if (processes.isEmpty())
		{
			return null;
		}

		return processes.get(0).getPackageName();
	}

	String getActivePackagesCompat()
	{
		//noinspection deprecation
		final List<ActivityManager.RunningTaskInfo> taskInfo = mActivityManager.getRunningTasks(1);
		final ComponentName componentName = taskInfo.get(0).topActivity;
		return componentName.getPackageName();
	}
}
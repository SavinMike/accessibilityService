package com.prototype.applauncher.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.prototype.applauncher.runnable.DetectApplicationLaunchRunnable;

import java.util.Timer;

/**
 * Date: 12.06.2016
 * Time: 15:04
 *
 * @author Savin Mikhail
 */
public class ApplicationLauncherService extends Service
{
	@Override
	protected void attachBaseContext(final Context base)
	{
		super.attachBaseContext(base);
		new Timer().scheduleAtFixedRate(new DetectApplicationLaunchRunnable(base), 0, 150);
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Nullable
	@Override
	public IBinder onBind(final Intent intent)
	{
		return null;
	}
}

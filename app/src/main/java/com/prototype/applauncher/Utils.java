package com.prototype.applauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 12.06.2016
 * Time: 14:59
 *
 * @author Savin Mikhail
 */
public class Utils
{

	public static List<String> sAvalibleAppsPackages = new ArrayList<>();

	public static List<String> getsAvalibleAppsPackages(Context context)
	{
		if (sAvalibleAppsPackages.isEmpty())
		{
			sAvalibleAppsPackages.add(context.getPackageName());
			sAvalibleAppsPackages.add(getCallerPackageName(context));
			sAvalibleAppsPackages.add("com.android.incallui");
			sAvalibleAppsPackages.add(getLauncherPackageName(context));
		}
		return sAvalibleAppsPackages;
	}

	public static String getLauncherPackageName(Context context)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return resolveInfo.activityInfo.packageName;
	}

	public static String getCallerPackageName(Context context)
	{
		Intent intent = new Intent(Intent.ACTION_DIAL);
		ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return resolveInfo.activityInfo.packageName;
	}
}

package com.michael.flashsocial.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class NavigationUtil {
    public static void navigateActivity(Activity activity, Context context, Class<?> cls , int reqCode, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, reqCode);
    }

    public static void navigateActivity(Fragment fragment, Context context, Class<?> cls, int reqCode, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        fragment.startActivityForResult(intent, reqCode);
    }

    public static void navigateActivity(Activity activity, Context context, Class<?> cls, int reqCode) {
        Intent intent = new Intent(context, cls);
        activity.startActivityForResult(intent, reqCode);
    }

    public static void navigateActivity(Fragment fragment, Context context, Class<?> cls, int reqCode) {
        Intent intent = new Intent(context, cls);
        fragment.startActivityForResult(intent, reqCode);
    }
}

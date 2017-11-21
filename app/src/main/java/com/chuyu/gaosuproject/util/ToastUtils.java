package com.chuyu.gaosuproject.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    private static boolean IS_SHOW = true;

    private ToastUtils() {
        throw new AssertionError();
    }

    public static void show(Context context, int resId) {
        if (IS_SHOW) {
            show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
        }
    }

    public static void show(Context context, int resId, int duration) {
        if (IS_SHOW) {
            show(context, context.getResources().getText(resId), duration);
        }
    }

    public static void show(Context context, CharSequence text) {
        if (IS_SHOW) {
            show(context, text, Toast.LENGTH_SHORT);
        }
    }

    public static void show(Context context, CharSequence text, int duration) {
        if (IS_SHOW) {
            Toast.makeText(context, text, duration).show();
        }
    }

    public static void show(Context context, int resId, Object... args) {
        if (IS_SHOW) {
            show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
        }
    }

    public static void show(Context context, String format, Object... args) {
        if (IS_SHOW) {
            show(context, String.format(format, args), Toast.LENGTH_SHORT);
        }
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        if (IS_SHOW) {
            show(context, String.format(context.getResources().getString(resId), args), duration);
        }
    }

    public static void show(Context context, String format, int duration, Object... args) {
        if (IS_SHOW) {
            show(context, String.format(format, args), duration);
        }
    }
    public static void show(Context context, String text){
        if (IS_SHOW) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }
    public static void showSure(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}

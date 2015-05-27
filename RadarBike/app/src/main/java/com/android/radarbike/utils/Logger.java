package com.android.radarbike.utils;

/**
 * Created by vntalgo on 4/10/2015.
 */
/*
 * Copyright (C) 2014 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */



import android.util.Log;

public class Logger {

    // Log tag
    private static final String TAG = "RadarBike";

    // Debug flag
    private static boolean DEBUG = true;

    /**
     * Enable all the logs
     */
    public static void enable() {
        DEBUG = true;
    }

    public static boolean isDebugOn() {
        return DEBUG;
    }

    /**
     * Disable all the logs
     */
    public static void disable() {
        DEBUG = false;
    }

    /**
     * Verbose logs
     *
     * @param text
     */
    public static void LOGV(String text) {
        if (DEBUG == true) {
            Log.v(TAG, text);
        }
    }

    /**
     * Verbose logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGV(String subtag, String text) {
        if (DEBUG == true) {
            Log.v(TAG, subtag + " " + text);
        }
    }

    /**
     * Debug logs
     *
     * @param text
     */
    public static void LOGD(Object text) {
        if (DEBUG == true) {
            Log.d(TAG, String.valueOf(text.toString()));
        }
    }

    /**
     * Debug logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGD(String subtag, String text) {
        if (DEBUG == true) {
            Log.d(TAG, subtag + " " + text);
        }
    }

    /**
     * Information logs
     *
     * @param text
     */
    public static void LOGI(String text) {
        if (DEBUG == true) {
            Log.i(TAG, text);
        }
    }

    /**
     * Information logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGI(String subtag, String text) {
        if (DEBUG == true) {
            Log.i(TAG, subtag + " " + text);
        }
    }

    /**
     * Warning logs
     *
     * @param text
     */
    public static void LOGW(String text) {
        if (DEBUG == true) {
            Log.w(TAG, text);
        }
    }

    /**
     * Warning logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGW(String subtag, String text) {
        if (DEBUG == true) {
            Log.w(TAG, subtag + " " + text);
        }
    }

    /**
     * Error logs
     *
     * @param text
     */
    public static void LOGE(String text) {
        if (DEBUG == true) {
            Log.e(TAG, text);
        }
    }

    /**
     * Error logs
     *
     * @param subtag
     * @param text
     */
    public static void LOGE(String subtag, String text) {
        if (DEBUG == true) {
            Log.e(TAG, subtag + " " + text);
        }
    }
}


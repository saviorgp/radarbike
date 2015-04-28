package com.android.radarbike.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.radarbike.utils.Constants;

import java.util.Objects;
import java.util.Set;

/**
 * Created by AlexGP on 25/03/2015.
 */
public class Preferences {

   private SharedPreferences prefs;
   private static Preferences prefsInstance;


   private Preferences(Context context){
       prefs = context.getSharedPreferences("RadarBike",Context.MODE_PRIVATE);
   }

   public static Preferences getPreferences(Context context){
       if(prefsInstance == null){
           prefsInstance = new Preferences(context);
       }
       return prefsInstance;
   }

    public void editPreference(final String key, final Object value) {
        SharedPreferences.Editor sharedEditor = prefs.edit();
        if (value instanceof Boolean) {
            sharedEditor.putBoolean(key, Boolean.parseBoolean(value.toString()));
        } else if (value instanceof Integer) {
            sharedEditor.putInt(key, Integer.parseInt(value.toString()));
        } else if (value instanceof Float) {
            sharedEditor.putFloat(key, Float.parseFloat(value.toString()));
        } else if (value instanceof String) {
            sharedEditor.putString(key, String.valueOf(value));
        } else if (value instanceof Long) {
            sharedEditor.putLong(key, Long.parseLong(value.toString()));
        } else if (value instanceof Set) {
            sharedEditor.putStringSet(key, (Set<String>)value);
        } else {
            sharedEditor.remove(key);
        }
        sharedEditor.apply();
    }

    public int getSelectedModePreference(){
        return this.prefs.getInt(Constants.SELECTED_MODE, 0);
    }

    public boolean isPosCheckedOut(){
        return this.prefs.getBoolean(Constants.IS_POS_CHECKED_OUT, false);
    }
}
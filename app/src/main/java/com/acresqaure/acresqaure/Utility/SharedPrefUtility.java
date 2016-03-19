package com.acresqaure.acresqaure.Utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by parasdhawan on 19/03/16.
 */
public class SharedPrefUtility {

    final String MyPREFERENCES_KEY="TelematicsPref";
    SharedPreferences sharedpreferences;

    public SharedPrefUtility(Context context){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public boolean saveString(String DATA_KEY,String data){
        boolean isSavedCorrect=true;
        try {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(DATA_KEY, data);
            editor.commit();

//            String datax = sharedpreferences.getString(DATA_KEY, "");
            //return data;

            isSavedCorrect=true;
        }catch (Exception e){
            isSavedCorrect=false;
            e.printStackTrace();
        }
        return isSavedCorrect;
    }

    public boolean saveBoolean(String DATA_KEY,boolean data){
        boolean isSavedCorrect=true;
        try {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(DATA_KEY, data);
            editor.commit();

//            String datax = sharedpreferences.getString(DATA_KEY, "");
            //return data;

            isSavedCorrect=true;
        }catch (Exception e){
            isSavedCorrect=false;
            e.printStackTrace();
        }
        return isSavedCorrect;
    }

    public String getString(String DATA_KEY,String defaultValue){
        try {
            String data = sharedpreferences.getString(DATA_KEY, defaultValue);
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return defaultValue;
    }

    public boolean getBoolean(String DATA_KEY,boolean defaultValue){
        try {
            boolean data = sharedpreferences.getBoolean(DATA_KEY, defaultValue);
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return defaultValue;
    }

    public boolean hasKey(String DATA_KEY){
        return sharedpreferences.contains(DATA_KEY);
    }

    public void clear()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void remove(String DATA_KEY){


        try{
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(DATA_KEY);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
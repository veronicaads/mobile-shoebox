package id.ac.umn.shoebox;

/**
 * SharedPreference Manager
 */

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    SharedPreferences sharedPreferences;
    Context mContext;
    // shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String PREF_NAME = "sessionPref";
    SharedPreferences.Editor editor;

    public SharedPrefManager (Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    public void saveIsLoggedIn(Context context, Boolean isLoggedIn){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean ("IS_LOGGED_IN", isLoggedIn);
        editor.commit();

    }

    public boolean getISLogged_IN() {
        //mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false);
    }

    public void saveToken(Context context, String toke){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ID_TOKEN", toke);
        editor.commit();
    }

    public String getUserToken(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("ID_TOKEN", "");
    }

    public void saveEmail(Context context, String email){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", email);
        editor.commit();
    }

    public String getUserEmail(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("EMAIL", null);
    }


    public void saveName(Context context, String name){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NAME", name);
        editor.commit();
    }

    public String getName(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("NAME", null);
    }


    public void savePhoto(Context context, String photo){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PHOTO", photo);
        editor.commit();
    }

    public String getpNumber(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("NUMBER", null);
    }

    public void savepNumber(Context context, String pNumber){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NUMBER", pNumber);
        editor.commit();
    }

    public String getpPrivilege(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("PRIVILEGE", null);
    }

    public void savepPrivilege(Context context, String pPriviledge){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PRIVILEGE", pPriviledge);
        editor.commit();
    }

    public String getPhoto(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("PHOTO", null);
    }

    public void saveCabangAdmin(Context context, String cabang){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cabangAdmin", cabang);
        editor.commit();
    }

    public String getCabangAdmin(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString("cabangAdmin", null);
    }

    public void clear(){
        editor.clear();
        editor.apply();
    }
}
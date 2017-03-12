package com.quexten.ulricianumplanner;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Quexten on 04-Mar-17.
 */

public abstract class NewsListener {

    public static final String ACTIVITY_IDENTIFIER = "com.quexten.ulricianumplanner.MainActivity";
    public static final String NEWS_IDENTIFIER = "news";

    private Context context;

    public NewsListener(Context context) {
        this.context = context;
    }

    public abstract void newsReceived(String news);

    public void saveNews(String news) {
        SharedPreferences sharedPref = context.getSharedPreferences(ACTIVITY_IDENTIFIER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(NEWS_IDENTIFIER, news);
        editor.commit();
    }

    public String loadNews() {
        SharedPreferences sharedPref = context.getSharedPreferences(ACTIVITY_IDENTIFIER, Context.MODE_PRIVATE);
        return sharedPref.getString(NEWS_IDENTIFIER, "");
    }

}

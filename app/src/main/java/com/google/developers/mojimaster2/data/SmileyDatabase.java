package com.google.developers.mojimaster2.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.developers.mojimaster2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

@Database(entities = Smiley.class, version = 1)
public abstract class SmileyDatabase extends RoomDatabase {

    public abstract SmileyDao smileyDao();
    static Context mcontext;

    private static volatile SmileyDatabase sInstance = null;

    /**
     * Returns an instance of Room Database.
     *
     * @param context application context
     * @return The singleton SmileyDatabase
     */


    @NonNull
    public static synchronized SmileyDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (SmileyDatabase.class) {
                if (sInstance == null) {
                    //TODO create a database instance and fill with data from JSON
                    mcontext=context;
                    sInstance= Room.databaseBuilder(context.getApplicationContext(),
                            SmileyDatabase.class,"smiley_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return sInstance;
    }

    private static RoomDatabase.Callback callback= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

         new MyAsyncTask().execute();

        }
    };


    private static class MyAsyncTask extends AsyncTask<Void,Void,Void>
    {

        public MyAsyncTask() {
        }

        @Override
        protected Void doInBackground(Void... voids) {

            fillWithDemoData(mcontext);

            return null;
        }

    }

    @WorkerThread
    private static void fillWithDemoData(Context context) {
        SmileyDao dao = getInstance(context).smileyDao();
        JSONArray emoji = loadJsonArray(context);
        try {
            for (int i = 0; i < emoji.length(); i++) {
                JSONObject item = emoji.getJSONObject(i);
                dao.insert(new Smiley(item.getString("code"),
                        item.getString("name"),
                        item.getString("char")));
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private static JSONArray loadJsonArray(Context context) {
        StringBuilder builder = new StringBuilder();
        InputStream in = context.getResources().openRawResource(R.raw.emoji);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONObject json = new JSONObject(builder.toString());
            return json.getJSONArray("emojis");

        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }

        return null;
    }

}

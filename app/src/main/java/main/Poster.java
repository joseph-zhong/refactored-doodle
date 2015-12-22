package com.instantly.app.main;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.instantly.app.common.User;
import com.instantly.app.dataBase.DatabaseConnection;
import com.instantly.app.list_view.History;
import com.instantly.app.map_view.SearchEngine;
import com.instantly.app.map_view.TaskMapViewFragment;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Poster extends AsyncTask<Void, Integer, Void> {

    private User user;
    private String title;
    private String category;
    private int rewards;
    private String description;
    private Context context;

    public Poster(User user, String title, String category, int rewards,
                  String description, Context context) {
        this.user = user;
        this.title = title;
        this.category = category;
        this.rewards = rewards;
        this.description = description;
        this.context = context;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (isSuccessful()) {
            Toast.makeText(context, "You post the task successfully.", Toast.LENGTH_SHORT).show();
            new SearchEngine(TaskMapViewFragment.mCurrentLatLng, TaskMapViewFragment.mGoogleMap, MainActivity.DEFAULT_SEARCH_OPTION).execute();
            new History(user, "poster").execute();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        publishProgress();
        return null;
    }

    private boolean isSuccessful() {
        if (title.length() == 0) {
            Toast.makeText(context, "Title can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.length() == 0) {
            Toast.makeText(context, "Description can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        double latitude = TaskMapViewFragment.mCurrentLatLng.latitude;
        double longitude = TaskMapViewFragment.mCurrentLatLng.longitude;
        HttpResponse h = DatabaseConnection.createReward(user.getID(), title, category,
                rewards, description, latitude, longitude);
        if (h != null) {
            try {
                JSONObject jo = DatabaseConnection.streamToJSONObject(h
                        .getEntity().getContent());
                return true;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}


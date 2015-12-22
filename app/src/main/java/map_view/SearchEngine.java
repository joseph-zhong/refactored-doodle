package com.instantly.app.map_view;

import com.instantly.app.dataBase.DatabaseConnection;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.instantly.app.R;
import com.instantly.app.list_view.TaskListViewFragment;
import com.instantly.app.main.MainActivity;
import com.instantly.app.searchBar.SearchBar;

public class SearchEngine extends AsyncTask<Void, Integer, Void> {

    private LatLng mCurrentLatLng;
    private GoogleMap map;
    private int option;

    public SearchEngine(LatLng mCurrentLatLng, GoogleMap map, int option) {
        this.mCurrentLatLng = mCurrentLatLng;
        this.map = map;
        this.option = option;
    }

    @Override
    protected Void doInBackground(Void... params) {
        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        loadMapData();
    }

    public void loadMapData() {
        HttpResponse h = option == MainActivity.DEFAULT_SEARCH_OPTION ?
                DatabaseConnection.searchNearby(mCurrentLatLng) :
                DatabaseConnection.searchNearby(mCurrentLatLng,
                        SearchBar.category, SearchBar.keyword,
                        SearchBar.distance, SearchBar.time);
        SearchBar.reset();
        JSONArray searchList = null;
        if (h != null) {
            try {
                searchList = DatabaseConnection.streamToJSONArray(h
                        .getEntity().getContent());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        map.clear();
        HashMap<Marker, JSONObject> markerInfo = TaskMapViewFragment.getNewMarkerInfo();
        for (int i = 0; searchList != null && i < searchList.length(); i++) {
            JSONObject result = null;
            LatLng pos = null;
            String title = "";
            try {
                result = searchList.getJSONObject(i);
                pos = new LatLng(result.getDouble("Latitude"), result.getDouble("Longitude"));
                title = result.getString("RewardTitle");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MarkerOptions marker = new MarkerOptions();
            marker.position(pos);
            marker.title(title);
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.dollar));
            Marker mk = map.addMarker(marker);
            markerInfo.put(mk, result);
        }
        ((TaskListViewFragment) MainActivity.mFragments[1]).update();
    }
}


package com.instantly.app.list_view;

import android.os.AsyncTask;

import com.instantly.app.common.User;
import com.instantly.app.dataBase.DatabaseConnection;
import com.instantly.app.list_view.pick.PickHistoryFragment;
import com.instantly.app.list_view.post.PostHistoryFragment;
import com.instantly.app.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class History extends AsyncTask<Void, Integer, Void> {

    private int id;
    private String type;

    public History(User user, String type) {
        this.id = user.getID();
        this.type = type;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        buildHistory();
    }

    @Override
    protected Void doInBackground(Void... params) {
        publishProgress();
        return null;
    }

    private void buildHistory() {
        JSONArray list = DatabaseConnection.getHistory(type, id);
        boolean isPostHistory = type.equals("poster");
        if (isPostHistory) {
            PostHistoryFragment.mContentItems = new ArrayList<JSONObject>();
        } else {
            PickHistoryFragment.mContentItems = new ArrayList<JSONObject>();
        }
        for (int i = 0; i < list.length(); i++) {
            try {
                if (isPostHistory) {
                    PostHistoryFragment.mContentItems.add(list.getJSONObject(i));
                } else {
                    PickHistoryFragment.mContentItems.add(list.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (isPostHistory) {
            ((PostHistoryFragment) MainActivity.mFragments[3]).update();
        } else {
            ((PickHistoryFragment) MainActivity.mFragments[2]).update();
        }
    }
}

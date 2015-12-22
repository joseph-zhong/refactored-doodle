package common;

import android.os.AsyncTask;

import dataBase.DatabaseConnection;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Complete extends AsyncTask<Void, Integer, Void> {

    private String role;
    private int rewardID;

    public Complete(String role, int rewardID) {
        this.role = role;
        this.rewardID = rewardID;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        HttpResponse h = DatabaseConnection.completeTask(role, rewardID);
        if (h != null) {
            try {
                JSONObject jo = DatabaseConnection.streamToJSONObject(h
                        .getEntity().getContent());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        publishProgress();
        return null;
    }
}

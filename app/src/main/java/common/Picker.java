package common;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.instantly.app.dataBase.DatabaseConnection;
import com.instantly.app.list_view.History;
import com.instantly.app.main.MainActivity;
import com.instantly.app.map_view.SearchEngine;
import com.instantly.app.map_view.TaskMapViewFragment;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Picker extends AsyncTask<Void, Integer, Void> {

    private int rewardID;
    private int posterID;
    private User user;
    private Context context;

    public Picker(int rewardID, int posterID, User user, Context context) {
        this.rewardID = rewardID;
        this.posterID = posterID;
        this.user = user;
        this.context = context;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (isPickable()) {
            Toast.makeText(context, "You pick the task successfully.", Toast.LENGTH_SHORT).show();
            new SearchEngine(TaskMapViewFragment.mCurrentLatLng, TaskMapViewFragment.mGoogleMap, MainActivity.DEFAULT_SEARCH_OPTION).execute();
            new History(user, "picker").execute();
        } else {
            Toast.makeText(context, "You can't pick the task posted by yourself.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        publishProgress();
        return null;
    }

    private boolean isPickable() {
        if (posterID == user.getID()) return false;
        HttpResponse h = DatabaseConnection.pickReward(rewardID, user.getID());
        if (h != null) {
            try {
                JSONObject jo = DatabaseConnection.streamToJSONObject(h
                        .getEntity().getContent());
                if (jo.getString("type").equals("error")) return false;
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



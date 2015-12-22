package list_view.post;

import android.os.AsyncTask;

import com.instantly.app.dataBase.DatabaseConnection;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PickerRater extends AsyncTask<Void, Integer, Void> {
    private int rewardID;
    private int rate;

    public PickerRater(int rewardID, int rate) {
        this.rewardID = rewardID;
        this.rate = rate;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        HttpResponse h = DatabaseConnection.ratePicker(rewardID, rate);
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

package login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.instantly.app.dataBase.DatabaseConnection;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginAuth extends AsyncTask<Void, Integer, Void> {
    private String email;
    private String pwd;
    private Context context;
    private LoginActivity activity;

    public LoginAuth(String email, String pwd, Context context, LoginActivity activity) {
        this.email = email;
        this.pwd = pwd;
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        JSONObject info = getUserInfo();
        SharedPreferences sp = context.getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (info == null) {
            editor.putString("email", null);
            editor.putString("password", null);
            Toast.makeText(context, "Wrong username or wrong password.", Toast.LENGTH_SHORT).show();
        } else {
            editor.putString("email", email);
            editor.putString("password", pwd);
            int id = -1;
            int rating = -1;
            int credit = 100;
            String username = null;
            String contact = null;
            try {
                JSONObject jo = info.getJSONObject("value");
                id = jo.getInt("CustomerID");
                rating = jo.getInt("Rating");
                credit = jo.getInt("Credit");
                username = jo.getString("UserName");
                contact = jo.getString("Contact");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editor.putInt("id", id);
            editor.putInt("rating", rating);
            editor.putInt("credit", credit);
            editor.putString("username", username);
            editor.putString("contact", contact);
            editor.commit();
            activity.reload();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        publishProgress();
        return null;
    }

    private JSONObject getUserInfo() {
        HttpResponse h = DatabaseConnection.login(email, pwd);
        if (h != null) {
            try {
                JSONObject jo = DatabaseConnection.streamToJSONObject(h
                        .getEntity().getContent());
                if (jo.getString("type").equals("error")) return null;
                return jo;
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
}

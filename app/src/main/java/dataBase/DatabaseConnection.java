package dataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class DatabaseConnection {
    private static final String API_ROOT = "http://54.86.169.6/hunttask/";

    public static JSONArray getHistory(String type, int id) {
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        String URL = "";
        int numberOfResults = 20;
        try {
            URL += API_ROOT + "search_reward_by_customerid_api.php?json=" +
                    URLEncoder.encode("{\"customerid\":" + id
                            + ",\"number\":" + numberOfResults
                            + ",\"type\":\"" + type + "\"}", "utf-8");
            request.setURI(new URI(URL));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            response = client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) try {
            return streamToJSONArray(response.getEntity().getContent());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public static HttpResponse ratePicker(int rewardID, int rate) {
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        String URL = "";
        try {
            URL += API_ROOT + "rate_picker_api.php?json=" +
                    URLEncoder.encode("{\"rewardid\":" + rewardID
                            + ",\"rating\":" + rate + "}", "utf-8");
            request.setURI(new URI(URL));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse completeTask(String role, int rewardID) {
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        String URL = "";
        try {
            URL += API_ROOT + "complete_reward_api.php?json=" +
                    URLEncoder.encode("{\"role\":"
                            + "\"" + role + "\""
                            + ",\"rewardid\":"
                            + rewardID + "}", "utf-8");
            request.setURI(new URI(URL));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse login(String email, String pwd) {
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        String URL = "";
        try {
            URL += API_ROOT + "login_api.php?json=" +
                    URLEncoder.encode("{\"emailaddress\":"
                            + "\"" + email + "\""
                            + ",\"userpassword\":"
                            + "\"" + pwd + "\"}", "utf-8");
            request.setURI(new URI(URL));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse createReward(int customerID, String title,
                                            String category, int rewards, String description,
                                            double latitude, double longitude) {
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        String URL = "";
        try {
            URL += API_ROOT + "create_reward_api.php?json=" +
                    URLEncoder.encode("{\"customerid\":"
                            + customerID
                            + ",\"rewardtitle\":"
                            + "\"" + title + "\""
                            + ",\"category\":"
                            + "\"" + category + "\""
                            + ",\"rewards\":"
                            + "\"" + rewards + "\""
                            + ",\"description\":"
                            + "\"" + description + "\""
                            + ",\"latitude\":"
                            + latitude
                            + ",\"longitude\":"
                            + longitude + "}", "utf-8");
            request.setURI(new URI(URL));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse pickReward(int rewardID, int id) {
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        String URL = "";
        try {
            URL += API_ROOT + "pick_reward_api.php?json=" +
                    URLEncoder.encode("{\"rewardid\":"
                            + rewardID
                            + ",\"pickerid\":"
                            + id + "}", "utf-8");
            request.setURI(new URI(URL));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            response = client.execute(request);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse searchNearby(LatLng mCurrentLatLng) {
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        double longitude = mCurrentLatLng.longitude;
        double latitude = mCurrentLatLng.latitude;
        int width = -1;
        int height = -1;
        String URL = "";
        try {
            URL += API_ROOT + "map/search_rewards_in_box_api.php?json=" +
                    URLEncoder.encode("{\"longitude\":" + longitude
                            + ",\"latitude\":" + latitude
                            + ",\"width\":" + width
                            + ",\"height\":" + height + "}", "utf-8");
            request.setURI(new URI(URL));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            response = client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse searchNearby(LatLng mCurrentLatLng, String category, String keyword, int distance, long time) {
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        double longitude = mCurrentLatLng.longitude;
        double latitude = mCurrentLatLng.latitude;
        int width = distance;
        int height = distance;
        long timeInSecond = time * 60;
        if (time == -1) {
            timeInSecond = -1;
        }
        String URL = "";
        if (category.equals("All")) {
            category = "";
        }
        try {
            URL += API_ROOT + "search_reward_by_filter_api.php?json=" +
                    URLEncoder.encode("{" + "\"category\":\"" + category + "\""
                            + ",\"keyword\":\"" + keyword + "\""
                            + ",\"timeinterval\":" + timeInSecond
                            + ",\"longitude\":" + longitude
                            + ",\"latitude\":" + latitude
                            + ",\"width\":" + width
                            + ",\"height\":" + height + "}", "utf-8");
            request.setURI(new URI(URL));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            response = client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static JSONArray streamToJSONArray(InputStream in)
            throws IOException, JSONException {
        if (in == null)
            return new JSONArray();
        char[] buffer = new char[1024];
        Writer strWt = new StringWriter();
        Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"),
                1024);
        int count = reader.read(buffer);
        while (count != -1) {
            strWt.write(buffer, 0, count);
            count = reader.read(buffer);
        }
        in.close();
        return new JSONArray(strWt.toString());
    }

    public static JSONObject streamToJSONObject(InputStream in)
            throws IOException, JSONException {
        if (in == null)
            return new JSONObject();
        char[] buffer = new char[1024];
        Writer strWt = new StringWriter();
        Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"),
                1024);
        int count = reader.read(buffer);
        while (count != -1) {
            strWt.write(buffer, 0, count);
            count = reader.read(buffer);
        }
        in.close();
        return new JSONObject(strWt.toString());
    }
}

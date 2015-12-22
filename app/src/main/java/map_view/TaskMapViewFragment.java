package com.instantly.app.map_view;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.instantly.app.R;
import com.instantly.app.common.DialogBuilder;
import com.instantly.app.common.Picker;
import com.instantly.app.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

@SuppressLint("ValidFragment")
public class TaskMapViewFragment extends Fragment implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "TaskMapViewFragment";
    private static final int MAP_ZOOM_LEVEL = 13;
    private static View mCachedView;
    public static GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderApi mProviderApi = LocationServices.FusedLocationApi;
    private LocationRequest mLocationRequest;
    public static LatLng mCurrentLatLng;
    private static HashMap<Marker, JSONObject> markerInfo;
    private MainActivity activity;

    public TaskMapViewFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        if (mCachedView == null) {
            try {
                mCachedView = inflater.inflate(R.layout.fragment_task_map, container, false);
            } catch (InflateException e) {
                e.printStackTrace();
            }

        } else {
            ViewGroup parent = (ViewGroup) mCachedView.getParent();
            if (parent != null)
                parent.removeView(mCachedView);
        }

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));

        // Creating an instance for being able to interact with Google Map
        SupportMapFragment fm = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map);
        mGoogleMap = fm.getMap();
        mGoogleMap.setMyLocationEnabled(true);

        // Location client and latLng request object
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Camera change listener for direction arrow
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                activity.closeFloatingMenu();
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                activity.closeFloatingMenu();
            }
        });

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final JSONObject item = markerInfo.get(marker);
                DialogBuilder.buildTaskDetailDialog(item, getActivity(), new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        try {
                            new Picker(item.getInt("RewardID"), item.getInt("CustomerID"), activity.getUser(), TaskMapViewFragment.this.getActivity().getApplicationContext()).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
                return true;
            }
        });

        return mCachedView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mCurrentLatLng == null) {
            mCurrentLatLng = MapUtil.toLatLng(location);
            setCamera(mCurrentLatLng, MAP_ZOOM_LEVEL, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    new SearchEngine(mCurrentLatLng, mGoogleMap, MainActivity.DEFAULT_SEARCH_OPTION).execute();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Sets camera position.
     */
    private void setCamera(LatLng latLng, int zoomLevel, GoogleMap.CancelableCallback callback) {
        CameraUpdate cameraUpdate;
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
        mGoogleMap.animateCamera(cameraUpdate, callback);
    }

    public static HashMap<Marker, JSONObject> getNewMarkerInfo() {
        markerInfo = new HashMap<Marker, JSONObject>();
        return markerInfo;
    }

    public static HashMap<Marker, JSONObject> getMarkerInfo() {
        if (markerInfo != null) {
            return markerInfo;
        }
        markerInfo = new HashMap<Marker, JSONObject>();
        return markerInfo;
    }
}

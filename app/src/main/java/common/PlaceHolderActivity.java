package common;


import android.app.Activity;
import android.os.Bundle;

import com.instantly.app.R;

public class PlaceHolderActivity extends Activity {

    private static final String TAG = "PlaceHolderActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeholder);
    }

}

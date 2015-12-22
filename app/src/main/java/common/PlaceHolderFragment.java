package common;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joseph.criko.R;


public class PlaceHolderFragment extends Fragment {

    private static final String TAG = "PlaceHolderFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);

        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(TAG);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Place holder fragment");

        return view;
    }
}

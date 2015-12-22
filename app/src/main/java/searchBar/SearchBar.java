package com.instantly.app.searchBar;

import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.instantly.app.R;

public class SearchBar extends MaterialDialog {
    public static String keyword = "";
    public static String category = "All";
    public static int distance = 1;
    public static long time = 1;
    private SeekBar categoryBar;
    private TextView categoryValue;
    private SeekBar distanceBar;
    private TextView distanceValue;
    private SeekBar timeBar;
    private TextView timeValue;


    public SearchBar(Builder builder) {
        super(builder);
    }

    public void buildBar() {
        categoryBar = (SeekBar)findViewById(R.id.search_categoryBar);
        categoryValue = (TextView)findViewById(R.id.search_categoryValue);
        categoryValue.setText(category);
        categoryBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress < 100) {
                    category = "All";
                } else if (progress < 200) {
                    category = "Babysitter";
                } else if (progress < 300) {
                    category = "Delivery";
                } else if (progress < 400) {
                    category = "Handyman";
                } else if (progress < 500) {
                    category = "Tools";
                } else if (progress < 600) {
                    category = "Tutoring";
                } else {
                    category = "Others";
                }
                categoryValue.setText(category);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        distanceBar = (SeekBar)findViewById(R.id.search_distanceBar);
        distanceValue = (TextView)findViewById(R.id.search_distanceValue);
        distanceValue.setText("Within " + distance + "mile");
        distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance = progress / 10 + 1;
                String unit = " mile";
                if (progress / 10 + 1 != 1) {
                    unit += "s";
                }
                if (progress == 999) {
                    distance = -1;
                    distanceValue.setText("No Max");
                } else {
                    distanceValue.setText("Within " + distance + unit);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        timeBar = (SeekBar)findViewById(R.id.search_timeBar);
        timeValue = (TextView)findViewById(R.id.search_timeValue);
        timeValue.setText("Within 0 day 0 hr " + time + "min");
        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time = progress + 1;
                String dayUnit = " day";
                String hourUnit = " hr";
                String minuteUnit = " min";
                int day = (progress + 1) / 1440;
                int hour = (progress + 1 - day*1440) / 60;
                int minute = (progress + 1) % 60;
                if(day > 1) {
                    dayUnit += "s";
                }
                if(hour > 1) {
                    hourUnit += "s";
                }
                if(minute > 1) {
                    minuteUnit += "s";
                }
                if(progress == 10079) {
                    time = -1;
                    timeValue.setText("No Max");
                } else {
                    timeValue.setText("Within " + day + dayUnit + " " + hour + hourUnit + " " + minute + minuteUnit);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public static void reset() {
        keyword = "";
        category = "All";
        distance = 1;
        time = 1;
    }
}

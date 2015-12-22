package com.instantly.app.main;

import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.instantly.app.R;
import com.instantly.app.common.User;

public class TaskCreator implements View.OnClickListener {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MainActivity activity;
    private SeekBar bountyBar;
    private TextView bountyValue;
    private SeekBar categoryBar;
    private TextView categoryValue;
    private String category;
    private int rewards;
    private User user;
    private int categoryBarValue;

    public TaskCreator(User user, MainActivity activity, NavigationDrawerFragment mNavigationDrawerFragment) {
        this.user = user;
        this.activity = activity;
        this.mNavigationDrawerFragment = mNavigationDrawerFragment;
        initialization();
    }

    private void initialization() {
        rewards = 0;
        categoryBarValue = 0;
        category = "Babysitter";
    }

    @Override
    public void onClick(View v) {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(R.string.new_task_title)
                .customView(R.layout.dialog_create_task, true)
                .positiveText(R.string.create)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        String title = ((EditText) dialog.findViewById(R.id.taskTitle)).getText().toString();
                        String description = ((EditText) dialog.findViewById(R.id.description)).getText().toString();
                        new Poster(user, title, category, rewards, description, activity).execute();
                        initialization();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        initialization();
                    }

                }).build();
        activity.closeFloatingMenu();
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
        }
        bountyBar = (SeekBar) dialog.findViewById(R.id.bounty);
        bountyValue = (TextView) dialog.findViewById(R.id.bountyValue);
        bountyValue.setText("$" + rewards);
        bountyBar.setProgress(rewards);
        bountyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rewards = progress;
                bountyValue.setText("$" + rewards);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        categoryBar = (SeekBar) dialog.findViewById(R.id.category);
        categoryValue = (TextView) dialog.findViewById(R.id.categoryValue);
        categoryValue.setText(category);
        categoryBar.setProgress(categoryBarValue);
        categoryBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                categoryBarValue = progress;
                if (progress < 167) {
                    category = "Babysitter";
                } else if (progress < 334) {
                    category = "Delivery";
                } else if (progress < 500) {
                    category = "Handyman";
                } else if (progress < 666) {
                    category = "Tools";
                } else if (progress < 832) {
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

        dialog.show();
    }
}

package com.instantly.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.joseph.criko.R;

import common.Util;
import main.MainActivity;

public class ProfileActivity extends ActionBarActivity {

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        String profileUsername = intent.getStringExtra("username");
        String profileContact = intent.getStringExtra("contact");
        int profileCredit = intent.getIntExtra("credit", -1);
        int profileRating = intent.getIntExtra("rating", -1);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView username = (TextView) findViewById(R.id.profile_username);
        username.setText(profileUsername);
        TextView contact = (TextView) findViewById(R.id.profile_contact);
        contact.setText(profileContact);
        TextView ratingScore = (TextView) findViewById(R.id.profile_rating);
        if (profileRating == -1) {
            ratingScore.setText("No record");
        } else {
            ratingScore.setText(profileRating + "");
        }
        TextView credit = (TextView) findViewById(R.id.profile_credit);
        credit.setText(profileCredit + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                handleBackAction();
                return true;
            case R.id.action_logout:
                Util.logout(this);
//                SharedPreferences sp = getApplicationContext()
//                        .getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.clear();
//                editor.commit();
//                Intent intent = new Intent(ProfileActivity.this,
//                        LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        handleBackAction();
    }

    private void handleBackAction() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        int startFragmentIndex = getIntent().getIntExtra(MainActivity.EXTRA_CURRENT_FRAGMENT_INDEX, 0);
        returnIntent.putExtra(MainActivity.EXTRA_CURRENT_FRAGMENT_INDEX, startFragmentIndex);
        finish();
    }
}

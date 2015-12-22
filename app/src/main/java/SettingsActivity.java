package com.instantly.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.joseph.criko.R;

import common.Util;
import main.MainActivity;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
//                Intent intent = new Intent(SettingsActivity.this,
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

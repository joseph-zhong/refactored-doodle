package main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.instantly.app.login.LoginActivity;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.instantly.app.ProfileActivity;
import com.instantly.app.SettingsActivity;
import com.instantly.app.common.User;
import com.instantly.app.common.Util;
import com.instantly.app.list_view.History;
import com.instantly.app.list_view.pick.PickHistoryFragment;
import com.instantly.app.list_view.post.PostHistoryFragment;
import com.instantly.app.list_view.TaskListViewFragment;
import com.instantly.app.map_view.SearchEngine;
import com.instantly.app.map_view.TaskMapViewFragment;
import com.instantly.app.R;
import com.instantly.app.searchBar.SearchBar;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallback {

    public static final String EXTRA_CURRENT_FRAGMENT_INDEX = "extra_current_fragment_index";
    public static final int DEFAULT_SEARCH_OPTION = 0;
    public static final int FILTER_SEARCH_OPTION = 1;
    private static final String TAG = "MainActivity";
    private static final int SETTINGS_REQUEST_CODE = 12;
    private static final int PROFILE_REQUEST_CODE = 13;
    private static final int TASK_MAP_VIEW_INDEX = 0;
    private static final int TASK_LIST_VIEW_INDEX = 1;

    private User user;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private FloatingActionMenu mFloatingMenu;
    public static Fragment[] mFragments;
    private int mCurrentFragmentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        int rating = intent.getIntExtra("rating", 0);
        int credit = intent.getIntExtra("credit", 100);
        String username = intent.getStringExtra("username");
        String contact = intent.getStringExtra("contact");
        user = new User(id, rating, credit, username, contact);
        initialization();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE || requestCode == PROFILE_REQUEST_CODE) {
            int fragmentIndex = data.getIntExtra(EXTRA_CURRENT_FRAGMENT_INDEX, TASK_MAP_VIEW_INDEX);
            mNavigationDrawerFragment.selectItemFromOutside(fragmentIndex);
            getFragmentManager().beginTransaction().replace(R.id.container, mFragments[fragmentIndex]).commit();
        }
    }

    private void initialization() {
        buildToolBar();
        buildNavigation();
        buildCircularMenu();
    }

    private void buildToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
    }

    private void buildNavigation() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        mNavigationDrawerFragment.setUserData(user.getUsername(), user.getContact(), BitmapFactory.decodeResource(getResources(), R.drawable.avatar));
        // Set up fragments
        mFragments = new Fragment[4];
        mFragments[0] = new TaskMapViewFragment(MainActivity.this);
        mFragments[1] = new TaskListViewFragment(user);
        mFragments[2] = new PickHistoryFragment(user);
        mFragments[3] = new PostHistoryFragment(user, MainActivity.this);
        mCurrentFragmentIndex = TASK_MAP_VIEW_INDEX;
        getFragmentManager().beginTransaction().replace(R.id.container, mFragments[mCurrentFragmentIndex]).commit();
        new History(user, "picker").execute();
        new History(user, "poster").execute();
    }

    /**
     * Builds circular menu.
     */
    private void buildCircularMenu() {
        // Set up button on the lower right corner with default parameter
        final ImageView buttonMain = new ImageView(this);
        buttonMain.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important_light));

        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.button_action_blue_selector)
                .setContentView(buttonMain)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        int buttonSize = getResources().getDimensionPixelSize(R.dimen.circular_action_button_size);
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(buttonSize, buttonSize);
        rLSubBuilder.setLayoutParams(blueParams);

        ImageView buttonNew = new ImageView(this);
        ImageView buttonMap = new ImageView(this);
        ImageView buttonList = new ImageView(this);

        buttonNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new_light));
        buttonMap.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_map_light));
        buttonList.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_view_as_list_light));

        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        mFloatingMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(buttonNew).build())
                .addSubActionView(rLSubBuilder.setContentView(buttonMap).build())
                .addSubActionView(rLSubBuilder.setContentView(buttonList).build())
                .attachTo(rightLowerButton)
                .build();

        // Listen menu open and close events to animate the button content view
        mFloatingMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                buttonMain.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(buttonMain, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                buttonMain.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(buttonMain, pvhR);
                animation.start();
            }
        });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentFragmentIndex = TASK_MAP_VIEW_INDEX;
                mNavigationDrawerFragment.selectItemFromOutside(TASK_MAP_VIEW_INDEX);
                if (mNavigationDrawerFragment.isDrawerOpen()) {
                    mNavigationDrawerFragment.closeDrawer();
                }
                closeFloatingMenu();
                getFragmentManager().beginTransaction().replace(R.id.container, mFragments[TASK_MAP_VIEW_INDEX]).commit();
            }
        });
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentFragmentIndex = TASK_LIST_VIEW_INDEX;
                mNavigationDrawerFragment.selectItemFromOutside(TASK_LIST_VIEW_INDEX);
                if (mNavigationDrawerFragment.isDrawerOpen()) {
                    mNavigationDrawerFragment.closeDrawer();
                }
                closeFloatingMenu();
                getFragmentManager().beginTransaction().replace(R.id.container, mFragments[TASK_LIST_VIEW_INDEX]).commit();

            }
        });

        buttonNew.setOnClickListener(new TaskCreator(user, MainActivity.this, mNavigationDrawerFragment));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if (mFragments == null) {
            return;
        }

        Intent intent = null;
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                mCurrentFragmentIndex = position;
                getFragmentManager().beginTransaction().replace(R.id.container, mFragments[position]).commit();
                break;
            case 4:
                intent = new Intent(this, ProfileActivity.class);
                intent.putExtra(EXTRA_CURRENT_FRAGMENT_INDEX, mCurrentFragmentIndex);
                intent.putExtra("username", user.getUsername());
                intent.putExtra("contact", user.getContact());
                intent.putExtra("credit", user.getCredit());
                intent.putExtra("rating", user.getRating());
                startActivityForResult(intent, PROFILE_REQUEST_CODE);
                break;
            case 5:
                intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(EXTRA_CURRENT_FRAGMENT_INDEX, mCurrentFragmentIndex);
                startActivityForResult(intent, SETTINGS_REQUEST_CODE);
                break;
            default:
                Log.e(TAG, "invalid fragment position: " + position);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        closeFloatingMenu();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        closeFloatingMenu();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title(R.string.search)
                    .customView(R.layout.dialog_search, true)
                    .positiveText(getString(R.string.search))
                    .negativeText(getString(android.R.string.cancel))
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            new SearchEngine(TaskMapViewFragment.mCurrentLatLng, TaskMapViewFragment.mGoogleMap, FILTER_SEARCH_OPTION).execute();
                        }
                    });
            SearchBar dialog = new SearchBar(builder);
            dialog.buildBar();
            dialog.show();
            return true;
        }

        if (id == R.id.action_logout) {
            Util.logout(this);
//            SharedPreferences sp = getApplicationContext()
//                    .getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sp.edit();
//            editor.clear();
//            editor.commit();
//            Intent intent = new Intent(MainActivity.this,
//                    LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Close floating menu with animation.
     */
    public void closeFloatingMenu() {
        if (mFloatingMenu.isOpen()) {
            mFloatingMenu.close(true);
        }
    }

    public User getUser() {
        if(user == null) {
            Log.e("login error", "no user");
            return new User(-1, -1, -1, "", "");
        }
        return user;
    }
}

package login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.view.Menu;

import com.instantly.app.R;
import com.instantly.app.main.MainActivity;

public class LoginActivity extends Activity {

    private EditText usernameEditText = null;
    private EditText passwordEditText = null;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        usernameEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        usernameEditText.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        passwordEditText.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = usernameEditText.getText().toString();
                String passWord = passwordEditText.getText().toString();
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
                new LoginAuth(userName, passWord, view.getContext(), LoginActivity.this).execute();
            }
        });
        cacheData(this.findViewById(R.id.root).getContext());
    }

    public void cacheData(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginSaved", Context.MODE_PRIVATE);
        String email = sp.getString("email", null);
        String password = sp.getString("password", null);
        if (email != null && password != null) {
            int id = sp.getInt("id", -1);
            int rating = sp.getInt("rating", -1);
            int credit = sp.getInt("credit", 100);
            String username = sp.getString("username", "");
            String contact = sp.getString("contact", "");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("rating", rating);
            intent.putExtra("credit", credit);
            intent.putExtra("username", username);
            intent.putExtra("contact", contact);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void reload() {
        finish();
        startActivity(getIntent());
    }
}


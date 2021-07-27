package com.amitvikram.smartstudyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, BookFragment.OnFragmentInteractionListener, VideoFragment.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener {
    Intent intent, intent1;
    BottomNavigationView bottomNavigationView;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        intent = new Intent(MainActivity.this, LoginActivity.class);
        intent1 = getIntent();
        final HomeFragment homeFragment = new HomeFragment();
        final VideoFragment videoFragment = new VideoFragment();
        final BookFragment bookFragment = new BookFragment();
        final UserFragment userFragment = new UserFragment();
        setFragment(homeFragment);
        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_book:
                        setFragment(bookFragment);
                        return true;
                    case R.id.nav_video:
                        setFragment(videoFragment);
                        return true;
                    case R.id.nav_user:
                        setFragment(userFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_uploads) {
            //redirect on upload activity
            Intent intent = new Intent(MainActivity.this, UploadTopicActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu ) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        MenuItem upload = menu.findItem(R.id.action_uploads);
        upload.setVisible(false);
        HashMap<String, String> user = sessionManager.getUserDetail();
        if (user.get(SessionManager.USERTYPE).equals("teacher")) {
            upload.setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
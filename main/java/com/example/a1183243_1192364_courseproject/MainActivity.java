package com.example.a1183243_1192364_courseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Switch;

import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.SearchView;
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load theme preference
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("isDarkMode", false);

        if (isDarkMode) {
            setTheme(R.style.Theme_1183243_1192364_CourseProject_Dark);
        } else {
            setTheme(R.style.Theme_1183243_1192364_CourseProject_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure the theme toggle switch
        Switch themeToggle = findViewById(R.id.theme_toggle);
        themeToggle.setChecked(isDarkMode);
        themeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the new theme preference
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isDarkMode", isChecked);
            editor.apply();

            // Restart the activity to apply the new theme
            recreate();
        });

        // Other initialization code
        setupNavigationDrawer();
        setupSearchView();
        setAnimatedBackground();

    }

    private void setupNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Navigation menu item click handling
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_today:
                    loadFragment(new TodayTasksFragment());
                    break;
                case R.id.nav_new:
                    startActivity(new Intent(MainActivity.this, CreateTaskActivity.class));
                    break;
                case R.id.nav_all_tasks:
                    loadFragment(new AllTasksFragment());
                    break;
                case R.id.nav_completed:
                    loadFragment(new CompletedTasksFragment());
                    break;
                case R.id.nav_search:
                    loadFragment(new SearchFragment());
                    break;
                case R.id.nav_profile:
                    loadFragment(new ProfileFragment());
                    break;
                case R.id.nav_restapi:
                    loadFragment(new RestApi());
                    break;
                case R.id.nav_logout:
                    loadFragment(new LogoutFragment());
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void setAnimatedBackground() {
        if (drawerLayout == null) return;

        TypedValue typedValue = new TypedValue();
        boolean resolved = getTheme().resolveAttribute(R.attr.backgroundAnimation, typedValue, true);

        if (resolved && typedValue.resourceId != 0) {
            drawerLayout.setBackgroundResource(typedValue.resourceId);

            // Start the animation if it's an AnimationDrawable
            if (drawerLayout.getBackground() instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) drawerLayout.getBackground();
                animationDrawable.setEnterFadeDuration(1000);
                animationDrawable.setExitFadeDuration(1000);
                animationDrawable.start();
            }
        } else {
            Log.e("MainActivity", "Failed to resolve backgroundAnimation attribute");
        }
    }

}


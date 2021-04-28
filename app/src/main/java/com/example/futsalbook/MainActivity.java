package com.example.futsalbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.futsalbook.fragments.BookingsFragment;
import com.example.futsalbook.fragments.BookmarksFragment;
import com.example.futsalbook.fragments.HomeFragment;
import com.example.futsalbook.fragments.ProfileFragment;
import com.example.futsalbook.fragments.TournamentsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottom_navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_navigation_view = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottom_navigation_view.setOnNavigationItemSelectedListener(nav_listener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.notifications){
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav_listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                    Fragment selected_fragment = null;

                    getSupportFragmentManager().addOnBackStackChangedListener(
                            new FragmentManager.OnBackStackChangedListener() {
                                public void onBackStackChanged() {
                                    Fragment current = getCurrentFragment();
                                    if (current instanceof HomeFragment) {
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
                                    }
                                    else if(current instanceof TournamentsFragment) {
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_tournaments).setChecked(true);
                                    }
                                    else if (current instanceof BookingsFragment){
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_bookings).setChecked(true);
                                    }
                                    else if (current instanceof BookmarksFragment){
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_bookmarks).setChecked(true);
                                    }
                                    else if (current instanceof ProfileFragment){
                                        bottom_navigation_view.getMenu().findItem(R.id.nav_profile).setChecked(true);
                                    }
                                }
                            });

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selected_fragment = new HomeFragment();
                            break;
                        case R.id.nav_tournaments:
                            selected_fragment = new TournamentsFragment();
                            break;
                        case R.id.nav_bookings:
                            selected_fragment = new BookingsFragment();
                            break;
                        case R.id.nav_bookmarks:
                            selected_fragment = new BookmarksFragment();
                            break;
                        case R.id.nav_profile:
                            selected_fragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).addToBackStack(null).commit();
                    return true;

                }
            };

    public Fragment getCurrentFragment() {
        return this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment current = getCurrentFragment();
        if (manager.getBackStackEntryCount() >= 1 && !(current instanceof HomeFragment)) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.

            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // clear backstack first
            FragmentTransaction transaction = manager.beginTransaction();
            if (current instanceof HomeFragment) {
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            else if(current instanceof TournamentsFragment) {
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            else if (current instanceof BookingsFragment){
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            else if (current instanceof BookmarksFragment){
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            else if (current instanceof ProfileFragment){
                bottom_navigation_view.getMenu().findItem(R.id.nav_home).setChecked(true);
            }
            transaction.replace(R.id.fragment_container, new HomeFragment());
            transaction.commit();

        } else {
            //super.onBackPressed();
            // Otherwise, ask user if he wants to leave :)
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    }).create().show();
        }

    }
}
package com.delaroystudios.androidbottombar;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;


public class ThreeTabsActivity extends AppCompatActivity {
    private TextView messageView;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String NEARBYFRAGMENT_TAG = "NEARB";
    private static final String FRIENDSFRAGMENT_TAG = "FRIEDS";
    ActionBar mActionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_tabs);
        setTitle("BottomBar");

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                //messageView.setText(TabMessage.get(tabId, false));
                if (tabId == R.id.tab_favorites){
                    favoritesFragment();
                }else if (tabId == R.id.tab_nearby){
                    nearbyFragment();
                }else {
                    friendsFragment();
                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_favorites){
                    favoritesFragment();
                }else if (tabId == R.id.tab_nearby){
                    nearbyFragment();
                }else {
                    friendsFragment();
                }
            }
        });
    }

    private void favoritesFragment(){

        ThreeTabsFragment firstFragment = new ThreeTabsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, firstFragment, DETAILFRAGMENT_TAG).commit();

    }

    private void nearbyFragment(){

        NearbyFragment nearbyfragment = new NearbyFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, nearbyfragment, NEARBYFRAGMENT_TAG).commit();

    }

    private void friendsFragment(){

        FriendsFragment friendsfragment = new FriendsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, friendsfragment, FRIENDSFRAGMENT_TAG).commit();

    }


}
package com.webmyne.paylabas.userapp.base;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.webmyne.paylabas.userapp.giftcode.GiftCodeFragment;
import com.webmyne.paylabas.userapp.helpers.CustomTypeface;
import com.webmyne.paylabas.userapp.home.HomeFragment;
import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas_user.R;

public class MyDrawerActivity extends ActionBarActivity {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] leftSliderData = {"Home", "Profile", "About Us", "Contact Us","Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_drawer);
        nitView();

        if (toolbar != null) {
            toolbar.setTitle("Home");
            setSupportActionBar(toolbar);
        }

        initDrawer();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.main_container,new MyAccountFragment());
        ft.commit();

    }


    private void nitView() {

        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#494949"));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new ArrayAdapter<String>( MyDrawerActivity.this, android.R.layout.simple_list_item_activated_1, android.R.id.text1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);

        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawers();

                switch (position){

                    case 0:

                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.main_container,new MyAccountFragment());
                        ft.commit();

                       FragmentManager fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }

                        break;

                }

            }
        });
    }

    public void setToolColor(int color){
        toolbar.setBackgroundColor(color);
    }

    public void setToolTitle(String title){
        toolbar.setTitle(title);
    }
    public void setToolSubTitle(String subTitle){

        toolbar.setSubtitle(subTitle);
    }


    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
package com.webmyne.paylabas.userapp.base;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.webmyne.paylabas.userapp.home.MyAccountFragment;
import com.webmyne.paylabas.userapp.user_navigation.Aboutus;
import com.webmyne.paylabas.userapp.user_navigation.Contactus;
import com.webmyne.paylabas.userapp.user_navigation.Profile;
import com.webmyne.paylabas.userapp.user_navigation.Setting;
import com.webmyne.paylabas_user.R;

public class MyDrawerActivity extends ActionBarActivity {


    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] leftSliderData = {"Home", "Profile", "About Us", "Contact Us","Settings"};
    private int[] imagelist={R.drawable.icon_home,
            R.drawable.icon_editprofile,
            R.drawable.icon_aboutus,
            R.drawable.icon_contactus,
            R.drawable.icon_setting};
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
        leftDrawerList.setAdapter(new lViewadapter());

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
/*
                       FragmentManager fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
*/
                        break;
                    case 1:

                        FragmentManager manager1 = getSupportFragmentManager();
                        FragmentTransaction ft1 = manager1.beginTransaction();
                        ft1.replace(R.id.main_container,new Profile());
                        ft1.commit();

                        break;
                    case 2:

                        FragmentManager manager2 = getSupportFragmentManager();
                        FragmentTransaction ft2 = manager2.beginTransaction();
                        ft2.replace(R.id.main_container,new Aboutus());
                        ft2.commit();

                        break;
                    case 3:

                        FragmentManager manager3 = getSupportFragmentManager();
                        FragmentTransaction ft3 = manager3.beginTransaction();
                        ft3.replace(R.id.main_container,new Contactus());
                        ft3.commit();

                        break;
                    case 4:

                        FragmentManager manager4 = getSupportFragmentManager();
                        FragmentTransaction ft4 = manager4.beginTransaction();
                        ft4.replace(R.id.main_container,new Setting());
                        ft4.commit();

                        break;
                }

            }
        });
    }

    public class lViewadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return leftSliderData.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.mydrawer_listview_layout, parent, false);
            TextView title=(TextView)row.findViewById(R.id.txtTitle);
            ImageView img_icon=(ImageView)row.findViewById(R.id.imgIcon);
            img_icon.setBackgroundResource(imagelist[position]);

            title.setText(leftSliderData[position]);
            title.setTextSize(20);
            return row;
        }
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
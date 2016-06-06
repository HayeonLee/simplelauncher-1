package ch.arnab.simplelauncher;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @InjectView(R.id.main_tool_bar)
    Toolbar toolBar;
    @InjectView(R.id.main_drawer_view)
    NavigationView navigationView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    Fragment StateFrag = null;
    Fragment ListFrag = null;
    FragmentTransaction transaction;
    FragmentManager FM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolBar);



        mDrawerToggle
                = new ActionBarDrawerToggle(this, drawerLayout, toolBar,
                R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(mDrawerToggle);

        navigationView.setNavigationItemSelectedListener(this);
        StateFrag = new HY_StateDisplay();
        ListFrag = new AppsGridFragment();
        FM = getSupportFragmentManager();
        transaction = FM.beginTransaction();
        transaction.add(R.id.main_frame,StateFrag,"state");
        transaction.add(R.id.main_frame,ListFrag,"list");
        //transaction.replace(R.id.main_frame,StateFrag);
        transaction.hide(ListFrag);

        transaction.commit();
/*        Fragment fragment = new HY_StateDisplay();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
/*        StateFrag = new HY_StateDisplay();
        ListFrag = new AppsGridFragment();
        FM = getSupportFragmentManager();
        transaction = FM.beginTransaction();
        transaction.add(R.id.main_frame,StateFrag,"state");
        transaction.add(R.id.main_frame,ListFrag,"list");
        transaction.replace(R.id.main_frame,StateFrag);
        transaction.commit();
        Fragment fragment = new HY_StateDisplay();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

       //Fragment fragment = null;
        transaction = FM.beginTransaction();
        switch (id) {
            case R.id.navi_group1_item0:
                //fragment = new HY_StateDisplay();
                transaction.hide(ListFrag);
                transaction.show(StateFrag);
                break;
            case R.id.navi_group1_item1:
               // fragment = new AppsGridFragment();
                transaction.hide(StateFrag);
                transaction.show(ListFrag);
                break;
            case R.id.navi_group1_item2:
                transaction.hide(StateFrag);
                transaction.hide(ListFrag);
                break;
            case R.id.navi_group1_item3:
                transaction.hide(StateFrag);
                transaction.hide(ListFrag);
                break;
        }
        transaction.commit();
        drawerLayout.closeDrawers();
        menuItem.setChecked(true);

/*        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();

            drawerLayout.closeDrawers();
            menuItem.setChecked(true);
        }*/

        return true;
    }

}

/*import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends FragmentActivity implements View.OnClickListener {
    //public class HomeScreen extends FragmentActivity  {

    Fragment StateFrag = null;
    Fragment ListFrag = null;
    FragmentTransaction transaction;
    FragmentManager FM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        Button bt_oneFrag = (Button)findViewById(R.id.bt_oneFrag);
        bt_oneFrag.setOnClickListener(this);
        Button bt_twoFrag = (Button)findViewById(R.id.bt_twoFrag);
        bt_twoFrag.setOnClickListener(this);

        StateFrag = new HY_StateDisplay();
        ListFrag = new AppsGridFragment();
        FM = getSupportFragmentManager();
        transaction = FM.beginTransaction();
        transaction.add(R.id.mainFrag,StateFrag,"state");
        transaction.add(R.id.mainFrag,ListFrag,"list");
        transaction = FM.beginTransaction();
        transaction.replace(R.id.mainFrag,StateFrag);
        transaction.commit();
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public void onClick(View view) {

        transaction = FM.beginTransaction();

        switch (view.getId()){
            case R.id.bt_oneFrag:
                transaction.hide(ListFrag);
                transaction.show(StateFrag);
                break;
            case R.id.bt_twoFrag:
                transaction.hide(StateFrag);
                transaction.show(ListFrag);
                //transaction.replace(R.id.mainFrag,ListFrag);
                break;
            default:
                break;
        }
        transaction.commit();
    }
}*/

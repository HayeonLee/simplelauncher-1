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

    private static final String ACTION_PASERVICE_ACTIVATED ="PA_SERVICE_ACTIVATED";
    private static final String ACTION_PASERVICE_DEACTIVATED ="PA_SERVICE_DEACTIVATED";
    private static final String ACTION_PASERVICE_POINT_CHANGED ="PA_SERVICE_POINT_CHANGED";
    private static final String ACTION_PASERVICE_POINTTIME_TICKTING ="PA_SERVICE_POINTTIME_TICKTING";
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

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
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

        return true;
    }

}

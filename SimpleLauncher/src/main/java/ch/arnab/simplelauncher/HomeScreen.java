package ch.arnab.simplelauncher;

import android.os.Bundle;
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
        //transaction.hide(ListFrag);
/*        transaction = FM.beginTransaction();
        transaction.replace(R.id.mainFrag,StateFrag);
       // transaction.add(R.id.mainFrag,ListFrag);
        transaction.commit();*/
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
}

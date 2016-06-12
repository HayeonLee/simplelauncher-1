package ch.arnab.simplelauncher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.PAService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hayeonlee on 16. 5. 8.
 */
public class AppsManager extends Activity {

    Activity act = this;
    GridView gridView;
    private List<ResolveInfo> apps = new ArrayList<ResolveInfo>();
    private PackageManager pm;
    //for stop time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	mainIntent.putExtra("paservice","0xdeaddead");
        pm = getPackageManager();
        List<ResolveInfo> tapps = pm.queryIntentActivities(mainIntent,0);

        try
        {
            PAService pa = PAService.Stub.asInterface(ServiceManager.getService("PAService"));
            List<String> blockedList;
            if(pa != null)
            {
                blockedList = pa.getBlockedPackageList();

                for(ResolveInfo a : tapps)
                {
                    for(String p : blockedList)
                    {
                        try {
                            if (a.activityInfo.packageName.equals(p)) {
                                apps.add(a);
                            }
                        }catch(NullPointerException e)
                        {
                            Log.e("PAService","From launcher : blockedAppList NPE");
                        }
                    }
                }
            }

        }catch(RemoteException e)
        {

        }

        //pkgName = new ArrayList<String>();

        setContentView(R.layout.activity_manager);

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new gridAdapter());

        try
        {
           PAService pa = PAService.Stub.asInterface(ServiceManager.getService("PAService"));

            if(pa != null)
            {
                pa.enableTempExecutePermission();
                //pa.initPointTimer(1200,20);
            }else
            {
                throw new UnsupportedOperationException();
            }

        }catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try
        {
            PAService pa = PAService.Stub.asInterface(ServiceManager.getService("PAService"));

            if(pa != null)
            {
                pa.disableTempExecutePermission();
                //pa.initPointTimer(1200,20);
            }else
            {
                throw new UnsupportedOperationException();
            }

        }catch (RemoteException e)
        {
            e.printStackTrace();
        }

    }

    public class gridAdapter extends BaseAdapter {
        LayoutInflater inflater;

        public gridAdapter(){
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public final int getCount(){
            return apps.size();
        }

        public final Object getItem(int position){
            return apps.get(position);
        }

        public final long getItemId(int position){
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item, parent, false);
            }

            final ResolveInfo info = apps.get(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            TextView textView = (TextView) convertView.findViewById(R.id.textView1);
            imageView.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
            textView.setText(info.activityInfo.loadLabel(pm).toString());

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = getPackageManager().getLaunchIntentForPackage(info.activityInfo.packageName);

                        if (intent != null) {
                            startActivity(intent);
                        }
                }
            });

            return convertView;
        }
    }
}

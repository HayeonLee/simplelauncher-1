package ch.arnab.simplelauncher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
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
    private List<ResolveInfo> apps;
    private PackageManager pm;
    ArrayList<String> pkgName;

    private int point;
    private String timeStr;
    private String remainTimeStr;

    //for stop time
    private long restartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        Intent reIntent = getIntent();
        point = reIntent.getIntExtra("point",0);
        timeStr = reIntent.getStringExtra("timeStr");
        remainTimeStr = reIntent.getStringExtra("reTimeStr");
        pm = getPackageManager();
        apps = pm.queryIntentActivities(mainIntent,0);
        pkgName = new ArrayList<String>();

        restartTime = 0;

        setContentView(R.layout.activity_manager);

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new gridAdapter());

        Button button = (Button)findViewById(R.id.Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //receive new package list and reload launcher display with AppsLoader
                Intent sendIntent = new Intent("ch.arnab.simplelauncher.SEND_BROAD_CAST");
                sendIntent.putStringArrayListExtra("pkgName_list",pkgName);
                sendBroadcast(sendIntent);
                Intent data = new Intent();
                data.putExtra("afterPoint",point);

                restartTime = System.currentTimeMillis();
                data.putExtra("restartTime",restartTime);
                setResult(0,data);
                finish();
            }

        });
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
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox1);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                String msg = info.activityInfo.packageName.toString();
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (compoundButton.getId() == R.id.checkbox1) {
                        if (isChecked) {

                            if (point > 20) {
                                point -= 20;
                                pkgName.add(msg);
                                int num = point/20;
                                Toast.makeText(act, "You can check "+num+" more apps", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(act, "You need more points", Toast.LENGTH_SHORT).show();
                            }
                        }else if(!isChecked)
                        {
                            point +=20;
                            pkgName.remove(msg);
                            int num = point/20;
                            Toast.makeText(act, "You can check "+num+"more apps", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            return convertView;
        }
    }
}

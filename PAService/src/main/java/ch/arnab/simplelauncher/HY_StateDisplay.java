package ch.arnab.simplelauncher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.PAService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hayeonlee on 16. 6. 4.
 */
public class HY_StateDisplay extends Fragment {

    TextView mCulStuTimeView;
    private long time;
    private long culTime;

    //remain time
    TextView mRemainTimeView;
    //point
    TextView mPointView;
    private long point;
    boolean flag;
    //level
    TextView mLevelView;
    Button mLevelBtn;
    private long LEVEL;
    //unlock
    Button mUnlockBtn;

    ImageView mProfile;

    ProgressBar remainTimePb;
    Handler handler;
    long pInterval = 10;
    long perPoint = 10;

    private String rt ="";
    private String ct ="";

    private static final String ACTION_PASERVICE_POINT_CHANGED = "PA_SERVICE_POINT_CHANGED";
    private static final String ACTION_PASERVICE_POINTTIME_TICKTING = "PA_SERVICE_POINTTIME_TICKING";
    private static final String ACTION_PASERVICE_DEACTIVATED ="PA_SERVICE_DEACTIVATED";

    FloatingActionButton fab;
    DrawerLayout mylayout;
    View v2;


    BroadcastReceiver bc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_PASERVICE_POINT_CHANGED)) {
                if (intent.hasExtra("point")) {
                    point = intent.getLongExtra("point", 10);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPointView.setText(Long.toString(point));
                            //... UI 업데이트 작업
                        }
                    });
                    if(point>10)
                    {
                        mUnlockBtn.setBackgroundResource(R.drawable.ic_lock_open_black_48dp);
                        //mUnlockBtn.setVisibility(View.VISIBLE);
                    }else
                    {
                        mUnlockBtn.setBackgroundResource(R.drawable.ic_lock_black_48dp);

                    }
                }
            } else if (action.equals(ACTION_PASERVICE_POINTTIME_TICKTING)) {
                if (intent.hasExtra("time")) {
                    time = intent.getLongExtra("time", 0);
                    remainTimePb.setProgress((int)time);
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    rt = sdf.format(new Date(time*1000));
                    culTime += 1;
                    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
                    ct = sdf.format(new Date(culTime*1000));
                    mCulStuTimeView.setText(ct);
                    mRemainTimeView.setText(rt);
                }
            }else if(action.equals(ACTION_PASERVICE_DEACTIVATED))
            {
                try
                {
                    PAService pa = PAService.Stub.asInterface(ServiceManager.getService("PAService"));
                    pa.saveUserStateData(culTime,LEVEL);
                }catch (RemoteException e)
                {

                }
                getActivity().finish();
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.state_display, container, false);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v2 = layoutInflater.inflate(R.layout.activity_main,null);
        remainTimePb = (ProgressBar) v.findViewById(R.id.pb);

        IntentFilter inf = new IntentFilter();
        inf.addAction(ACTION_PASERVICE_DEACTIVATED);
        inf.addAction(ACTION_PASERVICE_POINT_CHANGED);
        inf.addAction(ACTION_PASERVICE_POINTTIME_TICKTING);
        getActivity().registerReceiver(bc,inf);

        try {
            PAService pa = PAService.Stub.asInterface(ServiceManager.getService("PAService"));

            if (pa != null) {
                long [] LT = pa.getSavedUserStateData();
                LEVEL = LT[0];
                culTime = LT[1];
                pa.setRequiredPointForExecuting(3,1);
                pa.initPointTimer(pInterval,perPoint);
                //changeBackground(LEVEL);
            } else {
                throw new UnsupportedOperationException();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        time = 0;

        mCulStuTimeView = (TextView) v.findViewById(R.id.CulStuTimeView);
        mRemainTimeView = (TextView) v.findViewById(R.id.RemaTimeView);
        mPointView = (TextView) v.findViewById(R.id.PointView);
        mLevelView = (TextView) v.findViewById(R.id.LevelView);
        mLevelView.setText(Long.toString(LEVEL));
        mCulStuTimeView.setText(Long.toString(culTime));
        mRemainTimeView.setText(Long.toString(time));
        mLevelBtn = (Button) v.findViewById(R.id.LevelBtn);
        mLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        PAService pa = PAService.Stub.asInterface(ServiceManager.getService("PAService"));
                        if (pa.modifyPointData(-20)) {
                            LEVEL++;
                            mPointView.setText(Long.toString(point));
                            mLevelView.setText(Long.toString(LEVEL));

                            //LEVEL에 대한 함수...
                            //changeBackground(LEVEL);
                        }
                    }catch (RemoteException e) {
                    }
            }
        });
        mUnlockBtn = (Button) v.findViewById(R.id.UnlockBtn);
        //mUnlockBtn.setVisibility(View.INVISIBLE);
            mUnlockBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(point > 10)
                    {
                        Intent intent = new Intent(getActivity(), AppsManager.class);
                        startActivity(intent);
                    }
                }
            });

        mProfile = (ImageView) v.findViewById(R.id.iv1);
        remainTimePb.setMax((int)pInterval);

        mylayout = (DrawerLayout) v2.findViewById(R.id.drawer_layout);

        return v;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }



}

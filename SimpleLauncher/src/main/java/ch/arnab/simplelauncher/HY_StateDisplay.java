package ch.arnab.simplelauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hayeonlee on 16. 6. 4.
 */
public class HY_StateDisplay extends Fragment {

    TextView mCulStuTimeView;
    private Timer mTimer;
    private int culmulTime;
    private long curTime;
    private long startTime;
    String timeStr;
    //remain time
    TextView mRemainTimeView;
    private int remainMin;
    private int remainSec;
    String remainTimeStr;

    //point
    TextView mPointView;
    private int point;
    boolean flag;
    //level
    TextView mLevelView;
    Button mLevelBtn;
    private int LEVEL;
    //unlock
    Button mUnlockBtn;

    //stop time
    private long stopTime;
    private long restartTime;
    private long pauseTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.state_display,container,false);
        //setContentView(R.layout.state_display);

/*        if(savedInstanceState!=null)
        {
            point = savedInstanceState.getInt("point",0);
            LEVEL = savedInstanceState.getInt("LEVEL",1);
            pauseTime = savedInstanceState.getLong("pauseTime");
        }else
        {*/
            point = 100;
            LEVEL = 1;
            pauseTime = 0;
       // }
        mCulStuTimeView = (TextView) v.findViewById(R.id.CulStuTimeView);
        MainTimerTask timerTask = new MainTimerTask();
        culmulTime = 0;
        curTime = 0;
        startTime = System.currentTimeMillis() + 1000;
        mTimer = new Timer();
        mTimer.schedule(timerTask, 100, 1000);

        //remain time setting
        mRemainTimeView = (TextView) v.findViewById(R.id.RemaTimeView);
        remainMin = 20;
        remainSec = 0;

        //
        //point = 100;
        //LEVEL = 1;
        stopTime = 0;
        restartTime = 0;
        //pauseTime = 0;

        flag = true;
        mPointView = (TextView) v.findViewById(R.id.PointView);
        mLevelView = (TextView) v.findViewById(R.id.LevelView);
        mLevelView.setText(Integer.toString(LEVEL));
        //
        mLevelBtn = (Button)v.findViewById(R.id.LevelBtn);
        //mLevelBtn.setVisibility(View.INVISIBLE);
        mLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(point>=20)
                {
                    point-=20;
                    LEVEL++;
                    mPointView.setText(Integer.toString(point));
                    mLevelView.setText(Integer.toString(LEVEL));
                }
            }
        });
        mUnlockBtn = (Button)v.findViewById(R.id.UnlockBtn);
        //mUnlockBtn.setVisibility(View.INVISIBLE);
        mUnlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(point>=20) {
                    //point -= 20;
                    mPointView.setText(Integer.toString(point));
                    Intent intent = new Intent(getActivity(), AppsManager.class);
                    intent.putExtra("point",point);
                    stopTime = curTime;
                    startActivityForResult(intent,0);
                    //finish();
                }
            }
        });

        return v;

    }

    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            curTime = System.currentTimeMillis();
            long millis = curTime - startTime - pauseTime;
            int sec = (int)(millis/1000);
            int hour = sec/3600;
            int min = (sec%3600)/60;
            sec = sec%60;

            //remain time
            remainSec = 60 - sec;
            remainMin = 29 - min%20;

            timeStr = String.format("%s:%s:%s",hour,min,sec);
            remainTimeStr = String.format("%s:%s",remainMin,remainSec);
            mCulStuTimeView.setText(timeStr);
            mRemainTimeView.setText(remainTimeStr);

            /*Point policy
                Every 30 minutes, 1 point increase.
                Every 20 point (10 hours), user choose LEVEL UP or unlocking app which user want to use.
            */

            if((min==00)&&(flag==true))
            {
                point++;
                flag=false;
                mPointView.setText(Integer.toString(point));
            }else if((min==30)&&(flag==false))
            {
                point++;
                flag=true;
                mPointView.setText(Integer.toString(point));
            }

            if(point>=20) {
                //push alarm
                //mLevelBtn.setVisibility(View.VISIBLE);
                //mUnlockBtn.setVisibility(View.VISIBLE);
            }
        }
    };
    class MainTimerTask extends TimerTask {
        public void run(){
            mHandler.post(mUpdateTimeTask);
        }
    }

    public void onDestroy(){
        mTimer.cancel();
        super.onDestroy();
    }
    public void onPause(){
        mTimer.cancel();
        super.onPause();
    }
    public void onResume(){
        super.onResume();
        MainTimerTask timerTask = new MainTimerTask();
        mTimer = new Timer();
        mTimer.schedule(timerTask,500,1000);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode)
        {
            case 0:
                point = data.getIntExtra("afterPoint",point);
                mPointView.setText(Integer.toString(point));
                restartTime = data.getLongExtra("restartTime",0);
                pauseTime += restartTime - stopTime;
                break;
        }
    }

/*    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("point",point);
        savedInstanceState.putInt("LEVEL",LEVEL);
        savedInstanceState.putLong("pauseTime",pauseTime);
    }*/
}

/*
public class HY_StateDisplay extends Activity {

    TextView mCulStuTimeView;
    private Timer mTimer;
    private int culmulTime;
    private long curTime;
    private long startTime;
    String timeStr;
    //remain time
    TextView mRemainTimeView;
    private int remainMin;
    private int remainSec;
    String remainTimeStr;

    //point
    TextView mPointView;
    private int point;
    boolean flag;
    //level
    TextView mLevelView;
    Button mLevelBtn;
    private int LEVEL;
    //unlock
    Button mUnlockBtn;

    //stop time
    private long stopTime;
    private long restartTime;
    private long pauseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_display);
        mCulStuTimeView = (TextView) findViewById(R.id.CulStuTimeView);
        MainTimerTask timerTask = new MainTimerTask();
        culmulTime = 0;
        curTime = 0;
        startTime = System.currentTimeMillis() + 1000;
        mTimer = new Timer();
        mTimer.schedule(timerTask, 100, 1000);

        //remain time setting
        mRemainTimeView = (TextView) findViewById(R.id.RemaTimeView);
        remainMin = 20;
        remainSec = 0;

        //
        point = 100;
        LEVEL = 1;
        flag = true;
        mPointView = (TextView) findViewById(R.id.PointView);
        mLevelView = (TextView) findViewById(R.id.LevelView);
        mLevelView.setText(Integer.toString(LEVEL));
        //
        mLevelBtn = (Button)findViewById(R.id.LevelBtn);
        //mLevelBtn.setVisibility(View.INVISIBLE);
        mLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(point>=20)
                {
                    point-=20;
                    LEVEL++;
                    mPointView.setText(Integer.toString(point));
                    mLevelView.setText(Integer.toString(LEVEL));
                }
            }
        });
        mUnlockBtn = (Button)findViewById(R.id.UnlockBtn);
        //mUnlockBtn.setVisibility(View.INVISIBLE);
        mUnlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(point>=20) {
                    //point -= 20;
                    mPointView.setText(Integer.toString(point));
                    Intent intent = new Intent(getApplicationContext(), AppsManager.class);
                    intent.putExtra("point",point);
                    stopTime = curTime;
                    startActivityForResult(intent,0);
                    //finish();
                }
            }
        });

        stopTime = 0;
        restartTime = 0;
        pauseTime = 0;

    }

    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            curTime = System.currentTimeMillis();
            long millis = curTime - startTime - pauseTime;
            int sec = (int)(millis/1000);
            int hour = sec/3600;
            int min = (sec%3600)/60;
            sec = sec%60;

            //remain time
            remainSec = 60 - sec;
            remainMin = 29 - min%20;

            timeStr = String.format("%s:%s:%s",hour,min,sec);
            remainTimeStr = String.format("%s:%s",remainMin,remainSec);
            mCulStuTimeView.setText(timeStr);
            mRemainTimeView.setText(remainTimeStr);
            */
/*Point policy
                Every 30 minutes, 1 point increase.
                Every 20 point (10 hours), user choose LEVEL UP or unlocking app which user want to use.
            *//*

            if((min==00)&&(flag==true))
            {
                point++;
                flag=false;
                mPointView.setText(Integer.toString(point));
            }else if((min==30)&&(flag==false))
            {
                point++;
                flag=true;
                mPointView.setText(Integer.toString(point));
            }

            if(point>=20) {
                //push alarm
                //mLevelBtn.setVisibility(View.VISIBLE);
                //mUnlockBtn.setVisibility(View.VISIBLE);
            }
        }
    };
    class MainTimerTask extends TimerTask {
        public void run(){
            mHandler.post(mUpdateTimeTask);
        }
    }

    protected void onDestroy(){
        mTimer.cancel();
        super.onDestroy();
    }
    protected void onPause(){
        mTimer.cancel();
        super.onPause();
    }
    protected void onResume(){
        MainTimerTask timerTask = new MainTimerTask();
        mTimer = new Timer();
        mTimer.schedule(timerTask,500,1000);
        super.onResume();
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode)
        {
            case 0:
                point = data.getIntExtra("afterPoint",point);
                mPointView.setText(Integer.toString(point));
                restartTime = data.getLongExtra("restartTime",0);
                pauseTime += restartTime - stopTime;
                break;
        }
    }
}
*/

package ch.arnab.simplelauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PAService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to look for interesting changes to the installed apps
 * so that the loader can be updated.
 *
 * @Credit http://developer.android.com/reference/android/content/AsyncTaskLoader.html
 */
public class PackageIntentReceiver extends BroadcastReceiver {

    final AppsLoader mLoader;

    private static final String ACTION_PASERVICE_PACKAGE_ADDED = "PA_SERVICE_PACKAGE_ADDED";


    public PackageIntentReceiver(AppsLoader loader) {
        mLoader = loader;

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PASERVICE_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        mLoader.getContext().registerReceiver(this, filter);

        // Register for events related to sdcard installation.
        IntentFilter sdFilter = new IntentFilter();
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        mLoader.getContext().registerReceiver(this, sdFilter);

        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction("ch.arnab.simplelauncher.SEND_BROAD_CAST");
        mLoader.getContext().registerReceiver(this, myFilter);

    }

    @Override public void onReceive(Context context, Intent intent) {
        // Tell the loader about the change.
        List<String> newNotAllowedPkg = null;
        try{
            PAService pa = PAService.Stub.asInterface(ServiceManager.getService("PAService"));
            if(pa!=null)
            {
                newNotAllowedPkg = pa.getBlockedPackageList();
            }
        }catch (RemoteException e)
        {
            e.printStackTrace();
        }
        ArrayList<String> p = new ArrayList<String>(newNotAllowedPkg);
        mLoader.receiveNotAllowedAppList(p);
        mLoader.onContentChanged();
    }

}

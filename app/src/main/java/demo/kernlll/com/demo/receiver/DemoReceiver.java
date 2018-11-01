package demo.kernlll.com.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import demo.kernlll.com.demo.service.DemoService;

public class DemoReceiver extends BroadcastReceiver {
    private static final String TAG = DemoReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.i(TAG,"receiver invoke");
    }
}

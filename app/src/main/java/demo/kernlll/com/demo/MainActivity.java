package demo.kernlll.com.demo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import demo.kernlll.com.demo.receiver.DemoReceiver;
import demo.kernlll.com.demo.service.DemoService;
import demo.kernlll.com.demo.service.IMyBinder;

public class MainActivity extends Activity {

    private MyConn conn;
    private Intent intent;
    private IMyBinder myBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DemoReceiver receiver = new DemoReceiver();
        IntentFilter intentFilter = new IntentFilter("demo.receiver");
        this.registerReceiver(receiver,intentFilter);
        this.unregisterReceiver(receiver);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private CLass MyConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            myBinder = (IMyBinder) iBinder;
        }

        @Override
        public void onServiceDisConnected(ComponentName name) {
            myBinder = null;
        }

    }

    public void bindService() {
        intent = new Intent(this, DemoService.class);
        conn = new MyConn();
        bindService(intent,conn,BIND_AUTO_CREATE);
    }

    public void invokeServiceMethod() {
        myBinder.invokeMethodInMyService();
    }

    public void unBindService() {
        unbindService(conn);
    }
}

package com.styleflying.AIDL;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.styleflying.R;

public class mAIDLActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnOk;
    private Button btnCancel;
    private Button btnCallBack;

    private mInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnOk = (Button) findViewById(R.id.btn_connect);
        btnCancel = (Button) findViewById(R.id.btn_disconnect);
        btnCallBack = (Button) findViewById(R.id.btn_send);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCallBack.setOnClickListener(this);
        btnCancel.setEnabled(false);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = mInterface.Stub.asInterface(service);
            btnOk.setEnabled(false);
            btnCancel.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    public void onClick(View v) {
        if (v == btnOk) {
            Bundle args = new Bundle();
            Intent intent = new Intent();
            intent.setAction("com.styleflying.AIDL.service");
            intent.setPackage("com.styleflying.AIDL");
            intent.putExtras(args);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        } else if (v == btnCancel) {
            btnCancel.setEnabled(false);
            btnOk.setEnabled(true);
            mService = null;
            unbindService(mConnection);
        } else if (v == btnCallBack) {
            try {
                if (mService != null)
                    mService.invokTest();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.harshit.indianstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
        if(connectivityReceiverListener != null){
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    public interface ConnectivityReceiverListener{
        void onNetworkConnectionChanged(boolean isConnected);
    }

}

package com.example.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkTester {
    private static final Logger LOG = LoggerFactory.getLogger(NetworkTester.class);

    private NetworkTester() {

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            LOG.info("NetworkCheck", "isNetworkAvailable: No");
            return false;
        }

        NetworkInfo[] info = connectivity.getAllNetworkInfo();

        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    LOG.info("NetworkCheck", "isNetworkAvailable: Yes");
                    return true;
                }
            }
        }
        return false;
    }
}

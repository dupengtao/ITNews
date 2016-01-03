package com.dpt.itnews.util;

import android.content.Context;

public class NetWorkStateUtil {
    public static boolean isNetworkConnected(Context context) {
        NetState netState = new NetState();
        return netState.hasNetWorkConnection(context);

    }

    public static boolean isWifiConnected(Context context) {
        NetState netState = new NetState();
        return netState.hasWifiConnection(context);
    }

    public static boolean isMobileConnected(Context context) {
        NetState netState = new NetState();
        return netState.hasGPRSConnection(context);
    }

    public static int getConnectedType(Context context) {
        return NetState.getNetWorkConnectionType(context);
    }

}

package com.buchanancreative.loggerlibrary.Manager;

import android.content.Context;


import java.util.ArrayList;

/**
 * Created by Alpha on 11/11/2017.
 */
// This class is used to create graphs of the data that was logged
public class LogListDataManager {
    private Context context;
    private static  LogListDataManager logListDataManager;
    private ArrayList<String> logSetsKeys;

    public LogListDataManager(Context context) {
        this.context = context;
    }

    public static LogListDataManager sharedManager(Context context) {
        if (logListDataManager == null) {
            logListDataManager = new LogListDataManager(context);
        } else {
            logListDataManager.context = context;
        }

        return logListDataManager;
    }

    public void loadLogSetListData() {

    }



    public int getLogsCount() {
        return logSetsKeys.size();
    }

    public String getLogSetAtIndex(int index) {
        return logSetsKeys.get(index);
    }

}

package com.phone.konka.accountingbook.Application;

import android.app.Application;
import android.content.pm.PackageManager;

import com.phone.konka.accountingbook.Base.Config;

/**
 * Created by 廖伟龙 on 2017/12/21.
 */

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initVersion();
    }

    public void initVersion() {
        try {

            Config.localVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            Config.serverVersion = 2;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}

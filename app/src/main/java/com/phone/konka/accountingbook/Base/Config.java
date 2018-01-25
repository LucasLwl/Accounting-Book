package com.phone.konka.accountingbook.Base;

import android.os.Environment;

/**
 * Created by 廖伟龙 on 2017/12/21.
 */

public class Config {


    public static final String BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String APP_DIR = BASE_DIR + "/Accounting Book";

    public static final String URL_PATH = "http://sz-ctfs.ftn.qq.com/ftn_handler/41ade229d9c794a2412b6d699706bd97cfe61c63f60684a52e61865c92cbe06df2b6d5e231bbec3ad3430a6cff98a871375f58fd998131227f7ae20e36f5fe92/app-release.apk";


}
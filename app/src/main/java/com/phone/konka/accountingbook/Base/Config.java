package com.phone.konka.accountingbook.Base;

import android.os.Environment;

/**
 * Created by 廖伟龙 on 2017/12/21.
 */

public class Config {

    public static int localVersion = 0;

    public static int serverVersion = 0;


    public static final String BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Accounting Book";

    public static final String URL_PATH = "http://dlsw.baidu.com/sw-search-sp/soft/e0/13545/GooglePinyinInstaller.1419846448.exe";
}

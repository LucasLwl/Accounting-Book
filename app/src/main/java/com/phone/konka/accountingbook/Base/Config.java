package com.phone.konka.accountingbook.Base;

import android.os.Environment;

/**
 * Created by 廖伟龙 on 2017/12/21.
 */

public class Config {


    public static final String BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String APP_DIR = BASE_DIR + "/Accounting Book";

    public static final String URL_PATH = "http://sz-ctfs.ftn.qq.com/ftn_handler/2eecc98cc2fcdeee3c605c3fa264396191cdabc146eed5458b2e009327626a9ebac51f45e6a59f432243c6a68805fcc7e75dbdd24e20550ec3e6d4391c9cacb5/app-release.apk";


}
package com.phone.konka.accountingbook.Base;

import android.os.Environment;

/**
 * Created by 廖伟龙 on 2017/12/21.
 */

public class Config {


    public static final String BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String APP_DIR = BASE_DIR + "/Accounting Book";

    public static final String URL_PATH = "https://sz-ctfs.ftn.qq.com/ftn_handler/9a66b2bebe8ab2e4aa705e2dc9e893c73f650bb6daec8a857dae137c2d06d3d670baaac466c3493dfd20c93d98dd5e108f641054f7be089c5a3c5b8d2bdcbcb1/app-release.apk";



}
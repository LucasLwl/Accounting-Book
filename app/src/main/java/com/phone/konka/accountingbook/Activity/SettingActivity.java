package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.ExcelUtil;
import com.phone.konka.accountingbook.Utils.ProviderManager;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 设置Activity
 * <p>
 * Created by 廖伟龙 on 2017/11/18.
 */

public class SettingActivity extends Activity implements View.OnClickListener {


    /**
     * Provider管理类
     */
    private ProviderManager mDataManager;


    /**
     * 线程池，用于Excel文件的导入导出
     */
    private ThreadPoolManager mThreadPool;


    /**
     * 当前日期
     */
    private String mTodayDate;

    /**
     * 导出次数
     */
    private static int mIndex = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initState();


        initData();
        initEven();
    }


    /**
     * 设置状态栏为半透明,设置沉浸式状态栏
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_setting_bar);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll.getLayoutParams();
            lp.height = getStatusBarHeight();
            ll.setLayoutParams(lp);
        }
    }


    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    private int getStatusBarHeight() {

        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 初始化数据
     */
    private void initData() {
        mThreadPool = ThreadPoolManager.getInstance();

        mDataManager = new ProviderManager(this);

        //        获取今日日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        mTodayDate = sdf.format(new Date());
    }


    /**
     * 初始化事件
     */
    private void initEven() {
        findViewById(R.id.img_setting_back).setOnClickListener(this);
        findViewById(R.id.ll_setting_inAccount).setOnClickListener(this);
        findViewById(R.id.ll_setting_outAccount).setOnClickListener(this);
        findViewById(R.id.ll_setting_aboutMe).setOnClickListener(this);
    }


//    /**
//     * 通过返回的Uri获取excel文件的路径
//     *
//     * @param uri
//     * @return
//     */
//    public String getFilePathFromUri(Uri uri) {
//
//        if (uri == null)
//            return null;
//
//        String path = null;
//        String scheme = uri.getScheme();
//
//        if (scheme == null) {
//            path = uri.getPath();
//        } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
//            path = uri.getPath();
//        } else if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
//            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                    if (index >= 0)
//                        path = cursor.getString(index);
//                }
//                cursor.close();
//            }
//        }
//        return path;
//    }


    private void showDialog() {
        final String excelName = mTodayDate + mIndex + ".xls";
        new AlertDialog.Builder(this)
                .setMessage("系统会将您的账本导出到以下文件: 内部存储器/Accounting Book/Bill/" + excelName)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SettingActivity.this, "文件正在导出", Toast.LENGTH_SHORT).show();

//                              在线程池中执行IO操作以及读数据库操作
                        mThreadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (mDataManager.isDBEmpty()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SettingActivity.this, "文件导出失败：您还没账单信息",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    ExcelUtil.writeExcel(excelName, "content", mDataManager.getAllData());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mIndex++;
                                            Toast.makeText(SettingActivity.this, "文件已导出至：内部存储设备/Accounting Book/Bill/" + excelName,
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }).show();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_setting_back:
                finish();
                break;

//            点击导入Excel表格
            case R.id.ll_setting_inAccount:

                Intent intent = new Intent(this, ImportExcelActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("application/vnd.ms-excel");
////                intent.setType("application/*");
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                try {
//                    startActivityForResult(intent, 1);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
//                            .show();
//                }
                break;


//            点击导出Excel表格
            case R.id.ll_setting_outAccount:

                showDialog();

                break;

            case R.id.ll_setting_aboutMe:
                Intent intent1 = new Intent(SettingActivity.this, AboutMeActivity.class);
                startActivity(intent1);
                break;
        }
    }
}

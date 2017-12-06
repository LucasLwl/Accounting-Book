package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DBOperator;
import com.phone.konka.accountingbook.Utils.ExcelUtil;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 设置Activity
 * <p>
 * Created by 廖伟龙 on 2017/11/18.
 */

public class SettingActivity extends Activity implements View.OnClickListener {


    /**
     * 数据库管理类
     */
    private DBOperator mDBOperator;


    /**
     * 线程池，用于Excel文件的导入导出
     */
    private ThreadPoolManager mThreadPool;


    /**
     * 今天日期
     */
    private String mTodayDate;

    /**
     * 导出次数
     */
    private static int mIndex = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initData();
        initEven();
    }

    private void initData() {
        mThreadPool = ThreadPoolManager.getInstance();

        mDBOperator = new DBOperator(this);

        //        获取今日日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        mTodayDate = sdf.format(new Date());
    }

    private void initEven() {
        findViewById(R.id.img_setting_back).setOnClickListener(this);
        findViewById(R.id.ll_setting_inAccount).setOnClickListener(this);
        findViewById(R.id.ll_setting_outAccount).setOnClickListener(this);
        findViewById(R.id.ll_setting_aboutMe).setOnClickListener(this);

    }


    /**
     * 点击选中的Excel后回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            final Uri uri = data.getData();

            // 弹出是否覆盖提示框
            new AlertDialog.Builder(this)
                    .setMessage("是否覆盖之前记录")
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(SettingActivity.this, "文件正在导入", Toast.LENGTH_SHORT).show();

//                            根据返回的Uri获取Excel表格的文件路径
                            final String path = getFilePathFromUri(uri);

//                           在线程池中进行IO操作和写数据库操作
                            mThreadPool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    mDBOperator.removeAllData();
                                    List<DetailTagBean> list = ExcelUtil.readExcel(path);
                                    for (DetailTagBean bean : list)
                                        mDBOperator.insertAccount(bean);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SettingActivity.this, "文件已经导入", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }).show();
        }
    }

    /**
     * 通过返回的Uri获取excel文件的路径
     *
     * @param uri
     * @return
     */
    public String getFilePathFromUri(Uri uri) {

        if (uri == null)
            return null;

        String path = null;
        String scheme = uri.getScheme();

        if (scheme == null) {
            path = uri.getPath();
        } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
            path = uri.getPath();
        } else if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (cursor != null)
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index >= 0)
                        path = cursor.getString(index);
                }
        }
        return path;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_setting_back:
                finish();
                break;

//            点击导入Excel表格
            case R.id.ll_setting_inAccount:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.ms-excel");
//                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(intent, 1);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
                            .show();
                }
                break;


//            点击导出Excel表格
            case R.id.ll_setting_outAccount:
//                根据当前日期获取Ecxel表名
                final String excelName = mTodayDate + (++mIndex) + ".xls";

//                弹出是否保存对话框
                new AlertDialog.Builder(this)
                        .setMessage("系统会将您的账本导入到以下文件：内部存储设备/Bill/" + excelName)
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
                                        if (mDBOperator.isDBEmpty()) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(SettingActivity.this, "文件导出失败：您还没账单信息",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        } else {
                                            ExcelUtil.writeExcel(excelName, "content", mDBOperator.getAllData());
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(SettingActivity.this, "文件已导出至：内部存储设备/Bill/" + excelName,
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }).show();
                break;

            case R.id.ll_setting_aboutMe:
                Intent intent1 = new Intent(SettingActivity.this, AboutMe.class);
                startActivity(intent1);
                break;
        }
    }
}

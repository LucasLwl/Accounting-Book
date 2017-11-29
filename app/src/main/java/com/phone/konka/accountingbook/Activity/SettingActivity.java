package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DBManager;
import com.phone.konka.accountingbook.Utils.ExcelUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/18.
 */

public class SettingActivity extends Activity implements View.OnClickListener {


    private DBManager mDBManager;

    private AlertDialog mDialog;

    private Date mDate;
    private String mTodayDate;
    private static int mIndex = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initData();
        initEven();
    }

    private void initData() {
        mDate = new Date(System.currentTimeMillis());
        mDBManager = new DBManager(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        mTodayDate = sdf.format(mDate);
    }

    private void testExcel() {

        mDBManager = new DBManager(this);
        List<DetailTagBean> list = ExcelUtil.readExcel("title.xls");
        for (DetailTagBean bean : list)
            mDBManager.insertAccount(bean);
    }

    private void initEven() {
        findViewById(R.id.img_setting_back).setOnClickListener(this);
        findViewById(R.id.tv_setting_inAccount).setOnClickListener(this);
        findViewById(R.id.tv_setting_outAccount).setOnClickListener(this);
        findViewById(R.id.tv_setting_aboutMe).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_setting_back:

                break;

            case R.id.tv_setting_inAccount:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("application/vnd.ms-excel");
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(intent, 1);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case R.id.tv_setting_outAccount:
                final String excelName = mTodayDate + (++mIndex) + ".xls";
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("系统会将您的账本导入到以下文件：内部存储设备/Bill/" + excelName)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ExcelUtil.writeExcel(excelName, "content", mDBManager.getAllData());
                                Toast.makeText(SettingActivity.this, "文件正在导出", Toast.LENGTH_SHORT).show();
                            }
                        });
                mDialog = builder.create();
                mDialog.show();
                break;

            case R.id.tv_setting_aboutMe:

                break;

        }

    }
}

package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.phone.konka.accountingbook.Adapter.FileAdapter;
import com.phone.konka.accountingbook.Base.Config;
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.ExcelUtil;
import com.phone.konka.accountingbook.Utils.ProviderManager;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 导入xls文件管理器
 * <p>
 * Created by 廖伟龙 on 2018-1-8.
 */

public class ImportExcelActivity extends Activity implements View.OnClickListener {


    /**
     * 文件列表
     */
    private ListView mLvFile;


    /**
     * 顶部滑动文件目录
     */
    private HorizontalScrollView mHsvDir;


    /**
     * 顶部文件目录列表
     */
    private LinearLayout mLlDir;


    /**
     * 当前目录文件数据
     */
    private File[] mFiles;


    /**
     * 文件列表适配器
     */
    private FileAdapter mAdapter;


    /**
     * 已打开的文件目录
     */
    private String mOpenDirs = "/Accounting Book/Bill";


    /**
     * Provider管理类
     */
    private ProviderManager mDataManager;


    /**
     * 线程池，用于Excel文件的导入
     */
    private ThreadPoolManager mThreadPool;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_excel);

        initState();

        initView();
        initData();
        initEvent();
    }


    /**
     * 实现按返回键逐级返回目录
     */
    @Override
    public void onBackPressed() {

        if (!mOpenDirs.isEmpty()) {
            mLlDir.removeViewAt(mLlDir.getChildCount() - 1);
            mOpenDirs = mOpenDirs.substring(0, mOpenDirs.lastIndexOf("/"));
            getFileList(Config.BASE_DIR + mOpenDirs);
            mAdapter.setDatas(mFiles);
        } else {
            super.onBackPressed();
        }

    }

    /**
     * 设置状态栏为半透明,设置沉浸式状态栏
     */
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            LinearLayout ll = (LinearLayout) findViewById(R.id.ll_importExcel_bar);
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
     * 初始化View
     */
    private void initView() {
        mLvFile = (ListView) findViewById(R.id.lv_importExcel_file);
        mLlDir = (LinearLayout) findViewById(R.id.ll_importExcel_dir);
        mHsvDir = (HorizontalScrollView) findViewById(R.id.hsv_importExcel_dir);
        mLvFile.setEmptyView(findViewById(R.id.ll_importExcel_emptyView));


//        默认打开到账单目录，显示目录列表
        String[] strArr = mOpenDirs.split("/");
        for (String str : strArr)
            addDirView(str);
    }


    /**
     * 初始化数据
     */
    private void initData() {

        mDataManager = new ProviderManager(this);
        mThreadPool = ThreadPoolManager.getInstance();

//        默认打开到账单目录
        getFileList(Config.BASE_DIR + mOpenDirs);
        mAdapter = new FileAdapter(this, mFiles);
        mLvFile.setAdapter(mAdapter);
    }


    /**
     * 初始化点击事件
     */
    private void initEvent() {

        findViewById(R.id.img_importExcel_back).setOnClickListener(this);

        findViewById(R.id.tv_importExcel_baseDir).setOnClickListener(this);


//        文件列表点击事件
        mLvFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = mFiles[position];
                if (file.isDirectory()) {

                    addDirView(file.getName());

                    mOpenDirs += "/" + file.getName();
                    getFileList(Config.BASE_DIR + mOpenDirs);
                    mAdapter.setDatas(mFiles);
                } else {
                    if (file.getName().endsWith(".xls")) {
                        showDialog(file.getAbsolutePath());
                    } else {
                        Toast.makeText(ImportExcelActivity.this, "请选择以.xls结尾的文件", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    /**
     * 根据目录路径获取其所有子文件和文件夹
     * 进行过滤隐藏文件
     * 文件夹显示在前，文件显示在后，以字典序列进行排序
     *
     * @param dir 目录路径
     */
    private void getFileList(String dir) {

        File file = new File(dir);

        if (!file.exists())
            file.mkdirs();

//        过滤隐藏文件
        mFiles = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });

//        进行排序

        Arrays.sort(mFiles, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                if (f1.isDirectory() && f2.isDirectory()) {
                    return f1.getName().toUpperCase().compareTo(f2.getName().toUpperCase());
                } else if (f1.isDirectory())
                    return -1;
                else if (f2.isDirectory())
                    return 1;
                else {
                    return f1.getName().toUpperCase().compareTo(f2.getName().toUpperCase());
                }
            }
        });
    }


    /**
     * 为顶部目录列表添加目录
     *
     * @param dirName 目录名
     */
    private void addDirView(String dirName) {

        if (dirName.isEmpty())
            return;

        TextView tvDir = new TextView(ImportExcelActivity.this);
        tvDir.setText(dirName + " >  ");
        tvDir.setTextSize(Dimension.SP, 14);
        tvDir.setTextColor(getResources().getColor(R.color.item_text));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tvDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = mLlDir.getChildCount();
                int index = mLlDir.indexOfChild(v);
                for (int i = count - 1; i > index; i--) {
                    mLlDir.removeViewAt(i);
                    mOpenDirs = mOpenDirs.substring(0, mOpenDirs.lastIndexOf("/"));
                }
                getFileList(Config.BASE_DIR + mOpenDirs);
                mAdapter.setDatas(mFiles);
            }
        });
        mLlDir.addView(tvDir, lp);
        mLlDir.post(new Runnable() {
            @Override
            public void run() {
                mHsvDir.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }


    /**
     * 弹出询问是否导入对话框
     *
     * @param filePath 文件绝对路径
     */
    private void showDialog(final String filePath) {
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
                        Toast.makeText(ImportExcelActivity.this, "文件正在导入", Toast.LENGTH_SHORT).show();

//                           在线程池中进行IO操作和写数据库操作
                        mThreadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                List<DetailTagBean> list = ExcelUtil.readExcel(filePath);
                                if (list.size() > 0) {
                                    mDataManager.deleteAllData();
                                    for (DetailTagBean bean : list)
                                        mDataManager.insertAccount(bean);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ImportExcelActivity.this, "文件已经导入", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ImportExcelActivity.this, "文件导入失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            }
                        });
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_importExcel_back:
                finish();
                break;

            case R.id.tv_importExcel_baseDir:
                mOpenDirs = "";
                mLlDir.removeAllViews();
                getFileList(Config.BASE_DIR + mOpenDirs);
                mAdapter.setDatas(mFiles);
                break;
        }
    }
}

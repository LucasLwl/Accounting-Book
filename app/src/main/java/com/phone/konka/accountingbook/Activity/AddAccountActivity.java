package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.Fragment.AddAccountFragment;
import com.phone.konka.accountingbook.Fragment.AddTagFragment;
import com.phone.konka.accountingbook.Fragment.EditTagFragment;
import com.phone.konka.accountingbook.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/18.
 */

public class AddAccountActivity extends Activity {


    /**
     * 表示Activity本身
     */
    public static final int FROM_ACTIVITY = -1;

    /**
     * 表示添加账单Fragment，且为支出
     */
    public static final int ADD_ACCOUNT_FRAGMENT_OUT = 0;

    /**
     * 表示添加账单Fragment，且为收入
     */
    public static final int ADD_ACCOUNT_FRAGMENT_IN = 1;

    /**
     * 表示编辑Tag的Fragment
     */
    public static final int EDIT_TAG_FRAGMENT = 2;


    /**
     * 表示添加Tag的Fragment
     */
    public static final int ADD_TAG_FRAGMENT = 3;

    /**
     * 标志位，表示正在显示的Fragment
     */
    private int mIndex;

    /**
     * 支出Tag数据
     */
    public List<TagBean> mOutList;

    /**
     * 收入Tag数据
     */
    public List<TagBean> mInList;


    /**
     * 支出推荐Tag数据
     */
    public List<TagBean> mOutRecomList;

    /**
     * 收入推荐Tag数据
     */
    public List<TagBean> mInRecomList;

    /**
     * 添加账单Fragment
     */
    private AddAccountFragment mAddAccountFragment;

    /**
     * 编辑Tag 的Fragment
     */
    private EditTagFragment mEditTagFragment;

    /**
     * 添加Tag的Fragment
     */
    private AddTagFragment mAddTagFragment;


    /**
     * Fragment的事务管理
     */
    private FragmentTransaction transaction;


    /**
     * SharedPreferences中存数据的文件名
     */
    private static final String TAG_NAME = "tag";


    /**
     * 存储收入Tag的key
     */
    private static final String IN_TAG = "in";


    /**
     * 存储支出Tag的key
     */
    private static final String OUT_TAG = "out";


    /**
     * 存储收入推荐Tag的key
     */
    private static final String IN_RECOM_TAG = "inrecom";


    /**
     * 存储支出推荐Tag的key
     */
    private static final String OUT_RECOM_TAG = "outrecom";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        initData();

//        显示初始的Fragment
        showFragment(FROM_ACTIVITY, ADD_ACCOUNT_FRAGMENT_OUT);
        mIndex = ADD_ACCOUNT_FRAGMENT_OUT;
    }


    @Override
    protected void onStop() {
        super.onStop();

        //存储Tag的信息
        writeToSharedPreferences(OUT_TAG, mOutList);
        writeToSharedPreferences(IN_TAG, mInList);
        writeToSharedPreferences(OUT_RECOM_TAG, mOutRecomList);
        writeToSharedPreferences(IN_RECOM_TAG, mInRecomList);
    }

    /**
     * 监听按下返回键的操作
     * <p>
     * 当在编辑TagFragment时，保存修改的Tag，并回到编辑前的Fragment
     */
    @Override
    public void onBackPressed() {
        if (mEditTagFragment != null && !mEditTagFragment.isHidden()) {
            if (mIndex == 0) {
                mOutList = mEditTagFragment.getMyTagList();
                mOutRecomList = mEditTagFragment.getRecomTagList();
            } else {
                mInList = mEditTagFragment.getMyTagList();
                mInRecomList = mEditTagFragment.getRecomTagList();
            }
            showFragment(EDIT_TAG_FRAGMENT, mIndex);
        } else if (mAddTagFragment != null && !mAddTagFragment.isHidden()) {

        } else {
            super.onBackPressed();
        }
    }

    /**
     * 返回是支出还是收入标志位
     * 返回当前显示的fragment标志
     *
     * @return
     */
    public int getIndex() {
        return mIndex;
    }

    private void initData() {


        mOutList = new ArrayList<>();
        mInList = new ArrayList<>();
        mOutRecomList = new ArrayList<>();
        mInRecomList = new ArrayList<>();


        /**
         * 获取用户Tag的信息
         */
        readFromSharedPreferences(OUT_TAG, mOutList);
        readFromSharedPreferences(IN_TAG, mInList);
        readFromSharedPreferences(OUT_RECOM_TAG, mOutRecomList);
        readFromSharedPreferences(IN_RECOM_TAG, mInRecomList);


        /**
         * 当sharedPreferences中没有数据时，代表用户初始使用，获取默认的数据
         */
        if (mOutList.size() == 0) {

            Log.i("ddd", "fromArray");
            String[] strArr = getResources().getStringArray(R.array.out_tag_text);
            TypedArray ta = getResources().obtainTypedArray(R.array.out_tag_icon);

            TagBean bean;
            for (int i = 0; i < strArr.length; i++) {
                bean = new TagBean(strArr[i], ta.getResourceId(i, R.drawable.icon_diet));
                mOutList.add(bean);
            }

            strArr = getResources().getStringArray(R.array.in_tag_text);
            ta = getResources().obtainTypedArray(R.array.in_tag_icon);

            for (int i = 0; i < strArr.length; i++) {
                bean = new TagBean(strArr[i], ta.getResourceId(i, R.drawable.icon_diet));
                mInList.add(bean);
            }

            strArr = getResources().getStringArray(R.array.out_recom_tag_text);
            ta = getResources().obtainTypedArray(R.array.out_recom_tag_icon);

            for (int i = 0; i < strArr.length; i++) {
                bean = new TagBean(strArr[i], ta.getResourceId(i, R.drawable.icon_diet));
                mOutRecomList.add(bean);
                mInRecomList.add(bean);
            }
        }

    }


    /**
     * 读取啥redPreferences中的Tag数据
     *
     * @param key
     * @param list
     */
    public void readFromSharedPreferences(String key, List<TagBean> list) {
        SharedPreferences spf = getSharedPreferences(TAG_NAME, MODE_PRIVATE);
        String str = spf.getString(key, "");
        try {
            JSONObject jo = new JSONObject(str);
            for (int i = 0; i < jo.length() / 2; i++) {
                TagBean bean = new TagBean();
                bean.setText(jo.getString("text" + i));
                bean.setIconID(jo.getInt("icon" + i));
                list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("ddd", "readFromSharedPreferences:  " + e.toString());
        }
    }


    /**
     * 向sharedPreferences写入Tag数据
     *
     * @param key
     * @param list
     */
    public void writeToSharedPreferences(String key, List<TagBean> list) {

        SharedPreferences.Editor editor = getSharedPreferences(TAG_NAME, MODE_PRIVATE).edit();
        try {
            JSONObject jo = new JSONObject();
            for (int i = 0; i < list.size(); i++) {
                jo.put("text" + i, list.get(i).getText());
                jo.put("icon" + i, list.get(i).getIconID());
            }
            editor.putString(key, jo.toString());
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("ddd", "writeToSharedPreferences:  " + e.toString());
        }
    }


    /**
     * 隐藏所有Frgament
     */
    private void hideAllFragment() {
        if (mAddAccountFragment != null)
            transaction.hide(mAddAccountFragment);
        if (mEditTagFragment != null)
            transaction.hide(mEditTagFragment);
        if (mAddTagFragment != null)
            transaction.hide(mAddTagFragment);
    }


    /**
     * 显示Fragment
     *
     * @param from 当前的Fragment
     * @param to   要显示的Fragment
     */
    public void showFragment(int from, int to) {
        transaction = getFragmentManager().beginTransaction();
        switch (to) {
            case ADD_ACCOUNT_FRAGMENT_IN:
            case ADD_ACCOUNT_FRAGMENT_OUT:
                if (mAddAccountFragment == null) {
                    mAddAccountFragment = new AddAccountFragment();
                    transaction.add(R.id.fl_addAccount_content, mAddAccountFragment);
                }
                hideAllFragment();
                transaction.show(mAddAccountFragment);
                transaction.commit();
                break;

            case EDIT_TAG_FRAGMENT:
                if (mEditTagFragment == null) {
                    mEditTagFragment = new EditTagFragment();
                    transaction.add(R.id.fl_addAccount_content, mEditTagFragment);
                }
                hideAllFragment();
                transaction.show(mEditTagFragment);
                mIndex = from;
                transaction.commit();
                break;

            case ADD_TAG_FRAGMENT:
                if (mAddTagFragment == null) {
                    mAddTagFragment = new AddTagFragment();
                    transaction.add(R.id.fl_addAccount_content, mAddTagFragment);
                }
                hideAllFragment();
                transaction.show(mAddTagFragment);
                mIndex = from;
                transaction.commit();
                break;
        }
    }


}

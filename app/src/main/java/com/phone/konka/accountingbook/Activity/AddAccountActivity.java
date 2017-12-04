package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.Fragment.AddAccountFragment;
import com.phone.konka.accountingbook.Fragment.AddTagFragment;
import com.phone.konka.accountingbook.Fragment.EditTagFragment;
import com.phone.konka.accountingbook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/18.
 */

public class AddAccountActivity extends Activity {


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
     * 标志位，标志是支出还是收入
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

    public static final String TAG = "AddAccountActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        initData();

//        显示初始的Fragment
        showFragment(FROM_ACTIVITY, ADD_ACCOUNT_FRAGMENT_OUT);

        Log.i("ddd", TAG + " :onCreate");
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


        for (int i = 0; i < 15; i++) {
            TagBean bean = new TagBean();
            bean.setText("in" + i);
            bean.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_diet));
            mInList.add(bean);
            mInRecomList.add(bean);
        }


        for (int i = 0; i < 15; i++) {
            TagBean bean = new TagBean();
            bean.setText("out" + i);
            bean.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_diet));
            mOutList.add(bean);
            mOutRecomList.add(bean);
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

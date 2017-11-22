package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.Fragment.AddTagFragment;
import com.phone.konka.accountingbook.Fragment.EditTagFragment;
import com.phone.konka.accountingbook.Fragment.AddAccountFragment;
import com.phone.konka.accountingbook.R;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/18.
 */

public class AddAccountActivity extends Activity {


    public static final int ADD_ACCOUNT_FRAGMENT = 0;
    public static final int EDIT_TAG_FRAGMENT = 1;
    public static final int ADD_TAG_FRAGMENT = 2;

    public List<TagBean> mOutList;
    public List<TagBean> mInList;
    public List<TagBean> mOutRecomList;
    public List<TagBean> mInRecomList;


    private AddAccountFragment mAddAccountFragment;
    private EditTagFragment mEditTagFragment;
    private AddTagFragment mAddTagFragment;

    private FragmentTransaction transaction;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        showFragment(-1, ADD_ACCOUNT_FRAGMENT);
    }


    private void hideAllFragment() {
        if (mAddAccountFragment != null)
            transaction.hide(mAddAccountFragment);
        if (mEditTagFragment != null)
            transaction.hide(mEditTagFragment);
        if (mAddTagFragment != null)
            transaction.hide(mAddTagFragment);
    }


    public void showFragment(int from, int to) {
        transaction = getFragmentManager().beginTransaction();
        switch (to) {
            case ADD_ACCOUNT_FRAGMENT:
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
                transaction.commit();
                break;
            case ADD_TAG_FRAGMENT:
                if (mAddTagFragment == null) {
                    mAddTagFragment = new AddTagFragment();
                    transaction.add(R.id.fl_addAccount_content, mAddTagFragment);
                }
                hideAllFragment();
                transaction.show(mAddTagFragment);
                transaction.commit();
                break;
        }
    }


}

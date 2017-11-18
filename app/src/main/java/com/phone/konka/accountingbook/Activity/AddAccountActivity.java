package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.phone.konka.accountingbook.Fragment.InFragment;
import com.phone.konka.accountingbook.Fragment.OutFragment;
import com.phone.konka.accountingbook.R;

/**
 * Created by 廖伟龙 on 2017/11/18.
 */

public class AddAccountActivity extends Activity implements View.OnClickListener {


    private TextView mTvIn;
    private TextView mTvOut;

    private InFragment mInFragment;
    private OutFragment mOutFragment;

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);


        initView();
        initEven();


    }


    private void initView() {
        mTvIn = (TextView) findViewById(R.id.tv_addAccount_in);
        mTvOut = (TextView) findViewById(R.id.tv_addAccount_out);
    }

    private void initEven() {
        mTvIn.setOnClickListener(this);
        mTvOut.setOnClickListener(this);
    }

    private void hideAllFragment() {
        if (mInFragment != null)
            transaction.hide(mInFragment);
        if (mOutFragment != null)
            transaction.hide(mOutFragment);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_addAccount_in:
                transaction = getFragmentManager().beginTransaction();
                if (mInFragment == null) {
                    mInFragment = new InFragment();
                    transaction.add(R.id.fl_addAccount_content, mInFragment);
                }
                hideAllFragment();
                transaction.show(mInFragment);
//                transaction.replace(R.id.fl_addAccount_content, mInFragment);
                transaction.commit();
                break;

            case R.id.tv_addAccount_out:
                transaction = getFragmentManager().beginTransaction();
                if (mOutFragment == null) {
                    mOutFragment = new OutFragment();
                    transaction.add(R.id.fl_addAccount_content, mOutFragment);
                }
                hideAllFragment();
                transaction.show(mOutFragment);
//                transaction.replace(R.id.fl_addAccount_content, mOutFragment);
                transaction.commit();
                break;
        }
    }


}

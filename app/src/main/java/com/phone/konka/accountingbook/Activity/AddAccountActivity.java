package com.phone.konka.accountingbook.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;

import com.phone.konka.accountingbook.Fragment.AddTagFragment;
import com.phone.konka.accountingbook.Fragment.EditTagFragment;
import com.phone.konka.accountingbook.Fragment.InFragment;
import com.phone.konka.accountingbook.Fragment.OutFragment;
import com.phone.konka.accountingbook.R;

/**
 * Created by 廖伟龙 on 2017/11/18.
 */

public class AddAccountActivity extends Activity {


    private InFragment mInFragment;
    private OutFragment mOutFragment;
    private EditTagFragment mEditTagFragment;
    private AddTagFragment mAddTagFragment;

    private GridView mView;

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        transaction = getFragmentManager().beginTransaction();
//        if (mEditTagFragment == null) {
//            mEditTagFragment = new EditTagFragment();
//            transaction.add(R.id.fl_addAccount_content, mEditTagFragment);
//        }
//        transaction.show(mEditTagFragment);
//        transaction.commit();

        if (mInFragment == null) {
            mInFragment = new InFragment();
            transaction.add(R.id.fl_addAccount_content, mInFragment);
        }
        transaction.show(mInFragment);
        transaction.commit();

    }


}

package com.phone.konka.accountingbook.Fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.phone.konka.accountingbook.Activity.AddAccountActivity;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;

/**
 * Created by 廖伟龙 on 2017/11/20.
 */

public class AddTagFragment extends Fragment implements View.OnClickListener {

    private View rootView;

    private ImageView mImgTag;
    private EditText mEtTag;

    private GridView mGvTag;
    private Adapter mAdapter;

    private int mIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_tag, null);

        initView();
        initData();
        initEven();
        return rootView;
    }

    private void initData() {
        mIndex = ((AddAccountActivity) getActivity()).getIndex();
        mAdapter = new Adapter();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mAdapter != null) {
            mIndex = ((AddAccountActivity) getActivity()).getIndex();
        }
    }

    private void initView() {

        mImgTag = (ImageView) rootView.findViewById(R.id.img_addTagFragment_tag);
        mEtTag = (EditText) rootView.findViewById(R.id.et_addTagFragment_tag);
        mGvTag = (GridView) rootView.findViewById(R.id.gv_addTagFragment_tag);

    }

    private void initEven() {

        rootView.findViewById(R.id.img_addTagFragment_back).setOnClickListener(this);
        rootView.findViewById(R.id.img_addTagFragment_save).setOnClickListener(this);

        mGvTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mImgTag.setImageDrawable(((ImageView) view).getDrawable());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_addTagFragment_save:
                TagBean bean = new TagBean();
                bean.setText(mEtTag.getText().toString());
                if (mIndex == 0) {
                    ((AddAccountActivity) getActivity()).mOutList.add(((AddAccountActivity) getActivity()).mOutList.size() - 1, bean);
                } else {
                    ((AddAccountActivity) getActivity()).mInList.add(((AddAccountActivity) getActivity()).mOutList.size() - 1, bean);
                }
            case R.id.img_addTagFragment_back:
                ((AddAccountActivity) getActivity()).showFragment(AddAccountActivity.ADD_TAG_FRAGMENT, mIndex);

                break;
        }
    }

    class Adapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}

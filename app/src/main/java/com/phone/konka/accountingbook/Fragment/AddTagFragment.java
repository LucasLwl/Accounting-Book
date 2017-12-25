package com.phone.konka.accountingbook.Fragment;

import android.app.Fragment;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.phone.konka.accountingbook.Activity.AddAccountActivity;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.ImageLoader;

import java.util.List;

/**
 * 添加账单字段的Fragment
 * <p>
 * Created by 廖伟龙 on 2017/11/20.
 */

public class AddTagFragment extends Fragment implements View.OnClickListener {

    /**
     * Fragment显示的View
     */
    private View rootView;


    /**
     * 新增字段的图标
     */
    private ImageView mImgTag;


    /**
     * 新增字段的名称
     */
    private EditText mEtTag;


    /**
     * 显示可选图标
     */
    private GridView mGvTag;


    /**
     * 可选图标适配器
     */
    private Adapter mAdapter;


    /**
     * 标志是收入还是支出
     */
    private int mIndex;


    /**
     * 可选图标的resourceId
     */
    private int mIconID;


    /**
     * 图片缓存加载器
     */
    private ImageLoader mImgLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_tag, null);

        initView();
        initData();
        initEven();
        return rootView;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mAdapter != null) {
            mIndex = ((AddAccountActivity) getActivity()).getIndex();
        }
    }


    /**
     * 初始化View
     */
    private void initView() {

        mImgTag = (ImageView) rootView.findViewById(R.id.img_addTagFragment_tag);
        mEtTag = (EditText) rootView.findViewById(R.id.et_addTagFragment_tag);
        mGvTag = (GridView) rootView.findViewById(R.id.gv_addTagFragment_tag);


    }


    /**
     * 初始化数据
     */
    private void initData() {

        TagBean bean = ((AddAccountActivity) getActivity()).mInRecomList.get(0);
        if (bean != null) {
            mImgTag.setImageResource(((AddAccountActivity) getActivity()).mInRecomList.get(0).getIconID());
            mIconID = ((AddAccountActivity) getActivity()).mInRecomList.get(0).getIconID();
        }

        mImgLoader = ImageLoader.getInstance(getActivity());

        mIndex = ((AddAccountActivity) getActivity()).getIndex();
        mAdapter = new Adapter(((AddAccountActivity) getActivity()).mInRecomList);
        mGvTag.setAdapter(mAdapter);
    }


    /**
     * 初始化事件
     */
    private void initEven() {

        rootView.findViewById(R.id.img_addTagFragment_back).setOnClickListener(this);
        rootView.findViewById(R.id.img_addTagFragment_save).setOnClickListener(this);

        mGvTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIconID = ((AddAccountActivity) getActivity()).mInRecomList.get(position).getIconID();
                mImgTag.setImageDrawable(((ImageView) view).getDrawable());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            点击保存
            case R.id.img_addTagFragment_save:

                if (mEtTag.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "类别名不能为空", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    TagBean bean = new TagBean();
                    bean.setText(mEtTag.getText().toString());
                    bean.setIconID(mIconID);
                    if (mIndex == 0) {
                        for (TagBean tag : ((AddAccountActivity) getActivity()).mOutList) {
                            if (tag.getText().equals(bean.getText())) {
                                Toast.makeText(getActivity(), "该标签已存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        ((AddAccountActivity) getActivity()).mOutList.add(((AddAccountActivity) getActivity()).mOutList.size() - 1, bean);
                    } else {
                        for (TagBean tag : ((AddAccountActivity) getActivity()).mInList) {
                            if (tag.getText().equals(bean.getText())) {
                                Toast.makeText(getActivity(), "该标签已存在", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        ((AddAccountActivity) getActivity()).mInList.add(((AddAccountActivity) getActivity()).mInList.size() - 1, bean);
                    }
                }


//                点击返回
            case R.id.img_addTagFragment_back:
                ((AddAccountActivity) getActivity()).showFragment(AddAccountActivity.ADD_TAG_FRAGMENT, mIndex);
                break;
        }
    }

    class Adapter extends BaseAdapter {

        private List<TagBean> mlist;

        public Adapter(List mlist) {
            this.mlist = mlist;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public TagBean getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ImageView(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
                convertView.setLayoutParams(lp);
            }
            mImgLoader.getBitmap(getItem(position).getIconID(), (ImageView) convertView);
            return convertView;
        }
    }
}
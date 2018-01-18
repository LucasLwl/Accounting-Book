package com.phone.konka.accountingbook.Fragment;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.phone.konka.accountingbook.Activity.AddAccountActivity;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.ImageLoader;

import java.util.ArrayList;
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

    /**
     * 显示是否保存PopupWindow
     */
    private PopupWindow mPopupSave;
    private View mPopupView;

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
            mAdapter.setList(((AddAccountActivity) getActivity()).mInRecomList);
            mAdapter.notifyDataSetChanged();

            mEtTag.setText("");
            setIconID();
        }

        if (hidden) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(mEtTag.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * 初始化View
     */
    private void initView() {

        mImgTag = (ImageView) rootView.findViewById(R.id.img_addTagFragment_tag);
        mEtTag = (EditText) rootView.findViewById(R.id.et_addTagFragment_tag);

        mGvTag = (GridView) rootView.findViewById(R.id.gv_addTagFragment_tag);
        mGvTag.setEmptyView(rootView.findViewById(R.id.tv_addTag_noIcon));

        mPopupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_edit_fragment_save, null);
    }


    /**
     * 初始化数据
     */
    private void initData() {


        setIconID();

        mImgLoader = ImageLoader.getInstance(getActivity());

        mIndex = ((AddAccountActivity) getActivity()).getIndex();
        mAdapter = new Adapter(((AddAccountActivity) getActivity()).mInRecomList, getActivity());
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
//                mImgTag.setImageDrawable(((ImageView) view).getDrawable());
                mImgLoader.getBitmap(mIconID, mImgTag);
            }
        });

        mPopupView.findViewById(R.id.tv_popup_editFragment_no).setOnClickListener(this);
        mPopupView.findViewById(R.id.tv_popup_editFragment_yes).setOnClickListener(this);
    }

    public void onBackPress() {
        if (!mEtTag.getText().toString().isEmpty()) {
            if (mPopupSave == null) {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                mPopupSave = new PopupWindow(mPopupView, display.getWidth() / 2, display.getHeight() / 6, true);
                mPopupSave.setBackgroundDrawable(new BitmapDrawable());
                mPopupSave.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        setBackgroundAlpha(1.0f);
                    }
                });
            }
            setBackgroundAlpha(0.5f);
            mPopupSave.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        } else
            ((AddAccountActivity) getActivity()).showFragment(AddAccountActivity.ADD_TAG_FRAGMENT, mIndex);
    }

    private void setBackgroundAlpha(float alpha) {

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }


    /**
     * 设置新增Tag的图标
     */
    private void setIconID() {
        if (((AddAccountActivity) getActivity()).mInRecomList.size() != 0) {
            TagBean bean = ((AddAccountActivity) getActivity()).mInRecomList.get(0);
            if (bean != null) {
                mImgTag.setImageResource(((AddAccountActivity) getActivity()).mInRecomList.get(0).getIconID());
                mIconID = ((AddAccountActivity) getActivity()).mInRecomList.get(0).getIconID();
            }
        } else {
            mImgTag.setImageResource(R.drawable.icon_other);
            mIconID = R.drawable.icon_other;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_popup_editFragment_yes:
                if (mPopupSave != null && mPopupSave.isShowing()) {
                    mPopupSave.dismiss();
                }
//            点击保存
            case R.id.img_addTagFragment_save:

                if (mEtTag.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "类别名不能为空", Toast.LENGTH_SHORT).show();
                } else if (mImgTag.getDrawable() == null) {
                    Toast.makeText(getActivity(), "图标不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    TagBean bean = new TagBean();
                    bean.setText(mEtTag.getText().toString());
                    bean.setIconID(mIconID);
                    if (mIndex == 0) {
                        for (TagBean tag : ((AddAccountActivity) getActivity()).mOutList)
                            if (tag.getText().equals(bean.getText())) {
                                Toast.makeText(getActivity(), "该标签已存在", Toast.LENGTH_SHORT).show();
                                return;
                            }

//                            同步Activity的List
                        ((AddAccountActivity) getActivity()).mOutList.add(((AddAccountActivity) getActivity()).mOutList.size() - 1, bean);

//                        保存Tag
                        ((AddAccountActivity) getActivity()).writeToSharedPreferences(AddAccountActivity.OUT_TAG,
                                ((AddAccountActivity) getActivity()).mOutList);
                    } else {
                        for (TagBean tag : ((AddAccountActivity) getActivity()).mInList)
                            if (tag.getText().equals(bean.getText())) {
                                Toast.makeText(getActivity(), "该标签已存在", Toast.LENGTH_SHORT).show();
                                return;
                            }

//                        同步Activity的List
                        ((AddAccountActivity) getActivity()).mInList.add(((AddAccountActivity) getActivity()).mInList.size() - 1, bean);

//                        保存Tag
                        ((AddAccountActivity) getActivity()).writeToSharedPreferences(AddAccountActivity.IN_TAG,
                                ((AddAccountActivity) getActivity()).mInList);
                    }
                    ((AddAccountActivity) getActivity()).showFragment(AddAccountActivity.ADD_TAG_FRAGMENT, mIndex);
                }
                break;

            case R.id.tv_popup_editFragment_no:
                if (mPopupSave != null && mPopupSave.isShowing()) {
                    mPopupSave.dismiss();
                }
                ((AddAccountActivity) getActivity()).showFragment(AddAccountActivity.ADD_TAG_FRAGMENT, mIndex);
                break;

//                点击返回
            case R.id.img_addTagFragment_back:
                onBackPress();
                break;
        }
    }

    class Adapter extends BaseAdapter {

        private List<TagBean> mlist;

        private Context mContext;

        public Adapter(List mlist, Context context) {
            this.mlist = mlist;
            mContext = context;
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

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_tag, null);
                holder = new ViewHolder();
                holder.imgItem = (ImageView) convertView.findViewById(R.id.img_addTag_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            mImgLoader.getBitmap(getItem(position).getIconID(), holder.imgItem);
            return convertView;
        }


        public void setList(List list) {
            this.mlist = list;
        }

        class ViewHolder {
            ImageView imgItem;
        }
    }
}
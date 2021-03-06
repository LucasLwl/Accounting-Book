package com.phone.konka.accountingbook.Fragment;

import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.phone.konka.accountingbook.Activity.AddAccountActivity;
import com.phone.konka.accountingbook.Adapter.DragGridAdapter;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.View.DragGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/20.
 */

public class EditTagFragment extends Fragment implements View.OnClickListener {


    /**
     * 编辑Fragment的View
     */
    private View rootView;

    /**
     * 显示我的Tag的可拖动GridView
     */
    private DragGridView mGvMyTag;

    /**
     * 显示推荐Tag的可拖动GridView
     */
    private DragGridView mGvRecomTag;


    /**
     * mGvMyTag的适配器
     */
    private DragGridAdapter mMyTagAdapter;

    /**
     * mGvRecomTag的适配器
     */
    private DragGridAdapter mRecomTagAdapter;


    /**
     * mGvMyTag的数据
     */
    private List<TagBean> mMyTagList;

    /**
     * mGvRecomTag的数据
     */
    private List<TagBean> mRecomTagList;


    /**
     * 显示是否保存PopupWindow
     */
    private PopupWindow mPopupSave;
    private View mPopupView;


    /**
     * 标志位，标志是收入的还是支出的tag
     */
    private int mIndex = 0;


    private TagBean mAddTag = null;

    public List<TagBean> getMyTagList() {
        addTagBean();
        return mMyTagList;
    }

    public List<TagBean> getRecomTagList() {
        return mRecomTagList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_tag, null);

        initView();
        initData();
        initEvent();


        return rootView;
    }

    /**
     * 鉴定Fragmen的显示状态改变
     * <p>
     * 当状态变为现实时，修改List的内容，因为可能在EditFragment中修改了Tag
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden && mMyTagAdapter != null && mRecomTagAdapter != null) {
            setList();
            mMyTagAdapter.setList(mMyTagList);
            mRecomTagAdapter.setList(mRecomTagList);
            mMyTagAdapter.notifyDataSetChanged();
            mRecomTagAdapter.notifyDataSetChanged();
        }
    }


    private void initView() {

        mGvMyTag = (DragGridView) rootView.findViewById(R.id.dgv_edit_mytag);
        mGvRecomTag = (DragGridView) rootView.findViewById(R.id.dgv_edit_recomtag);

        mGvMyTag.setEmptyView(rootView.findViewById(R.id.tv_editTag_noTag));
        mGvRecomTag.setEmptyView(rootView.findViewById(R.id.tv_editTag_noRecom));

        mPopupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_edit_fragment_save, null);

    }


    private void initData() {

        setList();

        mMyTagAdapter = new DragGridAdapter(getActivity(), mMyTagList, 0);
        mRecomTagAdapter = new DragGridAdapter(getActivity(), mRecomTagList, 1);

        mGvMyTag.setAdapter(mMyTagAdapter);
        mGvRecomTag.setAdapter(mRecomTagAdapter);

    }

    /**
     * 根据标志位来拷贝数据
     */
    private void setList() {

        mIndex = ((AddAccountActivity) getActivity()).getIndex();
        mMyTagList = new ArrayList<>();
        mRecomTagList = new ArrayList<>();
        if (mIndex == 0) {
            for (TagBean bean : ((AddAccountActivity) getActivity()).mOutList)
                mMyTagList.add(bean);
            for (TagBean bean : ((AddAccountActivity) getActivity()).mOutRecomList)
                mRecomTagList.add(bean);
        } else {
            for (TagBean bean : ((AddAccountActivity) getActivity()).mInList)
                mMyTagList.add(bean);
            for (TagBean bean : ((AddAccountActivity) getActivity()).mInRecomList)
                mRecomTagList.add(bean);
        }
        mAddTag = mMyTagList.remove(mMyTagList.size() - 1);
    }

    private void initEvent() {


        mGvMyTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mRecomTagList.add(mMyTagList.remove(position));
                mMyTagAdapter.notifyDataSetChanged();
                mRecomTagAdapter.notifyDataSetChanged();
            }
        });

        mGvRecomTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mMyTagList.add(mRecomTagList.remove(position));
                mMyTagAdapter.notifyDataSetChanged();
                mRecomTagAdapter.notifyDataSetChanged();
            }
        });

        rootView.findViewById(R.id.img_edit_fragment_back).setOnClickListener(this);
        rootView.findViewById(R.id.img_edit_fragment_save).setOnClickListener(this);


        mPopupView.findViewById(R.id.tv_popup_editFragment_no).setOnClickListener(this);
        mPopupView.findViewById(R.id.tv_popup_editFragment_yes).setOnClickListener(this);

    }

    /**
     * 设置Window的透明度
     *
     * @param alpha
     */
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }


    private boolean checkDataChange() {
        if (mIndex == 0) {
            if (mRecomTagList.size() != ((AddAccountActivity) getActivity()).mOutRecomList.size())
                return true;
            else
                for (int i = 0; i < mRecomTagList.size(); i++)
                    if (!mRecomTagList.get(i).getText()
                            .equals(((AddAccountActivity) getActivity()).mOutRecomList.get(i).getText()))
                        return true;
        } else {
            if (mRecomTagList.size() != ((AddAccountActivity) getActivity()).mInRecomList.size())
                return true;
            else
                for (int i = 0; i < mRecomTagList.size(); i++)
                    if (!mRecomTagList.get(i).getText()
                            .equals(((AddAccountActivity) getActivity()).mInRecomList.get(i).getText()))
                        return true;
        }
        return false;
    }

    public void onBackPress() {

        if (checkDataChange()) {
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
            ((AddAccountActivity) getActivity()).showFragment(AddAccountActivity.EDIT_TAG_FRAGMENT, mIndex);
    }


    private void addTagBean() {
        if (!mMyTagList.contains(mAddTag))
            mMyTagList.add(mAddTag);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_edit_fragment_save:

            case R.id.tv_popup_editFragment_yes:
                addTagBean();
                if (mIndex == 0) {
                    ((AddAccountActivity) getActivity()).mOutList = mMyTagList;
                    ((AddAccountActivity) getActivity()).mOutRecomList = mRecomTagList;

                    //=保存Tag式数据
                    ((AddAccountActivity) getActivity()).writeToSharedPreferences(AddAccountActivity.OUT_TAG, mMyTagList);
                    ((AddAccountActivity) getActivity()).writeToSharedPreferences(AddAccountActivity.OUT_RECOM_TAG, mRecomTagList);
                } else {
                    ((AddAccountActivity) getActivity()).mInList = mMyTagList;
                    ((AddAccountActivity) getActivity()).mInRecomList = mRecomTagList;

                    //保存Tag式数据
                    ((AddAccountActivity) getActivity()).writeToSharedPreferences(AddAccountActivity.IN_TAG, mMyTagList);
                    ((AddAccountActivity) getActivity()).writeToSharedPreferences(AddAccountActivity.IN_RECOM_TAG, mRecomTagList);
                }
            case R.id.tv_popup_editFragment_no:
                if (mPopupSave != null && mPopupSave.isShowing()) {
                    mPopupSave.dismiss();
                }
                ((AddAccountActivity) getActivity()).showFragment(AddAccountActivity.EDIT_TAG_FRAGMENT, mIndex);
                break;
            case R.id.img_edit_fragment_back:
                onBackPress();
        }

    }

}